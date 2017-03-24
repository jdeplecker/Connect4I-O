/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

import connectfournn.Settings;
import connectfournn.players.*;
import connectfournn.game.GameInstance;
import connectfournn.utility.StatsManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author Joeri
 */
public class PopulationManager {

    private static Random rnd = new Random();
    
    private NNPlayer best = new NNPlayer(1);
    
    PriorityQueue<NNPlayer> members = new PriorityQueue<>((a,b) -> (int)(b.getFitness()*1000 - a.getFitness()*1000));
    
    public PopulationManager(boolean loadbest) {
        
        int size = Settings.GENERATION_COUNT;
        
        if(loadbest){
            size--;
            LoadBest();
            best = members.peek();
            double[] genome = NNGenomeAdapter.NNToGenome(best.getNeuralNetwork());
            PrintGenome(genome);
        }
        for(int i=0; i<size; i++){
            NNPlayer m = new NNPlayer(2);
            TestMember(m);
            members.add(m);
        }
        SaveBest();
    }
    
    
    private void TestMember(NNPlayer p2){
        
        int gamesamount = Settings.GAME_COUNT;
                
        StatsManager stats = new StatsManager();
        APlayer easy = new EasyAIPlayer(1);
        APlayer easyhighest = new EasyHighestAIPlayer(1);
        
        for (int i = 0; i < gamesamount; i++) {
            GameInstance gi;
            if(i<gamesamount/3){
                gi = new GameInstance(easy, p2);                
            }
            else if(i<gamesamount/3*2){
                gi = new GameInstance(best, p2);    
            }
            else{
                gi = new GameInstance(easyhighest, p2);           
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
                
                NNPlayer p1 = highest.get(rnd.nextInt(highest.size()));
                double[] g1 = NNGenomeAdapter.NNToGenome(p1.getNeuralNetwork());
                
                NNPlayer p2 = highest.get(rnd.nextInt(highest.size()));
                double[] g2 = NNGenomeAdapter.NNToGenome(p2.getNeuralNetwork());
                
                double[] combined = CombineGenomes(g1, g2);
                NNPlayer child = new NNPlayer(2, NNGenomeAdapter.GenomeToNN(combined));
                
                TestMember(child);
                members.add(child);
                i++;
            }
            for(i=0; i<5; i++){
                TestMember(highest.get(i));
                members.add(highest.get(i));
            }
            
            System.out.print("after itt " + itt + ": " + new DecimalFormat("#00.00%").format(CalcAverage()));
            System.out.println(" highest: " + new DecimalFormat("#00.00%").format(members.peek().getFitness()));
        }
        SaveBest();
    }
    
    private double[] CombineGenomes(double[] g1, double[] g2){
        double[] properties = Arrays.copyOfRange(g1, g1.length-3, g1.length);
        
        
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
        
        output[output.length-3] = properties[0];
        output[output.length-2] = properties[1];
        output[output.length-1] = properties[2];
        
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
        
        double[] genome = NNGenomeAdapter.NNToGenome(members.element().getNeuralNetwork());
        GenomeLoader.SaveGenome(genome);
    }
    
    private void LoadBest(){
        double[] genome = GenomeLoader.LoadGenome();
        if(genome.length>0){
            NNPlayer bestplayer = new NNPlayer(2, NNGenomeAdapter.GenomeToNN(genome));
            TestMember(bestplayer);
            members.add(bestplayer);
        }
    }
    
    private void PrintGenome(double[] genome){
        for(double d: genome){
            System.out.print(d + " - ");
        }
        System.out.println("");
    }
}
