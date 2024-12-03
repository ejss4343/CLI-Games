package com.practice.games.boardgame;

import java.lang.reflect.Array;

public class Board<T> {

    public static final int MAX_LENGTH = 50;
    public static final int MIN_LENGTH = 3;
    private final T[][] defaultBoard;
    private T[][] board;
    private final Class<? extends T> cls;
    private boolean showIndices;

    public Board(T[][] board, Class<? extends T> cls) {
        this.cls = cls;
        this.board = board;
        this.defaultBoard = getDeepCopy(board);
        this.showIndices = false;
    }

    public T[][] getDeepCopy(T[][] original) {
        int numRows = original.length;
        int numCols = original[0].length;
        @SuppressWarnings("unchecked")
        T[][] copy = (T[][])Array.newInstance(cls, numRows, numCols);
        for (int i = 0; i < numRows; i++){
            System.arraycopy(original[i], 0, copy[i], 0, numCols);
        }
        return copy;
    }

    public boolean isValidLocation(int row, int col){
        int maxRow = this.getRowCount();
        int maxCol = this.getColCount();
        return !(row > maxRow || row < 0 || col > maxCol || col < 0);
    }

    public void setCell(T obj, int row, int col){
        board[row][col] = obj;
    }

    public T getCell(int row, int col){
        return board[row][col];
    }

    public int getRowCount() {
        return this.board.length;
    }

    public int getColCount() {
        return this.board[0].length;
    }

    public void reset() {
        this.board = getDeepCopy(defaultBoard);
    }

    public void toggleIndices() {
        this.showIndices = !showIndices;
    }

    public static boolean validDimensions(int row, int col){
        return (row <= MAX_LENGTH && row >= MIN_LENGTH
                && col <= MAX_LENGTH && col >= MIN_LENGTH);
    }
    
    @Override
    public String toString() {
        if (showIndices)
            return toStringWIndices();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < getColCount(); j++){
                s.append(" ").append(board[i][j].toString()).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public String toStringWIndices() {
        StringBuilder s = new StringBuilder();

        s.append("\u0020\u0020\u0020");

        for (int row = 0; row < getRowCount(); row++){
            if (row == 0) {
                for (int col = 0; col < getColCount(); col++){
                    if(col < 10)
                        s.append(Color.format(Color.ANSI_CYAN," " + col + " "));
                    else
                        s.append(Color.format(Color.ANSI_CYAN," " + col));
                }
                s.append("\n");
            }
            for (int col = 0; col < getColCount(); col++){
                if (col == 0) {
                    if(row < 10)
                        s.append(Color.format(Color.ANSI_CYAN," " + row + " "));
                    else
                        s.append(Color.format(Color.ANSI_CYAN," " + row));
                }
                s.append(" ").append(board[row][col].toString()).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

}
