package com.example.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Button;

import java.util.List;

import static com.example.tictactoe.Board.board;

public class ButtonWrapper {

    private final Activity activity;

    public ButtonWrapper(Activity activity) {
        this.activity = activity;
    }

    public void initButtonsState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                initButtonState(board[i][j].getId());
            }
        }
    }

    public void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                disableButton(board[i][j].getId());
            }
        }
    }

    public void initButtonState(int id) {
        Button button = activity.findViewById(id);
        setInitButtonState(button);
    }

    private void setInitButtonState(Button button) {
        button.setEnabled(true);
        button.setText(activity.getString(R.string.default_square_value));
        button.setTextColor(Color.BLACK);
    }

    public void disableButton(int id) {
        activity.findViewById(id).setEnabled(false);
    }

    public void setButtonTextColor(Button button, String text, int color) {
        button.setText(text);
        button.setTextColor(color);
    }

    public void setButtonsColor(List<Integer> buttons) {
        for (int id : buttons) {
            Button button = activity.findViewById(id);
            button.setTextColor(activity.getColor(R.color.dark_green));
        }
    }
}
