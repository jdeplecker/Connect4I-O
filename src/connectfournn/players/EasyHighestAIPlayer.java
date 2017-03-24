/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.players;

import connectfournn.game.Board;
import java.util.ArrayList;
import java.util.Random;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Joeri
 */
public class EasyHighestAIPlayer extends APlayer{

    Random rnd = new Random();

    public EasyHighestAIPlayer(int playerNr) {
        super(playerNr);
    }
    
    @Override
     public boolean MakeMove(Board b) {
        
        int highest = 0;
        int highestindex = 0;
        
        for(int i=0; i<b.GetWidth(); i++){
            int row = 0;
            while(row<b.GetHeight() && b.GetPiece(i, row)>0){
                row++;
            }
            if(row>=highest && row<b.GetHeight()){
                highest = row;
                highestindex = i;
            }
        }
        return b.putPiece(highestindex, playerNr);
        
    }
    
}
