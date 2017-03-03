/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

import machinelearning.PopulationManager;
import game.GameInstance;
import players.EasyAIPlayer;
import players.EasyHighestAIPlayer;

/**
 *
 * @author Joeri
 */
public class ConnectFourNN {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // play game
        PopulationManager pm = new PopulationManager(200, true);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
        pm.Improve(250);
        System.out.println("Highest %: " + pm.GetHighestPlayer().getFitness()*100);
        
        pm.GetHighestPlayer().playerNr = 2;
        GameInstance gi = new GameInstance(new EasyHighestAIPlayer(1), pm.GetHighestPlayer(), 7, 6);
        while(gi.GetWonState()==0){
            gi.PlayTurn();
            gi.PrintBoard();
        }
        gi.PrintWonState();
        
        
        gi = new GameInstance(new EasyAIPlayer(1), pm.GetHighestPlayer(), 7, 6);
        while(gi.GetWonState()==0){
            gi.PlayTurn();
            gi.PrintBoard();
        }
        gi.PrintWonState();
    }
}
