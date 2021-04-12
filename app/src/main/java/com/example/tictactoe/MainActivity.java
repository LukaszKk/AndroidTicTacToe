package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class MainActivity extends AppCompatActivity {

    private static final AlphaAnimation ALPHA_ANIMATION = new AlphaAnimation(1F, 0.6F);
    private static boolean isPvP = false;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.game = Game.initialize(this, isPvP);
    }

    public void newGame(View view) {
        view.startAnimation(ALPHA_ANIMATION);
        game = Game.initialize(this, isPvP);
    }

    public void newPvP(View view) {
        isPvP = true;
        view.startAnimation(ALPHA_ANIMATION);
        game = Game.initialize(this, isPvP);
    }

    public void newPvE(View view) {
        isPvP = false;
        view.startAnimation(ALPHA_ANIMATION);
        game = Game.initialize(this, isPvP);
    }

    public void finish(View view) {
        view.startAnimation(ALPHA_ANIMATION);
        finish();
    }

    public void changeButtonState(View view) {
        game.changeButtonState(view);
    }
}