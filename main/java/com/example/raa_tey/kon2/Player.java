package com.example.raa_tey.kon2;

class Player
{
    private int score;
    private char color;
    private String name;

    public Player() {
        this.score = 0;
        this.color = ' ';
        this.name = " ";
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public char getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
