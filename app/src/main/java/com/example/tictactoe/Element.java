package com.example.tictactoe;

public final class Element {

    private int id;
    private int value;

    private Element(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public static Element of(int id, int value) {
        return new Element(id, value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
