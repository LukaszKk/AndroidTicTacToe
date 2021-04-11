package com.example.tictactoe;

public enum Player {
    HUMAN(-10),
    AI(10),
    NONE(0);

    private final int value;

    Player(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
