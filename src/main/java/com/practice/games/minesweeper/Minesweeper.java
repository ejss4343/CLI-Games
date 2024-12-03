package com.practice.games.minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.practice.games.boardgame.*;
import com.practice.games.minesweeper.Cell.Type;

import com.practice.games.boardgame.Color;

public class Minesweeper implements Game {
    public enum Difficulty {
        EASY(7), MEDIUM(4), HARD(2);

        private final int chance;
        Difficulty(int chance){
            this.chance = chance;
        }
        public static Difficulty fromValue(String value) {
            for (Difficulty d : Difficulty.values()) {
                if (d.toString().equals(value.toUpperCase())) {
                    return d;
                }
            }
            return null;
        }
    }

    public enum State {
        IN_PROGRESS("In progress"), WON("Won"), LOST("Lost");
        private final String name;
        State(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return this.name;
        }

    }

    private final Board<Cell> board;
    private final String name;
    private int numMines;
    private State state;
    private int remainingMoves;
    private final Difficulty difficulty;
    private boolean isFlagging;
    private Set<Cell> cells;

    public Minesweeper(Scanner scanner){
        this.name = "Minesweeper";
        int[] dims = InputHelper.getBoardDimensions(scanner);
        this.board = new Board<>(new Cell[dims[0]][dims[1]], Cell.class);
        this.difficulty = selectDifficulty(scanner);
        this.start(dims[0], dims[1]);
    }

    private void start(int rowCount, int colCount) {
        this.state = State.IN_PROGRESS;
        int numOfCells =  rowCount * colCount;
        this.numMines = numOfCells / difficulty.chance;
        this.remainingMoves = numOfCells - numMines;
        this.cells = new HashSet<>();
        this.setMines(rowCount, colCount);
        this.fillBoard(rowCount, colCount);
        this.isFlagging = false;
    }

    @Override
    public void reset() {
        this.board.reset();
        this.start(board.getColCount(), board.getColCount());
    }

    private void setMines(int rowCount, int colCount) {
        Random rand = new Random();
        while( cells.size() < numMines ){
            int row = rand.nextInt(rowCount);
            int col = rand.nextInt(colCount);
            Cell cell = new Cell(row, col, Cell.Type.MINE);
            if(cells.add(cell)){
                board.setCell(cell, row, col);
            }
        }
    }

