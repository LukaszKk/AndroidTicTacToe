package com.example.tictactoe;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import static com.example.tictactoe.Board.board;

@RequiresApi(api = Build.VERSION_CODES.R)
public class GameStatus {

    public static List<Integer> winnerCombination = new ArrayList<>();

    public static Integer checkWinCondition(int availableCount) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0].getValue() != Player.NONE.getValue()) &&
                    equals(board[i][0].getValue(), board[i][1].getValue(), board[i][2].getValue())) {
                winnerCombination = List.of(board[i][0].getId(), board[i][1].getId(), board[i][2].getId());
                return board[i][0].getValue();
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((board[0][i].getValue() != Player.NONE.getValue()) &&
                    equals(board[0][i].getValue(), board[1][i].getValue(), board[2][i].getValue())) {
                winnerCombination = List.of(board[0][i].getId(), board[1][i].getId(), board[2][i].getId());
                return board[0][i].getValue();
            }
        }

        if ((board[0][0].getValue() != Player.NONE.getValue()) &&
                equals(board[0][0].getValue(), board[1][1].getValue(), board[2][2].getValue())) {
            winnerCombination = List.of(board[0][0].getId(), board[1][1].getId(), board[2][2].getId());
            return board[0][0].getValue();
        }

        if ((board[0][2].getValue() != Player.NONE.getValue()) &&
                equals(board[0][2].getValue(), board[1][1].getValue(), board[2][0].getValue())) {
            winnerCombination = List.of(board[0][2].getId(), board[1][1].getId(), board[2][0].getId());
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
