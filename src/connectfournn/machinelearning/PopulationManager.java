/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

import connectfournn.Settings;
import connectfournn.game.GameInstance;
import connectfournn.players.*;
import connectfournn.utility.StatsManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joeri
 */
public class PopulationManager {

    private static final Random rnd = new Random();
    private static final ExecutorService workers = Executors.newCachedThreadPool();
    
    private NNPlayer best = new NNPlayer(1);
    private GenomeUtility genomeutility = new GenomeUtility();
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
            genomeutility.PrintGenome(genome);
        }
        for(int i=0; i<size; i++){
            NNPlayer m = new NNPlayer(2);
            members.add(TestMember(m));
        }
        SaveBest();
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
            Collection<Callable<NNPlayer>> tasks = new ArrayList<>();
            int i=0;
            
            while(i<(memberscount-Settings.BEST_KEPT)/(rnd.nextFloat()*4+1)){ //cross produce
                
                NNPlayer p1 = highest.get(rnd.nextInt(highest.size()));
                double[] g1 = NNGenomeAdapter.NNToGenome(p1.getNeuralNetwork());
                
                NNPlayer p2 = highest.get(rnd.nextInt(highest.size()));
                double[] g2 = NNGenomeAdapter.NNToGenome(p2.getNeuralNetwork());
                
                double[] combined = genomeutility.CombineGenomes(g1, g2);
                NNPlayer child = new NNPlayer(2, NNGenomeAdapter.GenomeToNN(combined));
                
                tasks.add((Callable<NNPlayer>) () -> TestMember(child));
//                members.add(TestMember(child));
                i++;
            }
            while(i<memberscount-Settings.BEST_KEPT){ //mutate
                
                NNPlayer p = highest.get(rnd.nextInt(highest.size()));
                double[] g = NNGenomeAdapter.NNToGenome(p.getNeuralNetwork());
                
                NNPlayer child = new NNPlayer(2, NNGenomeAdapter.GenomeToNN(genomeutility.MutateGenome(g)));
                
                tasks.add((Callable<NNPlayer>) () -> TestMember(child));
//                members.add(TestMember(child));
                i++;
            }
                        
            for(i=0; i<Settings.BEST_KEPT; i++){
                NNPlayer highestfromprevious = highest.get(i);
                tasks.add((Callable<NNPlayer>) () -> TestMember(highestfromprevious));
//                members.add(TestMember(highest.get(i)));
            }
            
            List<Future<NNPlayer>> results;
            try {
                results = workers.invokeAll(tasks, 10, TimeUnit.SECONDS);
                for (Future<NNPlayer> f : results) {
                  members.add(f.get());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.print("gen " + itt + ": " + new DecimalFormat("#00.00%").format(CalcAverage()));
            System.out.println(" highest: " + new DecimalFormat("#00.00%").format(members.peek().getFitness()));
        }
        best = members.peek();
        SaveBest();
    }
    
    public NNPlayer TestMember(NNPlayer player){
        
        int gamesamount = Settings.GAME_COUNT;
        StatsManager stats = new StatsManager();;
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
        
        player.setFitness(stats.GetP2WinPercentage() + stats.GetTurns()/200 + stats.GetDrawPercentage()/4);
        
        return player;
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
            members.add(TestMember(bestplayer));
        }
    }
}
