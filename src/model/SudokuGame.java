package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Object that handles playing a game of Sudoku. TODO: method of updating user
 * win streak; clean up the duplicate methods of checking valid cells,
 * serialization of random boards to save some time
 */

public class SudokuGame extends OurObservable {

	private ArrayList<ArrayList<SudokuCell>> currentBoard;
	private ArrayList<ArrayList<SudokuCell>> solution;
//	private User currentUser;
	private Stack<Action> stack;

	/**
	 * Constructs a new Sudoku game.
	 * 
	 * @param difficulty Determines what type of board is generated.
	 */
	public SudokuGame(String difficulty) {
		currentBoard = new ArrayList<>();
		stack = new Stack<>();
//		currentUser = getUser();
		generateBoard();
		generateCells(difficulty);
		copyInitialBoard();
		solve(); // causing a little error before gui launch
	}

	/*
	 * Add a move to the sudoku game.
	 * @param row
	 * @param col
	 * @param n number to add
	 */
	public void addMove(int row, int col, int n) {
		stack.add(0, new AddToCell(getCell(row, col), true, n));
		stack.get(0).action();
		System.out.println(stack.size());
		this.notifyObservers(this);
	}

	/*
	 * Delete held value of a cell
	 * @param row
	 * @param col
	 */
	public void deleteMove(int row, int col) {
		stack.add(0, new EraseFromCell(getCell(row, col), true));
		stack.get(0).action();
		System.out.println(stack.size());
		this.notifyObservers(this);
	}

	/*
	 * add a candidate value to a cell
	 * @param row
	 * @param col
	 * @param n the candidate value to add
	 */
	public void addCandidate(int row, int col, int n) {
		stack.add(0, new AddToCell(getCell(row, col), false, n));
		stack.get(0).action();
		System.out.println(stack.size());
		this.notifyObservers(this);
	}
	
	/*
	 * erase a value from a cell
	 * @param row
	 * @param col
	 */
	public void deleteCandidates(int row, int col) {
		stack.add(0, new EraseFromCell(getCell(row, col), false));
		stack.get(0).action();
		System.out.println(stack.size());
		this.notifyObservers(this);
	}

	public ArrayList<Integer> getCandidates(int row, int col) {
		return currentBoard.get(row).get(col).getCandidateValues();
	}

	public SudokuCell getCell(int row, int col) {
		return currentBoard.get(row).get(col);
	}

	public ArrayList<ArrayList<SudokuCell>> getBoard() {
		return currentBoard;
	}

	/**
	 * Public method to call the recursive algorithm.
	 * 
	 */
	public void solve() {
		recursiveSolution();
	}

		/**
		 * Compares the current state of the board with the solution board. If there are
		 * any mismatches or any unfilled (0) cells, the user has not won.
		 * 
		 * @return True if the current board matches the solution board.
		 */
		public boolean checkIfSolutionIsCorrect() {
			for (int row = 0; row < currentBoard.size(); row++) {
				for (int col = 0; col < currentBoard.get(row).size(); col++) {
					SudokuCell currentCell = currentBoard.get(row).get(col);
					if (currentCell.getHeldValue() != solution.get(row).get(col).getHeldValue()
							|| currentCell.getHeldValue() == 0) {
						return false;
					}
				}
			}
			return true;
		}

	/**
	 * Creates a 9x9 2D ArrayList of empty SudokuCell objects.
	 */

	private void generateBoard() {
		for (int rows = 0; rows < 9; rows++) {
			ArrayList<SudokuCell> row = new ArrayList<>();
			for (int cols = 0; cols < 9; cols++) {
				row.add(new SudokuCell());
			}
			currentBoard.add(row);
		}
	}

	/**
	 * Copies the initial state of the game to a new 2D ArrayList. This list is used
	 * to create and store the unique solution to the current game.
	 */
	private void copyInitialBoard() {
		solution = new ArrayList<>();
		for (int r = 0; r < currentBoard.size(); r++) {
			ArrayList<SudokuCell> row = new ArrayList<>();
			for (int c = 0; c < currentBoard.get(r).size(); c++) {
				int valToAdd = currentBoard.get(r).get(c).getHeldValue();
				row.add(new SudokuCell().setHeldValue(valToAdd));
			}
			solution.add(row);
		}
	}

