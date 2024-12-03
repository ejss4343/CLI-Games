package com.practice.games.minesweeper;

import com.practice.games.boardgame.Color;

public class Cell {

    public static final int[][] DELTAS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
        {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    public enum Covered {
        FLAGGED(Color.format(Color.ANSI_RED, "-")), UNFLAGGED("-");

        private String name;

        Covered(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Type {
        MINE(Color.format(Color.ANSI_RED, "*"), -1),
        ZERO(Color.format(Color.ANSI_WHITE, "0"), 0),
        ONE(Color.format(Color.ANSI_WHITE, "1"), 1),
        TWO(Color.format(Color.ANSI_WHITE, "2"), 2),
        THREE(Color.format(Color.ANSI_PURPLE, "3"), 3),
        FOUR(Color.format(Color.ANSI_BLUE, "4"), 4),
        FIVE(Color.format(Color.ANSI_CYAN, "5"), 5),
        SIX(Color.format(Color.ANSI_GREEN, "6"), 6),
        SEVEN(Color.format(Color.ANSI_YELLOW, "7"), 7),
        EIGHT(Color.format(Color.ANSI_RED, "8"), 8);

        private String name;
        private int value;

        Type(String name, int value){
            this.name = name;
            this.value = value;
        }
        @Override
        public String toString() {
            return this.name;
        }
        public static Type fromValue(int value) {
            for (Type t : Type.values()) {
                if (t.value == value) {
                    return t;
                }
            }
            return null;
        }
    }

    private int row;
    private int col;
    private Type type;
    private boolean covered;
    private boolean flagged;

    public Cell(int row, int col){
        this(row, col, null);
    }

    public Cell(int row, int col, Type type){
        this.row = row;
        this.col = col;
        this.type = type;
        this.covered = true;
        this.flagged = false;
    }

    public boolean isCovered() { return covered; }

    public boolean isMine() { return this.type == Type.MINE; }

    public void flag() {
        this.flagged = !this.flagged;
    }

    public void flip(){
        this.covered = false;
    }

    public Type getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getLocation() {
        return "[" + row + ", " + col + "]";
    }

    @Override
    public String toString() {
        if (this.covered && this.flagged)
            return Covered.FLAGGED.toString();
        if (this.covered && !this.flagged)
            return Covered.UNFLAGGED.toString();
        return type.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cell))
            return false;
        Cell o = (Cell) obj;
        return (
            o.getRow() == this.row &&
            o.getCol() == this.col
        );
    }

    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }

}