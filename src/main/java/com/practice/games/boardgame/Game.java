package com.practice.games.boardgame;

public interface Game {
    void reset();
    void help();
    void move(int row, int col) throws MoveConflictException;
    void printEnterMoveMsg();
    boolean inBounds(int row, int col);
    String getName();
    void toggleIndices();
    boolean isActive();
    boolean handleInput(String move);

}
