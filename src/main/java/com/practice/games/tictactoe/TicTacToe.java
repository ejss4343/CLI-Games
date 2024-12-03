package com.practice.games.tictactoe;
import java.util.Arrays;

import com.practice.games.boardgame.*;

public class TicTacToe implements Game {
    public static final int NUM_MOVES = 9;

    public final int[][][] winningCombinations = {
            {{0,0},{0,1},{0,2}},
            {{1,0},{1,1},{1,2}},
            {{2,0},{2,1},{2,2}},
            {{0,0},{1,0},{2,0}},
            {{0,1},{1,1},{2,1}},
            {{0,2},{1,2},{2,2}},
            {{0,0},{1,1},{2,2}},
            {{0,2},{1,1},{2,0}}
    };
    public enum Symbol { X, O }

    private final Board<String> board;
    private State state;
    private final String name;
    private Symbol activePlayer;
    private int remainingMoves;

    public TicTacToe() {
        this.board = createBoard();
        this.state = State.IN_PROGRESS;
        this.remainingMoves = NUM_MOVES;
        this.name = "tic tac toe";
        this.activePlayer = Symbol.X;
    }

    private Board<String> createBoard(){
        String[][] newBoard = new String[3][3];
        
        for (String[] col : newBoard){
            Arrays.fill(col, "-");
        }

        return new Board<>(newBoard, String.class);
    }

    public Symbol getWinner() {
        Symbol winner = null;
        for(int[][] winningPath : winningCombinations){
            String p1 = board.getCell(winningPath[0][0], winningPath[0][1]);
            String p2 = board.getCell(winningPath[1][0], winningPath[1][1]);
            String p3 = board.getCell(winningPath[2][0], winningPath[2][1]);
            if (!p1.equals("-") && p1.equals(p2) && p2.equals(p3)) {
                this.state = State.WON;
                winner =  Symbol.X.toString().equals(p3) ? Symbol.X : Symbol.O;
                String winningString = Color.format(Color.ANSI_GREEN, p3);
                board.setCell(winningString, winningPath[0][0], winningPath[0][1]);
                board.setCell(winningString, winningPath[1][0], winningPath[1][1]);
                board.setCell(winningString, winningPath[2][0], winningPath[2][1]);
            }
        }
        return winner;
    }

    public void placePiece(Symbol s, int row, int col) throws MoveConflictException {
        if (!board.getCell(row, col).equals("-"))
            throw new MoveConflictException("Space "
                    + Color.format(Color.ANSI_RED, "<" + row + "," + col + ">")
                    + " is already taken.");
        if (remainingMoves == 1)
            this.state = State.TIE;
        this.remainingMoves--;
        this.board.setCell(s.toString(), row, col);
    }

    @Override
    public void help() {
        String s = "\n<h/help> Help";
        s += "\n<q/quit> Quit";
        s += "\n<v/view> View board";
        s += "\n<i/index> Show board indices\n";
        System.out.println(s);
    }

    @Override
    public void reset() {
        this.board.reset();
        this.state = State.IN_PROGRESS;
        this.remainingMoves = NUM_MOVES;
    }

    @Override
    public void toggleIndices() {
        this.board.toggleIndices();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public void move(int row, int col) throws MoveConflictException {
        placePiece(activePlayer, row, col);
        Symbol winner = getWinner();

        switch (this.state) {
            case WON -> System.out.println(Color.format(Color.ANSI_GREEN, winner.toString()) + " has won!");
            case TIE -> System.out.println("Game Over, Tie!");
            case IN_PROGRESS -> activePlayer = activePlayer == Symbol.X ? Symbol.O : Symbol.X;
            default -> {}
        }
        System.out.println(this);
    }

    @Override
    public boolean isActive() {
        return this.state == State.IN_PROGRESS;
    }

    @Override
    public boolean handleInput(String move) {
        return false;
    }

    @Override
    public void printEnterMoveMsg() {
        System.out.println("Player " + activePlayer
                + "'s turn. Enter move: <x,y>");
    }

    @Override
    public boolean inBounds(int row, int col) {
        return this.board.isValidLocation(row, col);
    }

}