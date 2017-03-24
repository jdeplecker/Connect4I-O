/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

import connectfournn.machinelearning.PopulationManager;
import connectfournn.game.GameInstance;
import connectfournn.players.EasyAIPlayer;
import connectfournn.players.EasyHighestAIPlayer;

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
        PopulationManager pm = new PopulationManager(false);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
        pm.Improve(50);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
        
        GameInstance gi = new GameInstance(new EasyHighestAIPlayer(1), pm.GetHighestPlayer());
        while(gi.GetWonState()==0){
            gi.PlayTurn();
            gi.PrintBoard();
        }
        gi.PrintWonState();
        
        
        gi = new GameInstance(new EasyAIPlayer(1), pm.GetHighestPlayer());
        while(gi.GetWonState()==0){
            gi.PlayTurn();
            gi.PrintBoard();
        }
        gi.PrintWonState();
    }
}
