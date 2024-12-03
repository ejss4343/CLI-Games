package com.practice.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.practice.games.minesweeper.Minesweeper;
import com.practice.games.reversi.Reversi;
import com.practice.games.tictactoe.TicTacToe;
import com.practice.games.boardgame.GameMain;
import com.practice.games.boardgame.Color;
import com.practice.games.boardgame.InputHelper;
import java.util.Scanner;

/**
 * The main screen logic for selecting a game.
 * There are currently 3 options, Tic tac toe,
 * Minesweeper and Reversi.
 */
@SpringBootApplication
public class GamesApplication {

	private static GameMain game;
	private static boolean flag = true;

	private static void printGameOptions() {
		System.out.println("Chose your game!");
		String s = "<" + Color.format(Color.ANSI_CYAN, "1") + "> TicTacToe\n";
		s += "<" + Color.format(Color.ANSI_GREEN, "2") + "> Minesweeper\n";
		s += "<" + Color.format(Color.ANSI_YELLOW, "3") + "> Reversi\n";
		s += "<" + Color.format(Color.ANSI_RED, "4") + "> Quit";
		System.out.println(s);
	}

	/**
	 * Prompts the user to choose a game with the provided scanner.
	 * @param scanner provided scanner.
	 */
	private static void chooseOption(Scanner scanner){
		String input = scanner.nextLine();
		input = input.trim().toLowerCase();
		switch (input) {
			case "1" -> game = new GameMain(new TicTacToe());
			case "2" -> game = new GameMain(new Minesweeper(scanner));
			case "3" -> game = new GameMain(new Reversi(scanner));
			case "4" -> {
				System.out.println("Are you sure? (y/n)");
				if(InputHelper.confirm(scanner, "quit")) {
					flag = false;
					System.out.println("Goodbye!");
				}
				else
					printGameOptions();
			}
			default -> {
				System.out.println("Invalid input, please try again.");
				chooseOption(scanner);
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(GamesApplication.class, args);

		Scanner scanner = new Scanner(System.in);
		flag = true;

		while(flag){
			printGameOptions();
			chooseOption(scanner);
			if(game != null && flag){
				game.start(scanner);
			}
		}

		scanner.close();
	}

}
