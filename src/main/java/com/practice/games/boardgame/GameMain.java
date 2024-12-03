package com.practice.games.boardgame;

import java.util.Scanner;

/**
 * Contains the logic common between running games that use 2D boards
 * that make moves which require them to specify a pair of coordinates.
 */
public class GameMain {

    /**
     * An abstract game with a 2D board
     */
    private final Game game;
    /**
     * True until the user quits the game
     */
    private boolean flag;

    public GameMain(Game game){
        this.game = game;
        this.flag = true;
    }

    private void quit() { flag = false; }

    /**
     * Uses the input from the provided scanner to a move with the game.
     * Parses input for x,y coordinates and handles errors. Leverages the
     * InputHelper class to check for commands such as "Quit" or "Help".
     * @param scanner
     */
    private void makeMove(Scanner scanner){
        String[] move = scanner.nextLine().split(",");

        if (InputHelper.isCmd(move[0], "quit"))
        {
            if (InputHelper.confirmQuit(scanner, game))
                quit();
            else {
                game.printEnterMoveMsg();
                makeMove(scanner);
            }
        }
        else if (!InputHelper.handleInput(game, move[0])) {
            try{
                int row = Integer.parseInt(move[0]);
                int col = Integer.parseInt(move[1]);
                if (move.length != 2)
                    throw new IndexOutOfBoundsException();
                if (!game.inBounds(row, col))
                    throw new IllegalArgumentException();
                this.game.move(row, col);
            }
            catch(NumberFormatException e) {
                InputHelper.printInvalidMoveFormat();
            }
            catch(IndexOutOfBoundsException | IllegalArgumentException iae) {
                InputHelper.printInvalidMoveCoordinates(move[0], move[1]);
            }
            catch(MoveConflictException mce) {
                System.out.println(mce.getMessage());
            }
        }
    }

    public void start(Scanner scanner) {

        InputHelper.printWelcomeMsg(game.getName());
        System.out.println(game);

        while(flag && game.isActive()){
            game.printEnterMoveMsg();
            makeMove(scanner);
            if (flag && !game.isActive()){
                flag = InputHelper.printPlayAgainMsg(scanner, game);
            }
        }

    }

}