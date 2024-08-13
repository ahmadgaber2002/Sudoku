package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pseudorandom Sudoku board generation, methodology mostly taken from user
 * Manish Kumar at:
 * https://stackoverflow.com/questions/6924216/how-to-generate-sudoku-boards-with-unique-solutions
 * Takes a premade, trivial Sudoku board and randomizes it.
 */
public class RandomBoardGenerator {

	private int board[][];
	private int boardSol[][];

	/**
	 * Constructs the premade board, randomizes it.
	 * 
	 * @param difficulty Defines how many cells will be removed. Allegedly, this is
	 *                   a poor indication of Sudoku difficulty, but it works for
	 *                   our purposes.
	 */
	public RandomBoardGenerator(String difficulty) {
		board = new int[][] { 
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 
			{ 4, 5, 6, 7, 8, 9, 1, 2, 3 },
			{ 7, 8, 9, 1, 2, 3, 4, 5, 6 }, 
			{ 2, 3, 1, 5, 6, 4, 8, 9, 7 }, 
			{ 5, 6, 4, 8, 9, 7, 2, 3, 1 },
			{ 8, 9, 7, 2, 3, 1, 5, 6, 4 }, 
			{ 3, 1, 2, 6, 4, 5, 9, 7, 8 }, 
			{ 6, 4, 5, 9, 7, 8, 3, 1, 2 },
			{ 9, 7, 8, 3, 1, 2, 6, 4, 5 } 
		};
		randomize();
		removeCells(difficulty);
	}

	public int[][] getBoard() {
		return board;
	}

	public void printBoard() {
		for (int r = 0; r < 9; r++) {
			if (r != 0 && r % 3 == 0) {
				System.out.print("---------------------\n");
			}
			for (int c = 0; c < 9; c++) {
				int cell = board[r][c];
				if (c != 0 && c % 3 == 0) {
					System.out.print("| ");
				}
				if (cell == 0) {
					System.out.print("  ");
				} else {
					System.out.print(cell + " ");
				}
			}
			System.out.print("\n");
		}
	}

	private void randomize() {
		shuffleNumbers();
		shuffleRows();
		shuffleCols();
		shuffleRowBlocks();
		shuffleColBlocks();
	}

	/**
	 * Removes a specified number of cells in order to set an approximate difficulty
	 * for the Sudoku puzzle.
	 * 
	 * @param difficulty How many cells will be removed
	 */
	private void removeCells(String difficulty) {
		ArrayList<int[]> positions = getPositions();
		Collections.shuffle(positions);

		// how many fewer cells will be removed at max on a specified difficulty
		int modEasy = 32;
		int modMedium = 16;

		int n = positions.size() - 17; // max of 64 cells removed
		if (difficulty.toUpperCase().equals("MEDIUM")) {
			n -= modMedium;
		} else if (difficulty.toUpperCase().equals("EASY")) {
			n -= modEasy;
		}

		// removes a max of n cells from the board
		while (n > 0) {
			int[] pos = positions.remove(positions.size() - 1);
			int row = pos[0];
			int col = pos[1];

			int val = board[row][col];

			board[row][col] = 0;

			// so that the solution board is modified, not the original board
			deepCopyBoardToSol();

			// if there's >1 solution, tries a different position
			if (solve(boardSol, 0, 0, 0) > 1) {
				board[row][col] = val;
			}

			n--;
		}
	}

	/**
	 * Finds whether a Sudoku puzzle has >1 solution. Only cares if count is >= 2,
	 * doesn't care about how many specific solutions. Modified version of the
	 * algorithm by fabian at
	 * https://stackoverflow.com/questions/24343214/determine-whether-a-sudoku-has-a-unique-solution
	 * 
	 * @param board 2D int array representing the board.
	 * @param row
	 * @param col
	 * @param count How many solutions.
	 * @return
	 */
	private int solve(int[][] board, int row, int col, int count /* initailly called with 0 */) {
		if (row == 9) {
			row = 0;
			if (++col == 9)
				return 1 + count;
		}
		if (board[row][col] != 0) // skip filled cells
			return solve(board, row + 1, col, count);
		// search for 2 solutions instead of 1
		// break, if 2 solutions are found
		for (int n = 1; n <= 9 && count < 2; ++n) {
			if (cellIsValid(board, row, col, n)) {
				board[row][col] = n;
				// add additional solutions
				count = solve(board, row + 1, col, count);
			}
		}
		board[row][col] = 0; // reset on backtrack
		return count;
	}

