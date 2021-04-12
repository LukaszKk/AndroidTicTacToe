package com.example.tictactoe;

import static com.example.tictactoe.Board.board;
import static com.example.tictactoe.Board.resetBoard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private final Activity activity;
    private final ButtonWrapper buttonWrapper;
    private final boolean isPvP;
    private final String playerValue;
    private final String computerValue;
    private boolean pvpActualPlayer;
    private int availableCount;

    private Game(Activity activity, boolean isPvP) {
        this.activity = activity;
        this.buttonWrapper = new ButtonWrapper(activity);

        this.isPvP = isPvP;
        this.pvpActualPlayer = true;
        boolean playerStart = isPvP || ThreadLocalRandom.current().nextInt(0, 2) == 0;
        this.playerValue = playerStart ? activity.getString(R.string.x_square_value) : activity.getString(R.string.o_square_value);
        this.computerValue = playerStart ? activity.getString(R.string.o_square_value) : activity.getString(R.string.x_square_value);
        this.availableCount = 9;

        resetBoard();
        buttonWrapper.initButtonsState();
        if (!isPvP) {
            buttonWrapper.disableButton(R.id.buttonPvE);
            buttonWrapper.enableButton(R.id.buttonPvP);
            setInfoText(String.format(activity.getString(R.string.playerInfo), playerValue));
            if (!playerStart) {
                makeMove();
            }
        } else {
            buttonWrapper.disableButton(R.id.buttonPvP);
            buttonWrapper.enableButton(R.id.buttonPvE);
            setInfoText(String.format(activity.getString(R.string.turnInfo), playerValue));
        }
    }

    public static Game initialize(Activity activity, boolean isPvP) {
        return new Game(activity, isPvP);
    }

    private void setInfoText(String infoText) {
        TextView infoView = activity.findViewById(R.id.infoView);
        infoView.setText(infoText);
    }

    public void changeButtonState(View view) {
        Button button = (Button) view;
        if (!button.getText().equals(activity.getString(R.string.default_square_value))) {
            return;
        }

        makeMove(button, pvpActualPlayer ? activity.getColor(R.color.red) : activity.getColor(R.color.dark_red));
        if (checkEndCondition(view)) {
            return;
        }

        if (isPvP) {
            pvpActualPlayer = !pvpActualPlayer;
            setInfoText(String.format(activity.getString(R.string.turnInfo), pvpActualPlayer ? playerValue : computerValue));
            return;
        }

        // ai
        makeMove();
        checkEndCondition(view);
    }

    /**
     * Picks best move
     */
    private void makeMove() {
        BoardXY move = Objects.requireNonNull(MovePicker.bestMove(availableCount));
        board[move.getX()][move.getY()].setValue(Player.AI.getValue());
        Button aiButton = activity.findViewById(board[move.getX()][move.getY()].getId());
        buttonWrapper.setButtonTextColor(aiButton, computerValue, activity.getColor(R.color.dark_red));
        availableCount--;
    }

    /**
     * Sets chosen move
     */
    private void makeMove(Button button, int buttonColor) {
        Element element = Board.findElementOfId(button.getId());
        Objects.requireNonNull(element).setValue(pvpActualPlayer ? Player.HUMAN.getValue() : Player.AI.getValue());
        buttonWrapper.setButtonTextColor(button, pvpActualPlayer ? playerValue : computerValue, buttonColor);
        availableCount--;
    }

    public boolean checkEndCondition(View view) {
        Integer winValue = GameStatus.checkWinCondition(availableCount);
        if (winValue == null) {
            return false;
        }

        if (winValue == Player.NONE.getValue()) {
            finish(view, activity.getString(R.string.draw));
            return true;
        }

        String winner = winValue == Player.HUMAN.getValue() ? playerValue : computerValue;
        buttonWrapper.setButtonsColor(GameStatus.winnerCombination);
        finish(view, String.format(activity.getString(R.string.winner), winner));
        return true;
    }

    private void finish(View view, String text) {
        buttonWrapper.disableButtons();
        setInfoText(text);
        PopUpActivity.showWinPopUp(activity, view, text);
    }
}
