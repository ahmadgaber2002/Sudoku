package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.SudokuGame;

class SudokuGameTest {

	private SudokuGame game1, game2, game3, game4, game5;
	
	@BeforeEach
	public void init() {
		game1 = new SudokuGame("premade");
		game2 = new SudokuGame("EASY");
		game3 = new SudokuGame("Medium");
		game4 = new SudokuGame("Hard");
	}
	
	@Test
	void testSetBoardCreation() {
		System.out.println("premade");
		game1.printBoard("");
		assertEquals(0, game1.getCell(0, 0).getHeldValue());
		assertEquals(6, game1.getCell(1, 0).getHeldValue());
		assertEquals(8, game1.getCell(3, 0).getHeldValue());
		assertEquals(1, game1.getCell(8, 4).getHeldValue());
		assertEquals(9, game1.getCell(4, 6).getHeldValue());
		assertEquals(1, game1.getCell(0, 8).getHeldValue());
	}
	
	@Test
	void testRandomBoardCreation() {
		System.out.println("random");
		game2.printBoard("");
		game3.printBoard("");
		game4.printBoard("");
		assertTrue(true); //TODO redo once algorithm is done
	}
	
	@Test
	void testRandomBoardSolvability() {
		game2.solve();
		game3.solve();
		game4.solve();
		game2.printBoard("solution");
		game3.printBoard("solution");
		game4.printBoard("solution");
	}
	
	@Test
	void testBadInput() {
		game5 = new SudokuGame("uhhhh");
		assertTrue(game5.getCell(1, 0).getHeldValue() == game1.getCell(1, 0).getHeldValue());
	}

	@Test
	void testSolution() {
		game1.solve();
		System.out.println("solution to premade"); 
		game1.printBoard("solution");
		assertFalse(game1.checkIfSolutionIsCorrect());
	}
	
	@Test
	void testCheckCorrectValue() {
		assertFalse(game1.cellIsValid(0, 0, 1));
		assertTrue(game1.cellIsValid(0, 0, 4));
		assertFalse(game1.cellIsValid(0, 0, 9));
		assertFalse(game1.cellIsValid(0, 2, 4));
		assertFalse(game1.cellIsValid(8, 8, 6));
		game1.addMove(0, 0, 4);
		assertTrue(game1.cellIsValid(0, 0, game1.getCell(0, 0).getHeldValue()));
	}
	
	@Test
	void cannotModifyInitialValues() {
		assertEquals(6, game1.getCell(1, 0).getHeldValue());
		game1.addMove(1, 0, 9);
		assertEquals(6, game1.getCell(1, 0).getHeldValue());
	}
	
	@Test
	void testRemove() {
		game1.deleteMove(1, 0);
		assertEquals(6, game1.getCell(1, 0).getHeldValue());
		game1.addMove(0, 2, 5);
		assertEquals(5, game1.getCell(0, 2).getHeldValue());
		game1.deleteMove(0, 2);
		assertEquals(0, game1.getCell(0, 2).getHeldValue());
	}
	
	@Test
	void testCandidatesAdd() {
		ArrayList<Integer> candidates00 = game1.getCandidates(0, 0);
		assertEquals(0, candidates00.size());
		game1.addCandidate(0, 0, 1);
		game1.addCandidate(0, 0, 2);
		game1.addCandidate(0, 0, 3);
		game1.addCandidate(0, 0, 0);
		game1.addCandidate(0, 0, 10);
		game1.addCandidate(0, 0, -127);
		assertEquals(3, candidates00.size());
		assertTrue(candidates00.contains(1));
		assertTrue(candidates00.contains(2));
		assertTrue(candidates00.contains(3));
		assertFalse(candidates00.contains(10));
		assertFalse(candidates00.contains(0));
		assertFalse(candidates00.contains(-127));
	}
	
//	@Test
//	void testCandidatesRemove() {
//		ArrayList<Integer> candidates00 = game1.getCandidates(0, 0);
//		game1.addCandidate(0, 0, 1);
//		game1.addCandidate(0, 0, 2);
//		game1.addCandidate(0, 0, 3);
//		assertEquals(3, candidates00.size());
//		game1.deleteCandidate(0, 0, 1);
//		game1.deleteCandidate(0, 0, 2);
//		game1.deleteCandidate(0, 0, 10);
//		game1.deleteCandidate(0, 0, 5);
//		game1.deleteCandidate(1, 0, 2);
//		assertEquals(1, candidates00.size());
//		assertTrue(candidates00.contains(3));
//		assertFalse(candidates00.contains(1));
//		assertFalse(candidates00.contains(2));
//	}
	
}
