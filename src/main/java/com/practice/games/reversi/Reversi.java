package com.practice.games.reversi;

import com.practice.games.boardgame.*;

import java.rmi.NoSuchObjectException;
import java.util.*;

public class Reversi implements Game {
    private static final int[][] MIDDLE_PIECES = {
            {3,3},{4,3},
            {3,4},{4,4}
    };
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;
    private final Board<Cell> board;
    private final String name;
    private final Scanner scanner;
    private State state;
    private Color activePlayer;
    private Color playerOne;
    private Color playerTwo;

    public Reversi(Scanner scanner) {
        this.board = new Board<>(new Cell[NUM_ROWS][NUM_COLS], Cell.class);
        this.name = "Reversi";
        this.scanner = scanner;
        this.start();
    }

    private void start() {
        this.state = State.IN_PROGRESS;
        this.getPlayers();
        System.out.println("\nPlayer One selected: " + Color.format(playerOne, Symbol.O.name()));
        System.out.println("Player Two selected: " + Color.format(playerTwo, Symbol.O.name()));
        this.activePlayer = playerOne;
        this.fillBoard();
        // System.out.println("{ " + board.getCell(3,3) + " }");
        getValidMovesForPosition(3,3).stream().forEach( r ->
        {
            System.out.println("*" + Arrays.toString(r) + "*");
        });
    }

    @Override
    public void reset() {
        this.board.reset();
        this.start();
    }

    private void fillBoard() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Cell cell = new Cell(i, j);
                board.setCell(cell, i , j);
            }
        }
        board.getCell(MIDDLE_PIECES[0][0], MIDDLE_PIECES[0][1]).cover(playerOne);
        board.getCell(MIDDLE_PIECES[1][0], MIDDLE_PIECES[1][1]).cover(playerTwo);
        board.getCell(MIDDLE_PIECES[2][0], MIDDLE_PIECES[2][1]).cover(playerTwo);
        board.getCell(MIDDLE_PIECES[3][0], MIDDLE_PIECES[3][1]).cover(playerOne);
    }

    private void getPlayers() {
        System.out.println("\nSelect your color with <name>");
        System.out.print(
                playerOne == null ? "Player one: "
                        : "Player two: "
        );
        String input = scanner.nextLine();

        if (InputHelper.isCmd(input, "Help")) {
            String s = "\nSelect your color with <name>\n";
            s += "<c/colors> View Colors\n";
            s += "<h/help> Help\n";
            System.out.println(s);
            getPlayers();
        }
        else if (InputHelper.isCmd(input, "Colors")) {
            displayColors();
            System.out.println();
            getPlayers();
        }
        else {
            try{
                Color color = Color.fromString(input);
                if (color == Color.ANSI_RESET){
                    System.out.println("Invalid color.");
                    getPlayers();
                }
                else if (playerOne == null){
                    playerOne = color;
                    getPlayers();
                }
                else if(color != playerOne)
                    playerTwo = color;
                else{
                    System.out.println(color + " already taken! Players must choose unique colors.");
                    getPlayers();
                }
            }
            catch (NoSuchObjectException e ){
                System.out.println(e.getMessage());
                getPlayers();
            }
        }
    }

    private void displayColors() {
        System.out.println("Colors:");
        for(Color c : Color.values()){
            if (c != Color.ANSI_RESET)
                System.out.println("\t" + c);
        }
    }

    private void showMoves() {
        System.out.println("MOVES");
    }

    @Override
    public void move(int row, int col) throws MoveConflictException {
        Cell cell = this.board.getCell(row, col);
        if (cell.isCovered())
            throw new MoveConflictException("Space "
                    + Color.format(Color.ANSI_RED, "<" + row + "," + col + ">")
                    + " has already been occupied by " + cell + ".");

        // Switch players
        this.activePlayer = getOpponent(activePlayer);

    }

    private boolean isValidMove(int row, int col){
        return (isValidLocation(row, col)
                    && !getAdjacentOpponentPieces(row, col).isEmpty()
        );
    }

    private Set<int[]> getValidMovesForPosition(int row, int col) {
        Set<int[]> moves = new HashSet<>();

        if (!isValidLocation(row, col)) // Invalid position
            return moves;

        Set<int[]> adjacents = null;
        // Set<int[]> adjacents = getAdjacentOpponentPieces(row, col);
        // getMoves(adjacents, moves);

        return adjacents;
    }

    private void getMoves(Map<Cell, int[]> adjacents, Set<int[]> moves){

        for(Cell cell : adjacents.keySet()){
            // System.out.println(Arrays.toString(adjacent) + "*");
            for(int[] delta : Cell.DELTAS) {
                int row = cell.getRow() + delta[0];
                int col = cell.getCol() + delta[1];
                if (isValidLocation(row, col)){
                    Cell adjacent = board.getCell(row, col);

                    if (adjacent.getColor().equals(getOpponent(activePlayer))){
                        getMoves(adjacents, moves);
                    }
                    else if (cell.getColor().equals(Color.ANSI_RESET)) {
                        int[] location = {cell.getRow(), cell.getCol()};
                        moves.add(location);
                    }
                }
            }
        }
    }

    private Map<Cell, int[]> getAdjacentOpponentPieces(int row, int col){
        Map<Cell, int[]> adjacents = new HashMap<>();

        // System.out.println("{ " + getOpponent(activePlayer) + " }");

        for(int[] delta : Cell.DELTAS) {
            int adjRow = row + delta[0];
            int adjCol = col + delta[1];

            System.out.println("{ " + adjRow + ", " + adjCol + " }");

            if (isValidLocation(adjRow, adjCol)){
                Cell cell = board.getCell(adjRow, adjCol);
                if (cell.getColor().equals(getOpponent(activePlayer))){
                    int[] location = {cell.getRow(), cell.getCol()};
                    adjacents.put(cell, location);
                }
            }
        }

        return adjacents;
    }

    private boolean isValidLocation(int row, int col){
        return !(row > NUM_ROWS || row < 0 || col > NUM_COLS || col < 0);
    }

    private Color getOpponent(Color player){
        return player.equals(playerOne) ? playerTwo : playerOne;
    }

    @Override
    public void help() {
        String s = "\n<h/help> Help";
        s += "\n<q/quit> Quit";
        s += "\n<v/view> View board";
        s += "\n<i/index> Show board indices";
        s += "\n<p/pass> Pass your turn";
        s += "\n<m/moves> Highlight possible moves";
        System.out.println(s);
    }

    @Override
    public String toString() {
        return this.board.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void toggleIndices() {
        this.board.toggleIndices();
    }

    @Override
    public void printEnterMoveMsg() {
        System.out.println(activePlayer
                + "'s turn. Enter move: <x,y>");
    }

    @Override
    public boolean isActive() {
        return this.state == State.IN_PROGRESS;
    }

    @Override
    public boolean handleInput(String move) {
        if (InputHelper.isCmd(move, "Pass")){
            System.out.print(activePlayer + "passed! ");
            activePlayer = activePlayer == playerOne ? playerTwo : playerOne;
            System.out.println(activePlayer + "'s turn.");
            return true;
        }
        if (InputHelper.isCmd(move, "Moves")) {
            showMoves();
            return true;
        }
        return false;
    }

    @Override
    public boolean inBounds(int row, int col) {
        return this.board.isValidLocation(row, col);
    }

}