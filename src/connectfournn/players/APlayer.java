/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.players;

import connectfournn.game.Board;

/**
 *
 * @author Joeri
 */
public class APlayer {

    public int playerNr;
    
    public APlayer(int playerNr) {
        this.playerNr = playerNr;
    }    
    
    public boolean MakeMove(Board b){
        return true;
    }
}
