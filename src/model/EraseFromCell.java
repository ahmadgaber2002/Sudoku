package model;

import java.util.ArrayList;

public class EraseFromCell implements Action{

	private SudokuCell cell;
	private boolean eraseHeld;
	
	private int heldValue;
	private	ArrayList<Integer> candidateValues;
	
	public EraseFromCell(SudokuCell cell, boolean eraseHeld) {
		this.cell = cell;
		this.eraseHeld = eraseHeld;
	}
	
	/*
	 * action
	 */
	@Override
	public void action() {
		if(eraseHeld) {
			heldValue = cell.getHeldValue();
			cell.removeValue();
		}
		else {
			candidateValues = new ArrayList<>(cell.getCandidateValues());
			cell.clearCandidates();
		}
	}
	
	/*
	 * undo action
	 */
	@Override
	public void undo() {
		if(eraseHeld) {
			cell.setHeldValue(heldValue);
		}
		else {
			for (Integer value : candidateValues) {
				cell.addCandidateValue(value);
			}
		}
	}

}
