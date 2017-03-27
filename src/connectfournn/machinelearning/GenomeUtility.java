/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.machinelearning;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Joeri
 */
public class GenomeUtility {
    
    private static final Random rnd = new Random();
    
    public void PrintGenome(double[] genome){
        for(double d: genome){
            System.out.print(d + " - ");
        }
        System.out.println("");
    }
    
    public double[] CombineGenomes(double[] g1, double[] g2){
        double[] properties = Arrays.copyOfRange(g1, g1.length-3, g1.length);
        
        
        double[] output = new double[g1.length];
        for(int i=0; i<g1.length; i++){
            output[i] = g1[i];
            if(rnd.nextBoolean()){
                output[i] = g2[i];
            }
        }
        output[output.length-3] = properties[0];
        output[output.length-2] = properties[1];
        output[output.length-1] = properties[2];
        
        return output;
    }
    
    public double[] MutateGenome(double[] g){
        double[] output = g;
        double[] properties = Arrays.copyOfRange(g, g.length-3, g.length);
        
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        output[rnd.nextInt(output.length)] = (rnd.nextInt(3)-1);
        
        output[output.length-3] = properties[0];
        output[output.length-2] = properties[1];
        output[output.length-1] = properties[2];  
        
        return output;
    }
}
