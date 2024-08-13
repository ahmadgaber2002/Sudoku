package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import model.RandomBoardGenerator;

class RandomBoardGeneratorTest {

	@BeforeEach
	void setUp() {
		genEasy = new RandomBoardGenerator("easy");
		genMedium = new RandomBoardGenerator("medium");
		genHard = new RandomBoardGenerator("hard");
	}
	
	RandomBoardGenerator genEasy, genMedium, genHard;
	
	@Test
	void easyBoardGenTest() {
		int[][] brd = genEasy.getBoard();
		int count = 0;
		for (int[] r : brd) {
			for (int c : r) {
				if (c == 0) {
					count++;
				}
			}
		}
		assertTrue(count <= 32);
		System.out.println("Easy");
		genEasy.printBoard();
	}
	
	@Test
	void mediumBoardGenTest() {
		int[][] brd = genMedium.getBoard();
		int count = 0;
		for (int[] r : brd) {
			for (int c : r) {
				if (c == 0) {
					count++;
				}
			}
		}
		assertTrue(count <= 48);
		System.out.println("Medium");
		genMedium.printBoard();
	}
	
	@Test
	void hardBoardGenTest() {
		int[][] brd = genHard.getBoard();
		int count = 0;
		for (int[] r : brd) {
			for (int c : r) {
				if (c == 0) {
					count++;
				}
			}
		}
		assertTrue(count <= 64);
		System.out.println("Hard");
		genHard.printBoard();
	}

}
