package com.example.tictactoe;

/**
 * Static board
 */
public class Board {

    public static Element[][] board;

    static {
        resetBoard();
    }

    public static void resetBoard() {
        board = new Element[][] {
                {Element.of(R.id.button4, 0), Element.of(R.id.button2, 0), Element.of(R.id.button3, 0)},
                {Element.of(R.id.button5, 0), Element.of(R.id.button1, 0), Element.of(R.id.button7, 0)},
                {Element.of(R.id.button9, 0), Element.of(R.id.button8, 0), Element.of(R.id.button6, 0)}
        };
    }

    public static Element findElementOfId(int id) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Element element = board[i][j];
                if (element.getId() == id) {
                    return element;
                }
            }
        }
        return null;
    }
}
