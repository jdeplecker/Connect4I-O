/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn;

/**
 *
 * @author Joeri
 */
public class Board {

    private final int[][] board; // 0 when no piece, 1 and 2 for player piece
    
    public Board(int width, int height) {
        board = new int[width][height];
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                board[i][j] = 0;
            }
        }
    }
    
    public int GetPiece(int col, int row){
        return board[col][row];
    }
    
    public boolean putPiece(int column, int playernr){
        //check if full & column correct
        int i = board[column].length-1;
        while(i >= 0 && board[column][i]==0){
            i--;
        }
        if(i<GetHeight()-1){
            board[column][i+1] = playernr;
            return true;
        }
        
        return false;
    }
    
    public int GetWidth() {
        return board.length;
    }

    public int GetHeight() {
        return board[0].length;
    }
    
    public void Print(){
        
        for(int j=GetHeight()-1; j>=0; j--){
            for(int i=0; i<GetWidth(); i++){
                
                if(board[i][j]>0){
                    System.out.print(board[i][j]+ " ");                    
                }
                else{
                    System.out.print("- ");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
