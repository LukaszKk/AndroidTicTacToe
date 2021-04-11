package com.example.tictactoe;

public class BoardXY {

    private int x;
    private int y;

    private BoardXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static BoardXY of(int x, int y) {
        return new BoardXY(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
