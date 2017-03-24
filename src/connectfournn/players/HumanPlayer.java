/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.players;

import connectfournn.game.Board;
import java.util.Scanner;

/**
 *
 * @author Joeri
 */
public class HumanPlayer extends APlayer{
    Scanner in = new Scanner(System.in);
    
    public HumanPlayer(int playerNr) {
        super(playerNr);
    }
    
    @Override
     public boolean MakeMove(Board b){
        System.out.println("Enter columnnr to insert (0 - " + (b.GetWidth()-1) + "):");
        int col = in.nextInt();
        if(col > 0 && col < b.GetWidth()){
            return b.putPiece(col, playerNr);
        }
        
        return false;
    } 
}
