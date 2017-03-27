/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

import connectfournn.machinelearning.PopulationManager;
import connectfournn.game.GameInstance;
import connectfournn.players.APlayer;
import connectfournn.players.EasyAIPlayer;
import connectfournn.players.EasyHighestAIPlayer;
import connectfournn.players.HumanPlayer;

/**
 *
 * @author Joeri
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // play game
        PopulationManager pm = new PopulationManager(true, false);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
//        PlayGame(new HumanPlayer(1), pm.GetHighestPlayer());
        pm.Improve(Settings.GENERATION_COUNT);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
        
        PlayGame(new EasyHighestAIPlayer(1), pm.GetHighestPlayer());
        PlayGame(new EasyAIPlayer(1), pm.GetHighestPlayer());
    }
    
    public static void PlayGame(APlayer p1, APlayer p2){
        
        GameInstance gi = new GameInstance(p1, p2);
        while(gi.GetWonState()==0){
            gi.PlayTurn();
            gi.PrintBoard();
        }
        gi.PrintWonState();
    }
}
