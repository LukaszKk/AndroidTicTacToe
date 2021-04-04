package com.example.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class ButtonManager {

    private final Activity activity;

    public ButtonManager(Activity activity) {
        this.activity = activity;
    }

    public void initButtonsState(List<Integer> buttons) {
        for (int id: buttons) {
            Button button = activity.findViewById(id);
            setButtonState(button);
            setButtonColor(button, Color.WHITE);
        }
    }

    private void setButtonState(Button button) {
        button.setEnabled(true);
        button.setText(activity.getString(R.string.default_square_value));
        button.setTextColor(Color.BLACK);
    }

    public void disableButtons(List<Integer> buttons) {
        for (int id: buttons) {
            activity.findViewById(id).setEnabled(false);
        }
    }

    public static void setButtonTextColor(Button button, String text, int color) {
        button.setText(text);
        button.setTextColor(color);
    }

    public static void setButtonColor(Button button, int color) {
        button.setBackgroundColor(color);
    }
}
