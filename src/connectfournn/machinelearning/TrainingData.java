/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

import connectfournn.game.Board;
import java.util.ArrayList;

/**
 *
 * @author Joeri
 */
public class TrainingData {
    public TrainingData(int width, int height){
        CreateTrainingData(width, height);
    }
    
    private void CreateTrainingData(int width, int height){
        ArrayList<State> states = new ArrayList<>();
        
        // horizontal states
        for(int i=0; i<width-4; i++){
            for(int j=0; j<height; j++){
                Board b = new Board(width, height);
//                b.putPiece(i, j);
                
                
                State s = new State();
                
                
            }
        }
    }
}
