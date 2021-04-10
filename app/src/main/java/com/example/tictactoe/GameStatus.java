package com.example.tictactoe;

import static com.example.tictactoe.ButtonManager.setButtonTextColor;
import static com.example.tictactoe.Board.winningCombinations;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiresApi(api = Build.VERSION_CODES.R)
public class GameStatus {

    private final Activity activity;
    private final ButtonManager buttonManager;
    private final List<Integer> buttons;
    private List<Integer> taken;
    private boolean playerStart;
    private String playerValue;
    private String computerValue;

    private GameStatus(Activity activity) {
        this.activity = activity;
        this.playerStart = ThreadLocalRandom.current().nextInt(0, 2) == 0;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
            this.playerValue = activity.getString(R.string.o_square_value);
            this.computerValue = activity.getString(R.string.x_square_value);
        } else {
            this.playerValue = activity.getString(R.string.x_square_value);
            this.computerValue = activity.getString(R.string.o_square_value);
        }
        this.buttonManager = new ButtonManager(activity);
        this.buttons = new ArrayList<>();
        this.taken = new ArrayList<>();

        for(int i = 1; i < 10; i++) {
            String name = "button" + i;
            int buttonId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            buttons.add(buttonId);
        }

        newGame();
    }

    public static GameStatus initialize(Activity activity) {
        return new GameStatus(activity);
    }

    public void newGame() {
        buttonManager.initButtonsState(buttons);
        if (!playerStart) {
            List<Integer> move = aiTurn();
            Button aiButton = activity.findViewById(Board.board[move.get(0)][move.get(1)]);
            setButtonTextColor(aiButton, computerValue, Color.BLUE);
        }
    }

    public void changeButtonState(View view) {
        Button button = (Button) view;
        if (!button.getText().equals(activity.getString(R.string.default_square_value))) {
            return;
        }

        Board.boardMap[i][j] = -1;
        setButtonTextColor(button, playerValue, Color.CYAN);

        List<Integer> move = aiTurn();
        Button aiButton = activity.findViewById(Board.board[move.get(0)][move.get(1)]);
        setButtonTextColor(aiButton, computerValue, Color.BLUE);

        taken.add(button.getId());
        playerStart = !playerStart;

        checkEndCondition(view);
    }

    private void checkEndCondition(View view) {
        boolean end = false;
        String winner = "";
        if (checkWinConditionFor(playerValue)) {
            end = true;
            winner = playerValue;
        } else if (checkWinConditionFor(computerValue)) {
            end = true;
            winner = computerValue;
        }

        if (!end) {
            return;
        }

        buttonManager.disableButtons(buttons);
        showWinPopUp(view, winner);
    }

    private boolean checkWinConditionFor(String value) {
        for (List<Integer> combination: winningCombinations) {
            Button button1 = activity.findViewById(combination.get(0));
            Button button2 = activity.findViewById(combination.get(1));
            Button button3 = activity.findViewById(combination.get(2));

            boolean val = checkButtonCombination(button1, button2, button3, value);
            if (val) {
                button1.setTextColor(Color.GREEN);
                button2.setTextColor(Color.GREEN);
                button3.setTextColor(Color.GREEN);
                return true;
            }
        }

        return false;
    }

    private boolean checkButtonCombination(Button button1, Button button2, Button button3, String value) {
        return button1.getText().toString().equals(value)
                && button2.getText().toString().equals(value)
                && button3.getText().toString().equals(value);
    }

    private void showWinPopUp(View view, String winner) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        TextView textView = popupView.findViewById(R.id.popup_text);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(String.format(activity.getString(R.string.winner), winner));

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }

    private List<Integer> aiTurn() {
        long bestScore = -Long.MAX_VALUE;
        List<Integer> move = List.of(0, 0);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Board.boardMap[i][j] == 0) {
                    Board.boardMap[i][j] = 1;
                    long score = minimax(0, true);
                    Board.boardMap[i][j] = 0;
                    if (score > bestScore) {
                        bestScore = score;
                        move = List.of(i, j);
                    }
                }
            }
        }

        return move;
    }

    private long minimax(int depth, boolean isMaximazing) {
        int winValue = checkWinConditionForBoardMap();
        if (winValue != -2) {
            return winValue;
        }

        if (isMaximazing) {
            long bestScore = -Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (Board.boardMap[i][j] == 0) {
                        Board.boardMap[i][j] = 1;
                        long score = minimax(depth + 1, false);
                        Board.boardMap[i][j] = 0;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            long bestScore = Long.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (Board.boardMap[i][j] == 0) {
                        Board.boardMap[i][j] = -1;
                        long score = minimax(depth + 1, true);
                        Board.boardMap[i][j] = 0;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
