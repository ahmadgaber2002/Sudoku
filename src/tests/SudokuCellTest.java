package tests;

import static org.junit.jupiter.api.Assertions.*;
import model.SudokuCell;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class SudokuCellTest {

	@BeforeEach
	public void init(){
		cell1 = new SudokuCell();
		cell2 = new SudokuCell(5);
	}

	private SudokuCell cell1, cell2;

	@Test
	void testEmptyCell() {
		assertEquals(cell1.getHeldValue(), 0);
		assertEquals(cell1.getCandidateValues().size(), 0);
		cell1.setHeldValue(1);
		cell1.addCandidateValue(5);
		assertFalse(cell1.getHeldValue() == 0);
		assertFalse(cell1.getCandidateValues().size() == 0);
	}
	
	@Test
	void testAddHeldValue() {
		assertEquals(cell1.getHeldValue(), 0);
		cell1.setHeldValue(1);
		assertTrue(cell1.getHeldValue() == 1);
		assertFalse(cell1.getHeldValue() == 2);
		cell1.setHeldValue(3);
		assertTrue(cell1.getHeldValue() == 3);
		assertFalse(cell1.getHeldValue() == 4);
	}
	
	@Test
	void testAddCandidateValues() {
		assertEquals(cell1.getCandidateValues().size(), 0);
		cell1.addCandidateValue(0);
		cell1.addCandidateValue(10);
		cell1.addCandidateValue(400);
		assertEquals(cell1.getCandidateValues().size(), 0);
		cell1.addCandidateValue(1);
		cell1.addCandidateValue(2);
		cell1.addCandidateValue(5);
		cell1.addCandidateValue(9);
		assertEquals(cell1.getCandidateValues().size(), 4);
	}
	
	@Test
	void testRemoveValue() {
		cell1.setHeldValue(4);
		assertEquals(4, cell1.getHeldValue());
		cell1.removeValue();
		assertEquals(0, cell1.getHeldValue());
		cell2.setHeldValue(1);
		cell2.removeValue();
		assertEquals(5, cell2.getHeldValue());
	}
	
	@Test
	void testRemoveCandidates() {
		cell1.addCandidateValue(1);
		cell1.addCandidateValue(2);
		cell1.addCandidateValue(3);
		
		cell2.addCandidateValue(5);
		cell2.addCandidateValue(6);
		assertEquals(0, cell2.getCandidateValues().size());
		cell2.removeCandidateValue(5);
		assertEquals(0, cell2.getCandidateValues().size());
		
		cell1.removeCandidateValue(1);
		cell1.removeCandidateValue(10);
		cell1.removeCandidateValue(0);
		assertEquals(2, cell1.getCandidateValues().size());
		assertTrue(cell1.getCandidateValues().contains(2));
		assertTrue(cell1.getCandidateValues().contains(3));
	}

}
