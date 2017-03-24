/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfournn.game;

/**
 *
 * @author Joeri
 */
public class BoardStateChecker {

    public static BoardStateChecker instance = new BoardStateChecker();

    private BoardStateChecker() {

    }

    // -1 for draw
    // 0 for not won    
    // 1 for player 1
    // 2 for player 2    
    public int CheckWon(Board b) {

        int horizontal = CheckHorizontal(b);
        if (horizontal != 0) {
            return horizontal;
        }
        int vertical = CheckVertical(b);
        if (vertical != 0) {
            return vertical;
        }

        int diagonal = CheckDiagonal(b);
        if (diagonal != 0) {
            return diagonal;
        }

        if (CheckFull(b)) {
            return -1;
        }

        return 0;
    }

    //0 not won
    // 1 or 2 for won player
    private int CheckHorizontal(Board b) {
        for (int row = 0; row < b.GetHeight(); row++) {
            for (int col = 0; col < b.GetWidth() - 3; col++) {
                if (b.GetPiece(col, row)>0) {

                    int playernr = b.GetPiece(col, row);

                    boolean isfour = true;
                    for (int i = 1; i < 4 && isfour; i++) {
                        if (b.GetPiece(col + i, row) != playernr) {
                            col += i;
                            isfour = false;
                        }
                    }
                    if (isfour) {
                        return playernr;
                    }
                }
            }
        }
        return 0;
    }

    private int CheckVertical(Board b) {
        for (int col = 0; col < b.GetWidth(); col++) {
            for (int row = 0; row < b.GetHeight() - 3; row++) {
                if (b.GetPiece(col, row)>0) {

                    int playernr = b.GetPiece(col, row);

                    boolean isfour = true;
                    for (int i = 1; i < 4 && isfour; i++) {
                        if (b.GetPiece(col, row + i) != playernr) {
                            isfour = false;
                        }
                    }
                    if (isfour) {
                        return playernr;
                    }
                }
            }
        }

        return 0;
    }

    private int CheckDiagonal(Board b) {

        //left under to right top
        for (int col = 0; col < b.GetWidth() - 3; col++) {
            for (int row = 0; row < b.GetHeight() - 3; row++) {
                if (b.GetPiece(col, row)>0) {
                    int playernr = b.GetPiece(col, row);

                    boolean isfour = true;
                    for (int i = 1; i < 4 && isfour; i++) {
                        if (b.GetPiece(col + i, row + i)==0 || b.GetPiece(col + i, row + i) != playernr) {
                            isfour = false;
                        }
                    }
                    if (isfour) {
                        return playernr;
                    }
                }
            }
        }

        //right under to left top
        for (int col = 3; col < b.GetWidth(); col++) {
            for (int row = 0; row < b.GetHeight() - 3; row++) {
                if (b.GetPiece(col, row)>0) {
                    int playernr = b.GetPiece(col, row);

                    boolean isfour = true;
                    for (int i = 1; i < 4 && isfour; i++) {
                        if (b.GetPiece(col - i, row + i)==0 || b.GetPiece(col - i, row + i) != playernr) {
                            isfour = false;
                        }
                    }
                    if (isfour) {
                        return playernr;
                    }
                }
            }
        }

        return 0;
    }

    public boolean CheckFull(Board b) {
        boolean isfull = true;
        for (int i = 0; i < b.GetWidth() && isfull; i++) {
            isfull = b.GetPiece(i, b.GetHeight() - 1)>0;
        }
        return isfull;
    }
}
