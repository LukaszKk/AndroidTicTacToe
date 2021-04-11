package com.example.tictactoe;

public enum Player {
    HUMAN(-1),
    AI(1),
    NONE(0);

    private final int value;

    Player(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
