/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.game;

import connectfournn.Settings;
import connectfournn.players.APlayer;

/**
 *
 * @author Joeri
 */
public class GameInstance {

    private final APlayer player1;
    private final APlayer player2;
    private final Board board;

    private boolean p1Turn;
    private int wonState;

    public GameInstance(APlayer p1, APlayer p2) {
        this.player1 = p1;
        player1.playerNr = 1;
        this.player2 = p2;
        player2.playerNr = 2;

        p1Turn = true;
        wonState = 0;
        board = new Board(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT);
    }

    public void PlayTurn() {
        if (wonState == 0) {
            boolean movemade = false;
            
            if (p1Turn) {
                movemade = player1.MakeMove(board);
            } else {
                movemade = player2.MakeMove(board);
            }
            p1Turn = !p1Turn;
            if(movemade){
                wonState = BoardStateChecker.instance.CheckWon(board);
            }
            else{
                wonState = -1;
            }
        }
    }
    
    public void PrintBoard(){
        board.Print();
    }

    public void PrintWonState() {
        switch (wonState) {
            case -1:
                System.out.println("DRAW");
                break;
            case 1:
                System.out.println("Player 1 won!");
                break;
            case 2:
                System.out.println("Player 2 won!");
                break;
            default:
                System.out.println("Unexpected won state");
                break;
        }

    }

    public APlayer GetPlayer1() {
        return player1;
    }

    public APlayer GetPlayer2() {
        return player2;
    }

    public int GetWonState() {
        return wonState;
    }

}
