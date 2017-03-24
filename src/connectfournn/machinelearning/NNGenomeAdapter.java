/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

/**
 *
 * @author Joeri
 */
public class NNGenomeAdapter {
    
    public static double[] NNToGenome(NeuralNetwork nn){
        
        double[][] hiddenToOutput = nn.getHiddenToOutput();
        double[][] inputToHidden = nn.getInputToHidden();
        
        //3 extra for input count, hidden count and output count
        int genomelength = inputToHidden.length * inputToHidden[0].length + hiddenToOutput.length*hiddenToOutput[0].length + 3;
        
        double[] genome = new double[genomelength];

        for (int i = 0; i < inputToHidden.length; i++) {
            for (int j = 0; j < inputToHidden[0].length; j++) {
                genome[inputToHidden[0].length * i + j] = inputToHidden[i][j];
            }
        }
        int startSecond = inputToHidden.length * inputToHidden[0].length;

        for (int i = 0; i < hiddenToOutput.length; i++) {
            for (int j = 0; j < hiddenToOutput[0].length; j++) {
                genome[hiddenToOutput[0].length * i + j + startSecond] = hiddenToOutput[i][j];
            }
        }
        genome[genome.length-3] = nn.getInputLength();
        genome[genome.length-2] = nn.getHiddenLength();
        genome[genome.length-1] = nn.getOutputLength();
        
        return genome;
    }
    
    public static NeuralNetwork GenomeToNN(double[] genome){
        int inputlength = (int)genome[genome.length-3];
        int hiddenlength = (int)genome[genome.length-2];
        int outputlength = (int)genome[genome.length-1];
        
        if(genome.length != inputlength * hiddenlength + hiddenlength*outputlength + 3){
            throw new ArrayIndexOutOfBoundsException("Genome doesn't have the right format");
        }
        
        NeuralNetwork nn = new NeuralNetwork(inputlength, hiddenlength, outputlength);
        
        int index = 0;

        for (int i = 0; i < inputlength; i++) {
            for (int j = 0; j < hiddenlength; j++) {
                nn.getInputToHidden()[i][j] = genome[index];
                index++;
            }
        }
//        int startSecond = inputlength * hiddenlength + 1;

        for (int i = 0; i < hiddenlength; i++) {
            for (int j = 0; j < outputlength; j++) {
                nn.getHiddenToOutput()[i][j] = genome[index];
                index++;
            }
        }
        
        return nn;
    }
}
