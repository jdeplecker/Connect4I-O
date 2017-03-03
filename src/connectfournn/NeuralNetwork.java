/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

import java.util.Random;

/**
 *
 * @author Joeri
 */
public class NeuralNetwork {

    private static Random rnd = new Random();

    private double[] inputLayer;
    private double[] hiddenLayer;
    private double[] outputLayer;

    private double[][] inputToHidden;
    private double[][] hiddenToOutput;

    public NeuralNetwork(int inputcount, int hiddencount, int outputcount) {
        this(inputcount, hiddencount, outputcount, GenerateGenome(inputcount * hiddencount + hiddencount * outputcount));
    }

    public NeuralNetwork(int inputcount, int hiddencount, int outputcount, double[] genome) {
        inputLayer = new double[inputcount];
        hiddenLayer = new double[hiddencount];
        outputLayer = new double[outputcount];

        inputToHidden = new double[inputcount][hiddencount];
        hiddenToOutput = new double[hiddencount][outputcount];
        SetGenome(genome);
    }

    private static double[] GenerateGenome(int size) {
        double[] output = new double[size];
//        for (int i = 0; i < size; i++) {
//            output[i] = 0;
//        }
        output[rnd.nextInt(size)] = rnd.nextInt(3)-1;
        output[rnd.nextInt(size)] = rnd.nextInt(3)-1;
        return output;
    }

    public int GetOutput(double[] input) {
        this.inputLayer = input;

        CalcHidden();
        CalcOutput();

        int biggest = 0;
        for (int i = 1; i < outputLayer.length; i++) {
            if (outputLayer[i] > outputLayer[biggest]) {
                biggest = i;
            }
        }

        return biggest;
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
            input[i] = 2 / (1 + Math.exp(-4.9*input[i]))-1;
        }

        return input;
    }

    public double[] GetGenome() {
        double[] output = new double[inputToHidden.length * inputToHidden[0].length + hiddenToOutput.length * hiddenToOutput[0].length];

        for (int i = 0; i < inputToHidden.length; i++) {
            for (int j = 0; j < inputToHidden[0].length; j++) {
                output[inputToHidden[0].length * i + j] = inputToHidden[i][j];
            }
        }
        int startSecond = inputToHidden.length * inputToHidden[0].length;

        for (int i = 0; i < hiddenToOutput.length; i++) {
            for (int j = 0; j < hiddenToOutput[0].length; j++) {
                output[hiddenToOutput[0].length * i + j + startSecond] = hiddenToOutput[i][j];
            }
        }
        return output;
    }

    public void SetGenome(double[] genome) {
        int index = 0;

        for (int i = 0; i < inputToHidden.length; i++) {
            for (int j = 0; j < inputToHidden[0].length; j++) {
                inputToHidden[i][j] = genome[index];
                index++;
            }
        }
        int startSecond = inputToHidden.length * inputToHidden[0].length + 1;

        for (int i = 0; i < hiddenToOutput.length; i++) {
            for (int j = 0; j < hiddenToOutput[0].length; j++) {
                hiddenToOutput[i][j] = genome[index];
                index++;
            }
        }
    }
}
