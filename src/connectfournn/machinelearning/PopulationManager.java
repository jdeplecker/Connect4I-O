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

    private static final Random rnd = new Random();
    
    private NNPlayer best = new NNPlayer(1);
    private boolean trainp1 = false;
    
    PriorityQueue<NNPlayer> members = new PriorityQueue<>((a,b) -> (int)(b.getFitness()*1000 - a.getFitness()*1000));
    
    public PopulationManager(boolean loadbest, boolean trainp1) {
        
        this.trainp1 = trainp1;        
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
    
    
    private void TestMember(NNPlayer player){
        
        int gamesamount = Settings.GAME_COUNT;
                
        StatsManager stats = new StatsManager();
        APlayer easy = new EasyAIPlayer(1);
        APlayer easyhighest = new EasyHighestAIPlayer(1);
        
        for (int i = 0; i < gamesamount; i++) {
           
            APlayer opponent;
            
            if(i<gamesamount/3){
                opponent = easy;             
            }
            else if(i<gamesamount/3*2){
                opponent = best;
            }
            else{
                opponent = easyhighest;    
            }
            
            GameInstance gi;
            if(trainp1){
                gi = new GameInstance(player, opponent);
            }
            else{
                gi = new GameInstance(opponent, player);
            }
            
            
            while (gi.GetWonState() == 0) {
                gi.PlayTurn();
                stats.AddTurn();
            }
            stats.AddStat(gi.GetWonState());
        }
        
        player.setFitness(stats.GetP2WinPercentage() + stats.GetTurns()/5);
    }
    
    public NNPlayer GetHighestPlayer(){
        return members.element();
    }
    
    public void Improve(int itterations){
        System.out.println("before improvement:" + CalcAverage());
        best = members.peek();
        for(int itt=0; itt< itterations; itt++){
            ArrayList<NNPlayer> highest = new ArrayList<>();
            int memberscount = members.size();
            
            for(int i=0; i<Settings.BEST_KEPT; i++){
                highest.add(members.poll());
            }
            members.clear();
            int i=0;
            while(i<(memberscount-Settings.BEST_KEPT)/2){ //cross produce
                
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
            while(i<memberscount-Settings.BEST_KEPT){ //mutate
                
                NNPlayer p = highest.get(rnd.nextInt(highest.size()));
                double[] g = NNGenomeAdapter.NNToGenome(p.getNeuralNetwork());
                
                NNPlayer child = new NNPlayer(2, NNGenomeAdapter.GenomeToNN(MutateGenome(g)));
                
                TestMember(child);
                members.add(child);
                i++;
            }
            for(i=0; i<Settings.BEST_KEPT; i++){
                TestMember(highest.get(i));
                members.add(highest.get(i));
            }
            
            System.out.print("gen " + itt + ": " + new DecimalFormat("#00.00%").format(CalcAverage()));
            System.out.println(" highest: " + new DecimalFormat("#00.00%").format(members.peek().getFitness()));
        }
        best = members.peek();
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
        output[output.length-3] = properties[0];
        output[output.length-2] = properties[1];
        output[output.length-1] = properties[2];
        
        return output;
    }
    
    private double[] MutateGenome(double[] g){
        double[] output = g;
        double[] properties = Arrays.copyOfRange(g, g.length-3, g.length);
        
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
