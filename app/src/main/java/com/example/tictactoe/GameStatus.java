package com.example.tictactoe;

import android.view.View;

import static com.example.tictactoe.Board.board;

public class GameStatus {

    public static Integer checkWinCondition(int availableCount) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0].getValue() != Player.NONE.getValue()) &&
                    equals(board[i][0].getValue(), board[i][1].getValue(), board[i][2].getValue())) {
                return board[i][0].getValue();
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((board[0][i].getValue() != Player.NONE.getValue()) &&
                    equals(board[0][i].getValue(), board[1][i].getValue(), board[2][i].getValue())) {
                return board[0][i].getValue();
            }
        }

        if ((board[0][0].getValue() != Player.NONE.getValue()) &&
                equals(board[0][0].getValue(), board[1][1].getValue(), board[2][2].getValue())) {
            return board[0][0].getValue();
        }

        if ((board[0][2].getValue() != Player.NONE.getValue()) &&
                equals(board[0][2].getValue(), board[1][1].getValue(), board[2][0].getValue())) {
            return board[0][2].getValue();
        }

        if (availableCount == 0) {
            return Player.NONE.getValue();
        }

        return null;
    }

    private static boolean equals(int first, int second, int third) {
        return (first == second) && (first == third);
    }
}
