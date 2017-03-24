/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.players;

import connectfournn.Settings;
import connectfournn.game.Board;
import connectfournn.machinelearning.NeuralNetwork;

/**
 *
 * @author Joeri
 */
public class NNPlayer extends APlayer {

    private double fitness = 0;
    
    private NeuralNetwork nn;
    
    public NNPlayer(int playerNr) {
        super(playerNr);
        
        int width = Settings.BOARD_WIDTH;
        int height = Settings.BOARD_HEIGHT;
        int hiddencount = Settings.HIDDENLAYER_COUNT;
        
        nn = new NeuralNetwork(width*height*2, hiddencount, width);
    }
    
    public NNPlayer(int playerNr, NeuralNetwork nn){
        super(playerNr);
        this.nn = nn;
    }
    
    @Override
    public boolean MakeMove(Board b) {
        int output = nn.GetOutput(b.ConvertToNNInput());
        return b.putPiece(output, playerNr);        
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public NeuralNetwork getNeuralNetwork() {
        return nn;
    }
}
