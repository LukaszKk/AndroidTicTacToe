package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private GameStatus gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gameStatus = GameStatus.initialize(this);
    }

    public void newGame(View view) {
        gameStatus.newGame();
    }

    public void finish(View view) {
        finish();
    }

    public void changeButtonState(View view) {
        gameStatus.changeButtonState(view);
    }
}