	/**
	 * Generates the cells of a blank Sudoku board. Defaults to a premade board if
	 * input doesn't fit.
	 * 
	 * @param difficulty Changes what board is created.
	 */
	private void generateCells(String difficulty) {
		String d = difficulty.toUpperCase();
		if (!d.equals("EASY") && !d.equals(("MEDIUM")) && !d.equals("HARD")) {
			generateCellsPremade();
			return;
		} else {
			generateCellsRandom(difficulty);
		}
	}

	/**
	 * Creates a random board based on the given difficulty.
	 * 
	 * @param difficulty
	 */
	private void generateCellsRandom(String difficulty) {
		RandomBoardGenerator gen = new RandomBoardGenerator(difficulty);
		int[][] randomBoard = gen.getBoard();
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int n = randomBoard[r][c];
				if (n != 0) {
					currentBoard.get(r).set(c, new SudokuCell(n));
				}
			}
		}
	}

	/**
	 * Prints the board. If specified in the parameter, prints the solution instead.
	 * 
	 * @param If "solution" is passed, prints the current solution board.
	 */
	public void printBoard(String s) {
		ArrayList<ArrayList<SudokuCell>> board;
		if (s.equals("solution")) {
			board = solution;
		} else {
			board = currentBoard;
		}

		System.out.print("----------------------------\n");
		int j = 0;
		for (ArrayList<SudokuCell> row : board) {
			j++;
			int i = 0;
			System.out.print("| ");
			for (SudokuCell col : row) {
				i++;
				if (col.getHeldValue() != 0) {
					System.out.print(col.getHeldValue() + " ");
				} else {
					System.out.print("  ");
				}
				if (i % 3 == 0) {
					System.out.print(" | ");
				}
			}
			System.out.println();
			if (j % 3 == 0) {
				System.out.print("----------------------------\n");
			}
		}
	}

	/**
	 * generates a preset board for testing purposes. massive block because it
	 * manually sets up every single cell. this specific board is:
	 * https://sandiway.arizona.edu/sudoku/wildcatjan17p.gif
	 * https://sandiway.arizona.edu/sudoku/wildcatjan17.gif
	 * 
	 * @param testing True if testing.
	 */
	private void generateCellsPremade() {

		currentBoard.get(1).set(0, new SudokuCell(6));
		currentBoard.get(1).set(1, new SudokuCell(8));
		currentBoard.get(2).set(0, new SudokuCell(1));
		currentBoard.get(2).set(1, new SudokuCell(9));

		currentBoard.get(0).set(3, new SudokuCell(2));
		currentBoard.get(0).set(4, new SudokuCell(6));
		currentBoard.get(1).set(4, new SudokuCell(7));
		currentBoard.get(2).set(5, new SudokuCell(4));

		currentBoard.get(0).set(6, new SudokuCell(7));
		currentBoard.get(0).set(8, new SudokuCell(1));
		currentBoard.get(1).set(7, new SudokuCell(9));
		currentBoard.get(2).set(6, new SudokuCell(5));

		currentBoard.get(3).set(0, new SudokuCell(8));
		currentBoard.get(3).set(1, new SudokuCell(2));
		currentBoard.get(4).set(2, new SudokuCell(4));
		currentBoard.get(5).set(1, new SudokuCell(5));

		currentBoard.get(3).set(3, new SudokuCell(1));
		currentBoard.get(4).set(3, new SudokuCell(6));
		currentBoard.get(4).set(5, new SudokuCell(2));
		currentBoard.get(5).set(5, new SudokuCell(3));

		currentBoard.get(3).set(7, new SudokuCell(4));
		currentBoard.get(4).set(6, new SudokuCell(9));
		currentBoard.get(5).set(7, new SudokuCell(2));
		currentBoard.get(5).set(8, new SudokuCell(8));

		currentBoard.get(6).set(2, new SudokuCell(9));
		currentBoard.get(7).set(1, new SudokuCell(4));
		currentBoard.get(8).set(0, new SudokuCell(7));
		currentBoard.get(8).set(2, new SudokuCell(3));

		currentBoard.get(6).set(3, new SudokuCell(3));
		currentBoard.get(7).set(4, new SudokuCell(5));
		currentBoard.get(8).set(4, new SudokuCell(1));
		currentBoard.get(8).set(5, new SudokuCell(8));

		currentBoard.get(6).set(7, new SudokuCell(7));
		currentBoard.get(6).set(8, new SudokuCell(4));
		currentBoard.get(7).set(7, new SudokuCell(3));
		currentBoard.get(7).set(8, new SudokuCell(6));

	}