    private void fillBoard(int rowCount, int colCount){
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if(board.getCell(i, j) == null){
                    setCellMineCount(i, j);
                }
            }
        }
    }

    private void setCellMineCount(int row, int col){
        int mineCount = 0;

        for (int[] delta : Cell.DELTAS){
            int[] neighbor = {(row + delta[0]), (col + delta[1])};
            if (validCord(neighbor[0], neighbor[1])
                && board.getCell(neighbor[0], neighbor[1]) != null
                && board.getCell(neighbor[0], neighbor[1]).getType() == Type.MINE){
                mineCount++;
            }
        }
        Cell cell = new Cell(row, col, Type.fromValue(mineCount));
        cells.add(cell);
        this.board.setCell(cell, row, col);
    }

    private void flipAdjacent(Cell cell){
        for (int[] delta : Cell.DELTAS){
            int[] neighbor = {(cell.getRow() + delta[0]), (cell.getCol() + delta[1])};
            if (validCord(neighbor[0], neighbor[1])){
                Cell adjCell = board.getCell(neighbor[0], neighbor[1]);
                if (adjCell.getType() == Type.ZERO && adjCell.isCovered()) {
                    adjCell.flip();
                    remainingMoves--;
                    flipAdjacent(adjCell);
                }
            }
        }
    }

    private boolean validCord(int row, int col){
        return (row < board.getRowCount() && row >= 0
                && col < board.getColCount() && col >= 0);
    }

    private void revealBoard(){
        for(int i = 0; i < board.getRowCount(); i++){
            for(int j = 0; j < board.getColCount(); j++){
                Cell cell = board.getCell(i,j);
                if(cell.isCovered())
                    cell.flip();
            }
        }
    }

    private Difficulty selectDifficulty(Scanner scanner) {
        String s= "Enter a difficulty:\n";
        s += "\tEasy\n";
        s += "\tMedium\n";
        s += "\tHard\n";
        System.out.println(s);
        String input = scanner.nextLine().trim().toUpperCase();

        Difficulty d = Difficulty.fromValue(input);
        return d != null ? d : selectDifficulty(scanner);
    }

    @Override
    public void move(int row, int col) throws MoveConflictException {
        Cell cell = this.board.getCell(row, col);
        if (!cell.isCovered())
            throw new MoveConflictException("Space "
                    + Color.format(Color.ANSI_RED, "<" + row + "," + col + ">")
                    + " has already been uncovered.");
        if(this.isFlagging)
            cell.flag();
        else
            uncover(cell);
        System.out.println(this);
    }

    private void uncover(Cell cell){
        if(cell.getType() == Type.MINE){
            this.state = State.LOST;
            this.remainingMoves = 0;
            revealBoard();
            System.out.println("Game over! Mine at: "
                    + Color.format(Color.ANSI_RED,
                    "<" + cell.getRow() + "," + cell.getCol() + ">"));
        }
        else {
            cell.flip();
            remainingMoves--;

            if(cell.getType() == Type.ZERO){
                flipAdjacent(cell);
            }
        }

        if(this.remainingMoves == 0 && this.state == State.IN_PROGRESS){
            this.state = State.WON;
            revealBoard();
            System.out.println(Color.format(Color.ANSI_GREEN,
                    "Congratulations you won!"));
        }
        System.out.println(Color.format(Color.ANSI_GREEN, String.valueOf(remainingMoves))
                + " remaining moves!");
    }

    private String getHint(){
        StringBuilder sb = new StringBuilder();
        Cell cell = cells.stream().filter(c -> c.isCovered() && !c.isMine()).findFirst().orElse(null);
        sb.append(
          cell == null ? "All safe cells uncovered, no hint available.\n"
            : "Safe cell located at " + Color.format(Color.ANSI_BLUE,
                  "<" + cell.getRow() + "," + cell.getCol() + ">") + "\n"
        );

        return sb.toString();
    }

    @Override
    public void printEnterMoveMsg() {
        System.out.println("# of Mines: "
                + Color.format(Color.ANSI_RED, String.valueOf(numMines)));
        System.out.println("Enter move: <x,y>");
    }

    @Override
    public boolean isActive() {
        return this.state == State.IN_PROGRESS;
    }

    @Override
    public boolean handleInput(String move) {
        if (InputHelper.isCmd(move, "Flag")){
            this.isFlagging = !this.isFlagging;
            System.out.println(
                isFlagging ? "Entered flagging mode! Use "
                        + Color.format(Color.ANSI_GREEN, "<flag>")
                        + " again to leave."
                        : Color.format(Color.ANSI_GREEN, "Exited flagging mode!")
            );
            System.out.println(this);
            return true;
        }
        if (InputHelper.isCmd(move, "Hint", false) || InputHelper.isCmd(move, "G")) {
            System.out.println(getHint());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if(this.isFlagging)
            System.out.println(Color.format(Color.ANSI_RED, "Flagging Mode Active"));
        return this.board.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void help() {
        String s = "\n<h/help> Help";
        s += "\n<q/quit> Quit";
        s += "\n<v/view> View board";
        s += "\n<i/index> Show board indices";
        s += "\n<g/hint> Get hint";
        s += "\n<f/flag> Enter flagging mode\n";
        System.out.println(s);
    }

    @Override
    public void toggleIndices() {
        this.board.toggleIndices();
    }

    @Override
    public boolean inBounds(int row, int col) {
        return this.board.isValidLocation(row, col);
    }

}