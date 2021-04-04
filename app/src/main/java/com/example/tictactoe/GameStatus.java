package com.example.tictactoe;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.tictactoe.ButtonManager.setButtonColor;
import static com.example.tictactoe.ButtonManager.setButtonTextColor;

@RequiresApi(api = Build.VERSION_CODES.R)
public class GameStatus {

    private static final List<List<Integer>> winningCombinations = List.of(List.of(R.id.button4, R.id.button2, R.id.button3),
            List.of(R.id.button3, R.id.button7, R.id.button6),
            List.of(R.id.button6, R.id.button8, R.id.button9),
            List.of(R.id.button9, R.id.button5, R.id.button4),
            List.of(R.id.button9, R.id.button5, R.id.button4),
            List.of(R.id.button2, R.id.button1, R.id.button8),
            List.of(R.id.button4, R.id.button1, R.id.button6),
            List.of(R.id.button9, R.id.button1, R.id.button3));

    private final Activity activity;
    private final ButtonManager buttonManager;
    private final List<Integer> buttons;
    private boolean playerChange;

    private GameStatus(Activity activity) {
        this.activity = activity;
        this.playerChange = false;
        this.buttonManager = new ButtonManager(activity);
        this.buttons = new ArrayList<>();

        for(int i = 1; i < 10; i++) {
            String name = "button" + i;
            int buttonId = activity.getResources().getIdentifier(name, "id", activity.getPackageName());
            buttons.add(buttonId);
        }
    }

    public static GameStatus initialize(Activity activity) {
        return new GameStatus(activity);
    }

    public void newGame() {
        buttonManager.initButtonsState(buttons);
    }

    public void changeButtonState(View view) {
        Button button = (Button) view;
        if (!button.getText().equals(activity.getString(R.string.default_square_value))) {
            return;
        }

        if (playerChange) {
            setButtonTextColor(button, activity.getString(R.string.o_square_value), Color.CYAN);
        } else {
            setButtonTextColor(button, activity.getString(R.string.x_square_value), Color.BLUE);
        }
        playerChange = !playerChange;

        checkEndCondition(view);
    }

    private void checkEndCondition(View view) {
        boolean end = false;
        String winner = "";
        if (checkWinConditionFor(activity.getString(R.string.o_square_value))) {
            end = true;
            winner = activity.getString(R.string.o_square_value);
        } else if (checkWinConditionFor(activity.getString(R.string.x_square_value))) {
            end = true;
            winner = activity.getString(R.string.x_square_value);
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
                setButtonColor(button1, Color.GREEN);
                setButtonColor(button2, Color.GREEN);
                setButtonColor(button3, Color.GREEN);
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
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        TextView textView = (TextView) popupView.findViewById(R.id.popup_text);
        textView.setText("Player " + winner + " won!");

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
}
