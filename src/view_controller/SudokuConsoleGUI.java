package view_controller;

import java.util.ArrayList;
import java.util.Scanner;

import model.SudokuGame;

/**
 * rudimentary console implementation of a Sudoku game. zero error checking, be
 * careful !
 *
 */
public class SudokuConsoleGUI {

	private static SudokuGame game;

	public static void main(String[] args) {
		game = new SudokuGame("premade");
		game.solve();
		Scanner input = new Scanner(System.in);
		while (true) {
			game.printBoard("");
			System.out.println("enter an act: (move, delete, addcandidate, deletecandidate, showcandidate, check)");
			String cmd = input.nextLine();
			System.out.println("enter a move: row,col,val");
			int[] vals = parseInput(input.nextLine());
			switch (cmd) {
			case "move":
				addMove(vals[0], vals[1], vals[2]);
				break;
			case "delete":
				deleteMove(vals[0], vals[1]);
				break;
			case "addcandidate":
				addCandidate(vals[0], vals[1], vals[2]);
				break;
			case "deletecandidate":
				deleteCandidate(vals[0], vals[1], vals[2]);
				break;
			case "check":
				checkCorrect(vals[0], vals[1]);
				break;
			case "showcandidate":
				ArrayList<Integer> c = game.getCandidates(vals[0], vals[1]);
				System.out.println(c);
				break;
			}
			if (game.checkIfSolutionIsCorrect()) {
				return;
			}
		}
	}

	private static void addMove(int r, int c, int n) {
		game.addMove(r, c, n);
	}

	private static void deleteMove(int r, int c) {
		game.deleteMove(r, c);
	}

	private static void addCandidate(int r, int c, int n) {
		game.addCandidate(r, c, n);
	}

	private static void deleteCandidate(int r, int c, int n) {
		return;
//		game.deleteCandidate(r, c, n);
	}

	private static void checkCorrect(int r, int c) {
		if (game.cellIsValid(r, c, game.getCell(r, c).getHeldValue())) {
			System.out.println("move is possible");
		} else {
			System.out.println("move is not possible");
		}
	}

	private static int[] parseInput(String inp) {
		String[] s = inp.split(",");
		int[] ret = new int[s.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer.parseInt(s[i]);
		}
		return ret;
	}

}
