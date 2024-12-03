package com.practice.games.boardgame;

import java.util.Scanner;

/**
 * A helper class for handling user input through provided Strings or Scanners.
 */
public class InputHelper {

    public static boolean isCmd(String input, String cmd){
        return isCmd(input, cmd, true);
    }

    /**
     * Checks if the given Strings match. Case-insensitive.
     * @param input
     * @param cmd Game commands such as "Quit", "Help", "View Board"
     * @param includeFirstLetter If true, Returns true if the first letter of the command matches user input.
     * @return
     */
    public static boolean isCmd(String input, String cmd, boolean includeFirstLetter){
        input = input.trim().toLowerCase();
        cmd = cmd.trim().toLowerCase();
        if (includeFirstLetter)
            return input.equals(cmd) || input.equals(cmd.substring(0, 1));
        return input.equals(cmd);
    }

    public static boolean confirmQuit(Scanner scanner, Game game){
        System.out.println("Are you sure? (y/n)");
        if (InputHelper.confirm(scanner, "leave the game.")){
            System.out.println("Goodbye!");
            return true;
        }
        System.out.println("The game continues...\n" + game);
        return false;
    }

    public static boolean confirm(Scanner scanner, String action){
        String input = scanner.nextLine().trim().toLowerCase();
        if (isCmd(input, "yes"))
            return true;
        if (isCmd(input, "no"))
            return false;
        System.out.println("Invalid answer. Please chose (y/n) to " + action);
        return confirm(scanner, action);
    }

    public static void printWelcomeMsg(String name) {
        System.out.println("Welcome to " + name + "!");
    }

    public static boolean printPlayAgainMsg(Scanner scanner, Game game) {
        System.out.println("Play again? (y/n)");
        if (InputHelper.confirm(scanner, "play again.")) {
            game.reset();
            System.out.println(game);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Handles commands common between games. If no match is found, passes the
     * input to the game to check for game specific commands.
     * @param game
     * @param move
     * @return
     */
    public static boolean handleInput(Game game, String move) {
        if (isCmd(move, "view")){
            System.out.println(game);
            return true;
        }
        else if (isCmd(move, "help")){
            game.help();
            return true;
        }   
        else if (isCmd(move, "index")){
            game.toggleIndices();
            System.out.println(game);
            return true;
        }
        return game.handleInput(move);
    }

    public static void printInvalidMoveFormat() {
        System.out.println("Invalid move format. Please enter position separated by commas, eg. "
                + Color.format(Color.ANSI_GREEN, "1,2"));
    }

    public static void printInvalidMoveCoordinates(String x, String y){
        System.out.println("Invalid coordinates. "
                + Color.format(Color.ANSI_RED, "<" + x + "," + y + ">") + " out of bounds.");
    }

    /**
     * Prompts users for board dimensions.
     * @param scanner
     * @return
     */
    public static int[] getBoardDimensions(Scanner scanner){
        System.out.println("Enter board dimensions: e.g. " + Color.format(Color.ANSI_GREEN, "5,5"));
        String input = scanner.nextLine();

        try{
            String[] dims = input.split(",");
            int[] dimensions = new int[]{Integer.parseInt(dims[0]), Integer.parseInt(dims[1])};
            if (!Board.validDimensions(dimensions[0], dimensions[1]))
                throw new InvalidDimensionsExecption("Boards have a max length of " + Board.MAX_LENGTH
                    + " and a min length of " + Board.MIN_LENGTH);
            return dimensions;
        }
        catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Invalid dimensions, please enter board dimensions in the form: "
                    + Color.format(Color.ANSI_GREEN, "1,2"));
        }
        catch(InvalidDimensionsExecption e) {
            System.out.println(e.getMessage());
        }

        return getBoardDimensions(scanner);
    }

}