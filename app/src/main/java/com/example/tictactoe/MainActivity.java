package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.game = Game.initialize(this);
    }

    public void newGame(View view) {
        game.newGame();
    }

    public void finish(View view) {
        finish();
    }

    public void changeButtonState(View view) {
        game.changeButtonState(view);
    }
}