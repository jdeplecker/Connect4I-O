/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joeri
 */
public class PopulationManager {

    private static Random rnd = new Random();
    
    
    private static final int TESTGAMES = 250;

    private static final int WIDTH = 7;
    private static final int HEIGHT = 6;
    
    private NNPlayer best = new NNPlayer(1, WIDTH, HEIGHT);
    
    PriorityQueue<NNPlayer> members = new PriorityQueue<>((a,b) -> (int)(b.getFitness()*1000 - a.getFitness()*1000));
    
    public PopulationManager(int size, boolean loadbest) {
        if(loadbest){
            size--;
            LoadBest();
            best = members.peek();
            best.playerNr = 1;
            PrintGenome(best.getNeuralNetwork().GetGenome());
        }
        for(int i=0; i<size; i++){
            NNPlayer m = new NNPlayer(2, WIDTH, HEIGHT);
            TestMember(m, TESTGAMES);
            members.add(m);
        }
        SaveBest();
    }
    
    
    private void TestMember(NNPlayer p2, int gamesamount){
        
        StatsManager stats = new StatsManager();
        p2.playerNr = 2;
        best.playerNr = 1;
        APlayer easy = new EasyAIPlayer(1);
        APlayer easyhighest = new EasyHighestAIPlayer(1);
        
        for (int i = 0; i < gamesamount; i++) {
            GameInstance gi;
            if(i<gamesamount/3){
                gi = new GameInstance(easy, p2, WIDTH, HEIGHT);                
            }
            else if(i<gamesamount/3*2){
                gi = new GameInstance(best, p2, WIDTH, HEIGHT);    
            }
            else{
                gi = new GameInstance(easyhighest, p2, WIDTH, HEIGHT);           
            }
            while (gi.GetWonState() == 0) {
                gi.PlayTurn();
                stats.AddTurn();
            }
            stats.AddStat(gi.GetWonState());
        }
        
        p2.setFitness(stats.GetP2WinPercentage());
    }
    
    public NNPlayer GetHighestPlayer(){
        return members.element();
    }
    
    public void Improve(int itterations){
        System.out.println("before improvement:" + CalcAverage());
        for(int itt=0; itt< itterations; itt++){
            ArrayList<NNPlayer> highest = new ArrayList<>();
            int memberscount = members.size();
            best = members.peek();
            
            for(int i=0; i<10; i++){
                highest.add(members.poll());
            }
            members.clear();
            int i=0;
            while(i<memberscount-5){
                NNPlayer child = new NNPlayer(2, WIDTH, HEIGHT);
                double[] g1 = highest.get(rnd.nextInt(highest.size())).getNeuralNetwork().GetGenome();
                double[] g2 = highest.get(rnd.nextInt(highest.size())).getNeuralNetwork().GetGenome();
                double[] combined = CombineGenomes(g1, g2);
                child.getNeuralNetwork().SetGenome(combined);
                TestMember(child, TESTGAMES);
                members.add(child);
                i++;
            }
            for(i=0; i<5; i++){
                TestMember(highest.get(i), TESTGAMES);
                members.add(highest.get(i));
            }
            
            System.out.print("after itt " + itt + ": " + new DecimalFormat("#00.00%").format(CalcAverage()));
            System.out.println(" highest: " + new DecimalFormat("#00.00%").format(members.peek().getFitness()));
        }
        SaveBest();
    }
    
    private double[] CombineGenomes(double[] g1, double[] g2){
        double[] output = new double[g1.length];
        for(int i=0; i<g1.length; i++){
            output[i] = g1[i];
            if(rnd.nextBoolean()){
                output[i] = g2[i];
            }
        }
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        
        return output;
    }
    
    private double CalcAverage(){
        double avg = 0;
        for(NNPlayer p: members){
            avg += p.getFitness();
        }
        avg/=members.size();
        return avg;
    }
    
    private void SaveBest(){
        
        double[] genome = members.element().getNeuralNetwork().GetGenome();
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("BestGenome.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(genome);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void LoadBest(){
        double[] genome;
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("BestGenome.txt");
            ObjectInputStream iis = new ObjectInputStream(fis);
            genome = (double[]) iis.readObject();
            
            if(genome.length>0){
                NNPlayer bestplayer = new NNPlayer(2, WIDTH, HEIGHT);
                bestplayer.getNeuralNetwork().SetGenome(genome);
                TestMember(bestplayer, TESTGAMES);
                members.add(bestplayer);
            }
        } catch ( IOException | ClassNotFoundException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void PrintGenome(double[] genome){
        for(double d: genome){
            System.out.print(d + " - ");
        }
        System.out.println("");
    }
}
