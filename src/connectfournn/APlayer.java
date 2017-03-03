/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

/**
 *
 * @author Joeri
 */
public class APlayer {

    int playerNr;
    
    public APlayer(int playerNr) {
        this.playerNr = playerNr;
    }    
    
    boolean MakeMove(Board b){
        return true;
    }
    
    public int GetPlayerNr(){
        return playerNr;
    }
}
