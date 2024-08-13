package model;

import java.util.ArrayList;

public class AddToCell implements Action{
	
	private SudokuCell cell;
	private boolean addHeld;
	
	private int value;
	
	private int oldValue;
	private ArrayList<Integer> candidates;
	
	public AddToCell(SudokuCell cell, boolean addHeld, int value){
		this.cell = cell;
		this.addHeld = addHeld;
		this.value = value;
	}
	
	/*
	 * action
	 */
	@Override
	public void action() {
		oldValue = cell.getHeldValue();
		if (addHeld) {
			cell.setHeldValue(value);
			candidates = new ArrayList<Integer>(cell.getCandidateValues());
			cell.clearCandidates();
		}
		else {
			cell.setHeldValue(0);
			cell.addCandidateValue(value);
		}
	}
	
	/*
	 * undo action
	 */
	@Override
	public void undo() {
		if (addHeld) {
			cell.setHeldValue(oldValue);
			for (Integer i : candidates) {
				cell.addCandidateValue(i);
			}
		}
		else {
			cell.setHeldValue(oldValue);
			cell.removeCandidateValue(value);
		}
	}

}
