/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Joeri
 */
public class StatsManager {
    private int[] stats = new int[4];
    private int totalgames = 0;
    private int turns = 0;
    
    public void AddStat(int gamestate){
        stats[gamestate + 1]++;
        totalgames++;
    }
    
    public void AddTurn(){
        turns++;
    }
    
    public double GetP1WinPercentage(){
        return (double) stats[2] / totalgames;
    }
    
    public double GetP2WinPercentage(){
        return (double) stats[3] / totalgames;
        
    }
    
    public double GetDrawPercentage(){
        return (double) stats[0] / totalgames;
    }
    
    public double GetTurns(){
        return (double) turns/totalgames;
    }
    
    public void PrintStats(){
        System.out.println(GetDrawPercentage() + "% draws");
        System.out.println(GetP1WinPercentage() + "% p1 won");
        System.out.println(GetP2WinPercentage() + "% p2 won");
    }
}
