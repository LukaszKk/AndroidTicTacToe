package com.example.tictactoe;

public class Board {

//    public static final List<List<Integer>> winningCombinations = List.of(List.of(R.id.button4, R.id.button2, R.id.button3),
//            List.of(R.id.button3, R.id.button7, R.id.button6),
//            List.of(R.id.button6, R.id.button8, R.id.button9),
//            List.of(R.id.button9, R.id.button5, R.id.button4),
//            List.of(R.id.button9, R.id.button5, R.id.button4),
//            List.of(R.id.button2, R.id.button1, R.id.button8),
//            List.of(R.id.button4, R.id.button1, R.id.button6),
//            List.of(R.id.button9, R.id.button1, R.id.button3));

    public static Element[][] board = {
            {Element.of(R.id.button4, 0), Element.of(R.id.button2, 0), Element.of(R.id.button3, 0)},
            {Element.of(R.id.button5, 0), Element.of(R.id.button1, 0), Element.of(R.id.button7, 0)},
            {Element.of(R.id.button9, 0), Element.of(R.id.button8, 0), Element.of(R.id.button6, 0)}
    };

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
