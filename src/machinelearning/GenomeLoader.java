/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machinelearning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import players.NNPlayer;

/**
 *
 * @author Joeri
 */
public class GenomeLoader {

    public GenomeLoader() {
    }
    
    public static void SaveGenome(double[] genome){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("BestGenome.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(genome);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static double[] LoadGenome(){
         double[] genome = new double[0];
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("BestGenome.txt");
            ObjectInputStream iis = new ObjectInputStream(fis);
            genome = (double[]) iis.readObject();
        } catch ( IOException | ClassNotFoundException ex) {
            Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PopulationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return genome;
    }
}
