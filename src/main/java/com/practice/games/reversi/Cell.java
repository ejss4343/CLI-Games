package com.practice.games.reversi;

import com.practice.games.boardgame.Color;

public class Cell {

    public static final int[][] DELTAS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
            {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };

    private int col;
    private int row;
    private boolean isCovered;
    private Color color;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.isCovered = false;
        this.color = Color.ANSI_RESET;
    }

    @Override
    public String toString() {
        return isCovered ? Color.format(color, Symbol.O.toString()) : Symbol.BLANK.toString();
    }

    public void cover(Color color) {
        this.isCovered = true;
        this.color = color;
    }

    public Color getColor() {
        return color;
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

    public boolean isCovered() {
        return isCovered;
    }

    @Override
    public int hashCode() {
        return this.getLocation().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell){
            Cell other = (Cell)obj;
            return other.getRow() == this.row
                    && other.getCol() == this.col;
        }
        return false;
    }

}