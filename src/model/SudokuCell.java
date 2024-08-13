package model;

import java.util.ArrayList;

/**
 * Respresentation of a single cell in a Sudoku game.
 * 
 * @author Oscar Metcalf
 */
public class SudokuCell {

	private int heldValue;
	private ArrayList<Integer> candidateValues;
	private boolean initial;

	/**
	 * Constructs a new cell which can be edited by the player.
	 */
	public SudokuCell() {
		heldValue = 0;
		candidateValues = new ArrayList<>();
		initial = false;
	}

	/**
	 * Constructs a cell which cannot be edited by the player. Used for creating a
	 * new game.
	 * 
	 * @param initialValue The initial value of the cell.
	 */
	public SudokuCell(int initialValue) {
		heldValue = initialValue;
		candidateValues = new ArrayList<>();
		initial = true;
	}

	/**
	 * Sets the value of the cell to 0 if it is not an initial game cell.
	 */
	public void removeValue() {
		if (!initial) {
			heldValue = 0;
		}
	}

	/**
	 * Removes the specified candidate value from the cell if it exists.
	 * 
	 * @param val The value to try and remove.
	 */
	public void removeCandidateValue(int val) {
		if (initial) {
			return;
		}
		for (int i = 0; i < candidateValues.size(); i++) {
			if (candidateValues.get(i) == val) {
				candidateValues.remove(i);
				return;
			}
		}
	}
	
	public void removeCandidateValue() {
		if (!candidateValues.isEmpty()) {
			candidateValues.remove(candidateValues.size()-1);
		}
	}
	
	public void clearCandidates() {
		candidateValues.clear();
	}

	/**
	 * Returns the current cell value.
	 * 
	 * @return
	 */
	public int getHeldValue() {
		return heldValue;
	}

	/**
	 * Gets all the candidate values.
	 * 
	 * @return An ArrayList of the current candidate values.
	 */
	public ArrayList<Integer> getCandidateValues() {
		return candidateValues;
	}

	/**
	 * Sets the cell's value, if it isn't an initial cell.
	 * 
	 * @param heldValue The value to be set.
	 * @return itself, for modification upon construction.
	 */
	public SudokuCell setHeldValue(int heldValue) {
		if (initial) {
			return this;
		}
		this.heldValue = heldValue;
		return this;
	}

	/**
	 * Adds the specified candidate value to the cell if possible and if it isn't an
	 * initial cell.
	 * 
	 * @param candidateValue The value to add.
	 */
	public void addCandidateValue(int candidateValue) {
		if (initial || candidateValue <= 0 || candidateValue > 9 || candidateValues.contains(candidateValue)) {
			return;
		}
		candidateValues.add(candidateValue);
	}
	
	public boolean isInitial() {
		return initial;
	}

}
