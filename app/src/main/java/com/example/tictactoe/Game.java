package com.example.tictactoe;

import static com.example.tictactoe.Board.board;
import static com.example.tictactoe.Board.resetBoard;
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

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Game {

    private final Activity activity;
    private final ButtonWrapper buttonWrapper;
    private final String playerValue;
    private final String computerValue;
    private int availableCount;

    private Game(Activity activity) {
        this.activity = activity;
        boolean playerStart = ThreadLocalRandom.current().nextInt(0, 2) == 0;
        this.playerValue = playerStart ? activity.getString(R.string.x_square_value) : activity.getString(R.string.o_square_value);
        this.computerValue = playerStart ? activity.getString(R.string.o_square_value) : activity.getString(R.string.x_square_value);
        this.buttonWrapper = new ButtonWrapper(activity);
        this.availableCount = 9;

        resetBoard();
        initButtonsState();
        if (!playerStart) {
            BoardXY move = Objects.requireNonNull(bestMove());
            board[move.getX()][move.getY()].setValue(Player.AI.getValue());
            Button aiButton = activity.findViewById(board[move.getX()][move.getY()].getId());
            setButtonTextColor(aiButton, computerValue, Color.BLUE);
            availableCount--;
        }
    }

    private void initButtonsState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonWrapper.initButtonState(board[i][j].getId());
            }
        }
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonWrapper.disableButton(board[i][j].getId());
            }
        }
    }

    public static Game initialize(Activity activity) {
        return new Game(activity);
    }

    public void changeButtonState(View view) {
        Button button = (Button) view;
        if (!button.getText().equals(activity.getString(R.string.default_square_value))) {
            return;
        }

        Element element = Board.findElementOfId(button.getId());
        Objects.requireNonNull(element).setValue(Player.HUMAN.getValue());
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

    private void printBoard() {
        System.out.println("AAAAAAAAAAAAA");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(i + "," + j + ": " + board[i][j].getValue());
            }
        }
    }

    private boolean checkEndCondition(View view) {
        Integer winValue = checkWinCondition();
        if (winValue == null) {
            return false;
        }

        if (winValue == Player.NONE.getValue()) {
            finish(view, activity.getString(R.string.draw));
            return true;
        }

        System.out.println("BBBBBB: " + playerValue);
        System.out.println(winValue);
        System.out.println(Player.HUMAN.getValue());

        String winner = winValue == Player.HUMAN.getValue() ? playerValue : computerValue;
        finish(view, String.format(activity.getString(R.string.winner), winner));
        return true;
    }

    private void finish(View view, String text) {
        disableButtons();
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
                    availableCount--;

                    long score = miniMax(0, false);

                    board[i][j].setValue(Player.NONE.getValue());
                    availableCount++;

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
        Integer winValue = checkWinCondition();
        if (winValue != null) {
            return winValue;
        }

        long bestScore;
        if (isMaximazing) {
            bestScore = -Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getValue() == Player.NONE.getValue()) {
                        board[i][j].setValue(Player.AI.getValue());
                        availableCount--;

                        long score = miniMax(depth + 1, false);

                        board[i][j].setValue(Player.NONE.getValue());
                        availableCount++;

                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else {
            bestScore = Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].getValue() == Player.NONE.getValue()) {
                        board[i][j].setValue(Player.HUMAN.getValue());
                        availableCount--;

                        long score = miniMax(depth + 1, true);

                        board[i][j].setValue(Player.NONE.getValue());
                        availableCount++;

                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }

        return bestScore;
    }

    private Integer checkWinCondition() {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0].getValue() != Player.NONE.getValue()) &&
                    equalsTriple(board[i][0].getValue(), board[i][1].getValue(), board[i][2].getValue())) {
                return board[i][0].getValue();
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((board[0][i].getValue() != Player.NONE.getValue()) &&
                    equalsTriple(board[0][i].getValue(), board[1][i].getValue(), board[2][i].getValue())) {
                return board[0][i].getValue();
            }
        }

        if ((board[0][0].getValue() != Player.NONE.getValue()) &&
                equalsTriple(board[0][0].getValue(), board[1][1].getValue(), board[2][2].getValue())) {
            return board[0][0].getValue();
        }

        if ((board[0][2].getValue() != Player.NONE.getValue()) &&
                equalsTriple(board[0][2].getValue(), board[1][1].getValue(), board[2][0].getValue())) {
            return board[0][2].getValue();
        }

        if (availableCount == 0) {
            return Player.NONE.getValue();
        }

        return null;
    }

    private boolean equalsTriple(int first, int second, int third) {
        return (first == second) && (first == third);
    }
}
