/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package players;

import game.Board;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Joeri
 */
public class EasyAIPlayer extends APlayer{

    Random rnd = new Random();

    public EasyAIPlayer(int playerNr) {
        super(playerNr);
    }
    
    
    @Override
     public boolean MakeMove(Board b) {
        
        ArrayList possibleColumns = new ArrayList();
        
        for(int i=0; i<b.GetWidth(); i++){
            if(b.GetPiece(i, b.GetHeight()-1)==0){
                possibleColumns.add(i);
            }
        }
        if(possibleColumns.size()>0){
            return b.putPiece((int)possibleColumns.get(rnd.nextInt(possibleColumns.size())), playerNr);            
        }
        
        return false;
    }
    
}
