/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

import java.util.Random;

/**
 *
 * @author Joeri
 */
public class NeuralNetwork {

    private static final Random rnd = new Random();

    private double[] inputLayer;
    private double[] hiddenLayer;
    private double[] outputLayer;

    private double[][] inputToHidden;
    private double[][] hiddenToOutput;

    public NeuralNetwork(int inputcount, int hiddencount, int outputcount) {
        inputLayer = new double[inputcount];
        hiddenLayer = new double[hiddencount];
        outputLayer = new double[outputcount];

        inputToHidden = new double[inputcount][hiddencount];
        hiddenToOutput = new double[hiddencount][outputcount];
        InitConnections();
    }

    private void InitConnections() {
        inputToHidden[rnd.nextInt(inputToHidden.length)][rnd.nextInt(inputToHidden[0].length)] = rnd.nextInt(3)-1;
        hiddenToOutput[rnd.nextInt(hiddenToOutput.length)][rnd.nextInt(hiddenToOutput[0].length)] = rnd.nextInt(3)-1;
    }

    public double[] GetOutput(double[] input) {
        this.inputLayer = input;

        CalcHidden();
        CalcOutput();

        return outputLayer;
    }

    private void CalcHidden() {
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenLayer[i] = 0;
            for (int j = 0; j < inputLayer.length; j++) {
                hiddenLayer[i] += inputLayer[j] * inputToHidden[j][i];
            }
            hiddenLayer[i] /= inputLayer.length;
        }

        hiddenLayer = UseActivationFunction(hiddenLayer);
    }

    private void CalcOutput() {
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = 0;
            for (int j = 0; j < hiddenLayer.length; j++) {
                outputLayer[i] += hiddenLayer[j] * hiddenToOutput[j][i];
            }
            outputLayer[i] /= hiddenLayer.length;

        }

        outputLayer = UseActivationFunction(outputLayer);
    }

    private double[] UseActivationFunction(double[] input) {

        for (int i = 0; i < input.length; i++) {
            //input[i] = 2 / (1 + Math.exp(-4.9*input[i]))-1; //sigmoid
            input[i] = -2 * input[i] * Math.exp(-input[i]*input[i]);
        }

        return input;
    }

    public double[][] getInputToHidden() {
        return inputToHidden;
    }

    public void setInputToHidden(double[][] inputToHidden) {
        this.inputToHidden = inputToHidden;
    }

    public double[][] getHiddenToOutput() {
        return hiddenToOutput;
    }

    public void setHiddenToOutput(double[][] hiddenToOutput) {
        this.hiddenToOutput = hiddenToOutput;
    }
    
    public int getInputLength(){
        return inputLayer.length;
    }
    
    public int getHiddenLength(){
        return hiddenLayer.length;
    }
    
    public int getOutputLength(){
        return outputLayer.length;
    }
}
