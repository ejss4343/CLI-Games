package com.practice.games.boardgame;

public enum State {
    IN_PROGRESS("In progress"), WON("Won"), TIE("Tie");
    private final String name;
    State(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }

}