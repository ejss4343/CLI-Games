package com.practice.games.reversi;

import com.practice.games.boardgame.Color;

public enum Symbol {
    O("0"), BLANK("-");

    private String name;

    Symbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}