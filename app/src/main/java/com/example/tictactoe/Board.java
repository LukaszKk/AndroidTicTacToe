package com.example.tictactoe;

import android.app.Activity;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final List<List<Integer>> winningCombinations = List.of(List.of(R.id.button4, R.id.button2, R.id.button3),
            List.of(R.id.button3, R.id.button7, R.id.button6),
            List.of(R.id.button6, R.id.button8, R.id.button9),
            List.of(R.id.button9, R.id.button5, R.id.button4),
            List.of(R.id.button9, R.id.button5, R.id.button4),
            List.of(R.id.button2, R.id.button1, R.id.button8),
            List.of(R.id.button4, R.id.button1, R.id.button6),
            List.of(R.id.button9, R.id.button1, R.id.button3));

    public static final int[][] board = {
            {R.id.button4, R.id.button2, R.id.button3},
            {R.id.button5, R.id.button1, R.id.button7},
            {R.id.button9, R.id.button8, R.id.button6}
    };

    public static int[][] boardMap = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };
}
