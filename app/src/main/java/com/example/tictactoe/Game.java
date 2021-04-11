package com.example.tictactoe;

import static com.example.tictactoe.Board.board;
import static com.example.tictactoe.ButtonWrapper.setButtonTextColor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Game {

    private final Activity activity;
    private final ButtonWrapper buttonWrapper;
    private final List<Integer> buttons;
    private final boolean playerStart;
    private final String playerValue;
    private final String computerValue;
    private int availableCount;

    private Game(Activity activity) {
        this.activity = activity;
        this.playerStart = ThreadLocalRandom.current().nextInt(0, 2) == 0;
        this.playerValue = playerStart ? activity.getString(R.string.x_square_value) : activity.getString(R.string.o_square_value);
        this.computerValue = playerStart ? activity.getString(R.string.o_square_value) : activity.getString(R.string.x_square_value);
        this.buttonWrapper = new ButtonWrapper(activity);
        this.buttons = new ArrayList<>();
        this.availableCount = 9;

        for(int i = 1; i < 10; i++) {
            String name = "button" + i;
            int buttonId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            buttons.add(buttonId);
        }

        newGame();
    }

    public static Game initialize(Activity activity) {
        return new Game(activity);
    }

    public void newGame() {
        buttonWrapper.initButtonsState(buttons);
        if (!playerStart) {
            BoardXY move = Objects.requireNonNull(bestMove());
            board[move.getX()][move.getY()].setValue(Player.AI.getValue());
            Button aiButton = activity.findViewById(board[move.getX()][move.getY()].getId());
            setButtonTextColor(aiButton, computerValue, Color.BLUE);
        }
    }

    public void changeButtonState(View view) {
        Button button = (Button) view;
        if (!button.getText().equals(activity.getString(R.string.default_square_value))) {
            return;
        }

        Element element = Board.findElementOfId(button.getId());
        Objects.requireNonNull(element).setValue(-1);
        setButtonTextColor(button, playerValue, Color.CYAN);
        availableCount--;

        if (checkEndCondition(view)) {
            return;
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        BoardXY move = Objects.requireNonNull(bestMove());
        board[move.getX()][move.getY()].setValue(Player.AI.getValue());
        Button aiButton = activity.findViewById(board[move.getX()][move.getY()].getId());
        setButtonTextColor(aiButton, computerValue, Color.BLUE);
        availableCount--;

        checkEndCondition(view);
    }

    private boolean checkEndCondition(View view) {
        int winValue = checkWinConditionForBoardMap();

        if (winValue != Player.NONE.getValue()) {
            String winner = winValue == Player.HUMAN.getValue() ? playerValue : computerValue;
            finish(view, String.format(activity.getString(R.string.winner), winner));
            return true;
        }

        if (availableCount == 0) {
            finish(view, activity.getString(R.string.draw));
            return true;
        }

        return false;
    }

//    private void checkEndCondition(View view) {
//        boolean end = false;
//        String winner = "";
//        if (checkWinConditionFor(playerValue)) {
//            end = true;
//            winner = playerValue;
//        } else if (checkWinConditionFor(computerValue)) {
//            end = true;
//            winner = computerValue;
//        }
//
//        if (!end) {
//            return;
//        }
//
//        buttonWrapper.disableButtons(buttons);
//        showWinPopUp(view, winner);
//    }

//    private boolean checkWinConditionFor(String value) {
//        for (List<Integer> combination: winningCombinations) {
//            Button button1 = activity.findViewById(combination.get(0));
//            Button button2 = activity.findViewById(combination.get(1));
//            Button button3 = activity.findViewById(combination.get(2));
//
//            boolean val = checkButtonCombination(button1, button2, button3, value);
//            if (val) {
//                button1.setTextColor(Color.GREEN);
//                button2.setTextColor(Color.GREEN);
//                button3.setTextColor(Color.GREEN);
//                return true;
//            }
//        }
//
//        return false;
//    }

//    private boolean checkButtonCombination(Button button1, Button button2, Button button3, String value) {
//        return button1.getText().toString().equals(value)
//                && button2.getText().toString().equals(value)
//                && button3.getText().toString().equals(value);
//    }

    private void finish(View view, String text) {
        buttonWrapper.disableButtons(buttons);
        showWinPopUp(view, text);
    }

    private void showWinPopUp(View view, String text) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        TextView textView = popupView.findViewById(R.id.popup_text);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(text);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private BoardXY bestMove() {
        long bestScore = -Long.MAX_VALUE;
        BoardXY move = null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getValue() == Player.NONE.getValue()) {
                    board[i][j].setValue(Player.AI.getValue());
                    long score = miniMax(0, true);
                    board[i][j].setValue(Player.NONE.getValue());
                    if (score > bestScore) {
                        bestScore = score;
                        move = BoardXY.of(i, j);
                    }
                }
            }
        }

        return move;
    }

    private long miniMax(int depth, boolean isMaximazing) {
        int winValue = checkWinConditionForBoardMap();
        if (winValue != Player.NONE.getValue()) {
            return winValue;
        }
        if (availableCount == 0) {
            return Player.NONE.getValue();
        }

        if (isMaximazing) {
            long bestScore = -Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getValue() == Player.NONE.getValue()) {
                        board[i][j].setValue(Player.AI.getValue());
                        long score = miniMax(depth + 1, false);
                        board[i][j].setValue(Player.NONE.getValue());
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            long bestScore = Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getValue() == Player.NONE.getValue()) {
                        board[i][j].setValue(Player.HUMAN.getValue());
                        long score = miniMax(depth + 1, true);
                        board[i][j].setValue(Player.NONE.getValue());
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private int checkWinConditionForBoardMap() {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0].getValue() != Player.NONE.getValue()) &&
                    equals(board[i][0].getValue(), board[i][2].getValue(), board[i][3].getValue())) {
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

        return Player.NONE.getValue();
    }

    private boolean equals(int first, int second, int third) {
        return (first == second) && (first == third);
    }
}