//	private User getUser() {
//		
//	}

	/**
	 * checks to see if a specific cell is valid. Used to check specifically on the
	 * solution 2D ArrayList object.
	 * 
	 * @param checkSolution True if method is being used for the recursive
	 *                      backtracking algorithm. False if it's being used to
	 *                      check user input.
	 * @param row           Row being checked
	 * @param col           Column being checked
	 * @param n             Specific held value of the cell being checked
	 * @return True if the cell is valid, otherwise false
	 */
	private boolean cellIsValidForRecursion(ArrayList<ArrayList<SudokuCell>> board, int row, int col, int n) {
		for (int x = 0; x < board.get(row).size(); x++) {
			if (board.get(row).get(x).getHeldValue() == n) {
				return false;
			}
		}

		for (int y = 0; y < board.get(col).size(); y++) {
			if (board.get(y).get(col).getHeldValue() == n) {
				return false;
			}
		}

		int row0 = row - row % 3;
		int col0 = col - col % 3;
		for (int i = row0; i < row0 + 3; i++) {
			for (int j = col0; j < col0 + 3; j++) {
				if (board.get(i).get(j).getHeldValue() == n) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Same as the other cellIsValid method, but with extra conditionals to "skip
	 * over" the player move. The way move checking works could be reworked, but
	 * this works for now.
	 * 
	 * @param row The row of the cell being checked.
	 * @param col The col of the cell being checked.
	 * @param val The value of the cell being checked.
	 * @return True if it's a valid move.
	 */
	public boolean cellIsValid(int row, int col, int val) {
		for (int x = 0; x < currentBoard.get(row).size(); x++) {
			if (x != col) {
				if (currentBoard.get(row).get(x).getHeldValue() == val) {
					return false;
				}
			}
		}

		for (int y = 0; y < currentBoard.get(col).size(); y++) {
			if (y != row) {
				if (currentBoard.get(y).get(col).getHeldValue() == val) {
					return false;
				}
			}
		}

		int row0 = row - row % 3;
		int col0 = col - col % 3;
		for (int i = row0; i < row0 + 3; i++) {
			for (int j = col0; j < col0 + 3; j++) {
				if (i != row || j != col) {
					if (currentBoard.get(i).get(j).getHeldValue() == val) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * Checking to see if the value to be added is the same as the solution.
	 */
	public boolean cellIsCorrect(int row, int column, int value) {
		return solution.get(row).get(column).getHeldValue() == value;
	}

	/**
	 * Solves the current Sudoku game recursively. This specific algorithm is based
	 * off the following resources: https://www.baeldung.com/java-sudoku,
	 * https://www.heimetli.ch/ffh/simplifiedsudoku.html
	 * 
	 * @return
	 */
	private boolean recursiveSolution() {
		for (int row = 0; row < solution.size(); row++) {
			for (int col = 0; col < solution.get(row).size(); col++) {
				if (solution.get(row).get(col).getHeldValue() == 0) {
					for (int n = 1; n <= 9; n++) {
						if (cellIsValidForRecursion(solution, row, col, n)) {
							solution.get(row).get(col).setHeldValue(n);
							if (recursiveSolution()) {
								return true;
							} else {
								solution.get(row).get(col).removeValue();
							}

						}
					}
					return false;
				}
			}
		}

		return true;
	}
	
	/*
	 * undo a move whether it be add value or remove a value
	 */
	public void undoMove() {
		if (!stack.isEmpty()) {
			stack.remove(0).undo();
			this.notifyObservers(this);
		}
		System.out.println(stack.size());
	}
	
	/*
	 * reset the game to its original position
	 */
	public void resetGame() {
		while (!stack.isEmpty()) {
			stack.remove(0).undo();
		}
		this.notifyObservers(this);
	}

	/*
	 * show correct value of a cell
	 * @param row
	 * @param column
	 */
	public void showCorrectCell(int row, int column) {
		currentBoard.get(row).get(column).setHeldValue(solution.get(row).get(column).getHeldValue());
		this.notifyObservers(this);
	}

	/*
	 * reveal board
	 */
	public void revealBoard() {
		for (int row = 0; row < solution.size(); row++) {
			for (int column = 0; column < solution.size(); column++) {
				currentBoard.get(row).get(column).setHeldValue(solution.get(row).get(column).getHeldValue());
			}
			this.notifyObservers(this);
		}
	}
}
