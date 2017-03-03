/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package players;

import game.Board;
import machinelearning.NeuralNetwork;

/**
 *
 * @author Joeri
 */
public class NNPlayer extends APlayer {

    private static final int HIDDENCOUNT = 15;
    private double fitness = 0;
    
    private NeuralNetwork nn;
    
    public NNPlayer(int playerNr, int width, int height) {
        super(playerNr);
        nn = new NeuralNetwork(width*height*2, HIDDENCOUNT, width);
    }

    @Override
    public boolean MakeMove(Board b) {
        int output = nn.GetOutput(ConvertToNNInput(b));
        return b.putPiece(output, playerNr);        
    }

    private double[] ConvertToNNInput(Board b) {
        double[] output = new double[b.GetWidth() * b.GetHeight()*2];
        for (int j = 0; j < b.GetHeight(); j++) {
            for (int i = 0; i < b.GetWidth(); i++) {
                if(b.GetPiece(i, j)==0){                    
                    output[j*b.GetWidth()+i*2] = 1;
                }
                else{
                    output[j*b.GetWidth()+i*2] = 1;
                    output[j*b.GetWidth()+i*2+1] = (double)(b.GetPiece(i, j))/2;
                }
            }
        }
        
        return output;
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