	/**
	 * Checks if a specified cell is valid for the given value.
	 * 
	 * @param board Sudoku board.
	 * @param row   Specified row.
	 * @param col   Specified column.
	 * @param n     Value being checked.
	 * @return True if valid.
	 */
	private boolean cellIsValid(int[][] board, int row, int col, int n) {
		return (rowIsValid(board, col, n) && colIsValid(board, row, n) && blockIsValid(board, row, col, n));
	}

	/**
	 * Gets an ArrayList of row, col coordinate pairs, representing every space on
	 * the board.
	 */
	private ArrayList<int[]> getPositions() {
		ArrayList<int[]> positions = new ArrayList<>();
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				int pos[] = { r, c };
				positions.add(pos);
			}
		}
		return positions;
	}

	/**
	 * Checks if a specified row is valid for the given column and value.
	 * 
	 * @param board Sudoku board.
	 * @param col   Specified column.
	 * @param n     Value being checked.
	 * @return True if valid.
	 */
	private boolean rowIsValid(int[][] board, int col, int n) {
		for (int r = 0; r < 9; r++) {
			if (board[r][col] == n) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a specified column is valid for the given row and value.
	 * 
	 * @param board Sudoku board.
	 * @param row   Specified row.
	 * @param n     Value being checked.
	 * @return True if valid.
	 */
	private boolean colIsValid(int[][] board, int row, int n) {
		for (int c = 0; c < 9; c++) {
			if (board[row][c] == n) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if a specified 3x3 block on the board is valid for the given value.
	 * 
	 * @param board Sudoku board.
	 * @param row   Specified row.
	 * @param col   Specified column.
	 * @param n     Value being checked.
	 * @return True if valid.
	 */
	private boolean blockIsValid(int[][] board, int row, int col, int n) {
		int rowBlock = (row / 3) * 3;
		int colBlock = (col / 3) * 3;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (board[rowBlock + r][colBlock + c] == n) {
					return false;
				}
			}
		}
		return true;
	}

	private void shuffleNumbers() {
		for (int i = 1; i <= 9; i++) {
			int n = ThreadLocalRandom.current().nextInt(1, 10);
			swapNumbers(i, n);
		}
	}

	private void shuffleRows() {
		int blockNum;
		for (int i = 0; i < 9; i++) {
			int n = ThreadLocalRandom.current().nextInt(0, 3);
			blockNum = i / 3;
			swapRows(i, blockNum * 3 + n);
		}
	}

	private void shuffleCols() {
		int blockNum;
		for (int i = 0; i < 9; i++) {
			int n = ThreadLocalRandom.current().nextInt(0, 3);
			blockNum = i / 3;
			swapCols(i, blockNum * 3 + n);
		}
	}

	private void shuffleRowBlocks() {
		for (int i = 0; i < 3; i++) {
			int n = ThreadLocalRandom.current().nextInt(0, 3);
			swapRowBlocks(i, n);
		}
	}

	private void shuffleColBlocks() {
		for (int i = 0; i < 3; i++) {
			int n = ThreadLocalRandom.current().nextInt(0, 3);
			swapColBlocks(i, n);
		}
	}

	private void swapNumbers(int n1, int n2) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (board[row][col] == n1) {
					board[row][col] = n2;
				} else if (board[row][col] == n2) {
					board[row][col] = n1;
				}
			}
		}
	}

	private void swapRows(int row1, int row2) {
		int[] row = board[row1];
		board[row1] = board[row2];
		board[row2] = row;
	}

	private void swapCols(int col1, int col2) {
		int colVal;
		for (int i = 0; i < 9; i++) {
			colVal = board[i][col1];
			board[i][col1] = board[i][col2];
			board[i][col2] = colVal;
		}
	}

	private void swapRowBlocks(int row1, int row2) {
		for (int i = 0; i < 3; i++) {
			swapRows(row1 * 3 + i, row2 * 3 + i);
		}
	}

	private void swapColBlocks(int col1, int col2) {
		for (int i = 0; i < 3; i++) {
			swapCols(col1 * 3 + i, col2 * 3 + i);
		}
	}

	/**
	 * Makes a deep copy of the current board. Used so that, when checking if a
	 * board has a solution, the current board is NOT modified.
	 */
	private void deepCopyBoardToSol() {
		boardSol = new int[board.length][board[0].length];
		for (int r = 0; r < boardSol.length; r++) {
			boardSol[r] = Arrays.copyOf(board[r], board[r].length);
		}
	}

}
