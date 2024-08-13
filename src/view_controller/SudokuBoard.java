package view_controller;

import java.util.ArrayList;
import java.util.Stack;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.OurObserver;
import model.SoundManager;
import model.SudokuCell;
import model.SudokuGame;

/**
 * GUI representation of a Sudoku game board.
 */
public class SudokuBoard extends Canvas implements OurObserver{

	private SudokuGame game;
	private GraphicsContext graphics;
	
	private int boardSize;
	private int cellSize;
	
	private final int boardStartX = 0;
	private final int boardStartY = 0;
	
	private int column, row;
	private boolean notes;
	
    private SoundManager soundManager = new SoundManager();
    private Stack<SudokuCell> cellStack;
    SudokuBoard board = new SudokuBoard(game, boardSize, soundManager);

	
	/*
	 * board gui for the sudoku game
	 */
	public SudokuBoard(SudokuGame game, int boardSize, SoundManager soundManager) {
		super(boardSize, boardSize);
		this.boardSize = boardSize;
		this.soundManager = soundManager;
		this.cellStack = new Stack<>();
		cellSize = boardSize/9;
		this.game = game;
		column = 0;
		row = 0;
		graphics = this.getGraphicsContext2D();
		clickCellHandler(null);
		drawEverything();
	}
	
	/*
	 * Description: draws everything when the sudoku game changes state
	 * @param theObserved
	 */
	@Override
	public void update(Object theObserved) {
		drawEverything();
	}
	
	/*
	 * Description: draws the entire sudoku board
	 */
	private void drawEverything() {
		drawBoard();
		selectCell(column, row);
		drawLines();
		drawValues();
		drawCandidates();
		drawErrors();
	}
	
	/*
	 * Description: draws the background of the board
	 */
	private void drawBoard() {
		graphics.setFill(Color.rgb(61, 199, 201));
		graphics.fillRect(boardStartX, boardStartY, boardSize, boardSize);
	}
	
	/*
	 * Description: draws the lines for the board
	 */
	private void drawLines() {
		int startX = boardStartX;
		int startY = boardStartY;
		for (int i=0; i < 10 ; i++) {
			graphics.setLineWidth(1);
			if (i % 3 == 0) {
				graphics.setLineWidth(3);
			}
			graphics.strokeLine(startX, boardStartY, startX, boardStartY + boardSize);
			graphics.strokeLine(boardStartX, startY, boardStartX + boardSize, startY);
			startX += cellSize;
			startY += cellSize;
		}
	}
	
	/*
	 * Description: paints the selected cell white
	 */
	private void selectCell(int column, int row) {
		graphics.setFill(Color.AZURE);

		int offsetX1 = 1, offsetX2 = 1;
		int offsetY1 = 1, offsetY2 = 1;
		if (column % 3 == 0) {
			offsetX1 ++;
		}
		if ((column + 1) % 3 == 0) {
			offsetX2 ++;
		}
		if (row % 3 == 0) {
			offsetY1 ++;
		}
		if ((row + 1) % 3 == 0) {
			offsetY2 ++;
		}
		graphics.fillRect(column * cellSize + offsetX1, row * cellSize + offsetY1, cellSize - (offsetX1 + offsetX2), cellSize - (offsetY1 + offsetY2));
	}
	
	/*
	 * Description: Draws the held values of the sudoku cells
	 */
	private void drawValues() {
		int fontSize = 24;
		
		ArrayList<ArrayList<SudokuCell>> board = game.getBoard();
		for (int row=0 ; row < board.size() ; row++) {
			for (int column=0 ; column < board.get(row).size() ; column++) {
				SudokuCell cell = board.get(row).get(column);
				if (cell.getHeldValue() != 0) {
					graphics.setFill(Color.BLACK);
					int x = (column * cellSize + cellSize/2) - fontSize/4;
					int y = (row * cellSize + cellSize/2) + fontSize/4;
					String text = String.valueOf(cell.getHeldValue());
					graphics.setFont(new Font("Arial", fontSize));
					graphics.fillText(text, x, y);
				}
			}
		}
	}
	
	/*
	 * Description: Draws the candidate values of the sudoku cell
	 */
	private void drawCandidates() {
		for (int r=0; r < game.getBoard().size() ; r++) {
			for (int c=0; c < game.getBoard().get(r).size() ; c++) {
				ArrayList<Integer> candidates = game.getCandidates(r, c);
				if (!candidates.isEmpty()) {
					graphics.setFont(new Font("Arial", 12));
					if (candidates.contains(1)) {
						drawOne(c, r);
					}
					if (candidates.contains(2)) {
						drawTwo(c, r);
					}
					if (candidates.contains(3)) {
						drawThree(c, r);
					}
					if (candidates.contains(4)) {
						drawFour(c, r);
					}
					if (candidates.contains(5)) {
						drawFive(c, r);
					}
					if (candidates.contains(6)) {
						drawSix(c, r);
					}
					if (candidates.contains(7)) {
						drawSeven(c, r);
					}
					if (candidates.contains(8)) {
						drawEight(c, r);
					}
					if (candidates.contains(9)) {
						drawNine(c, r);
					}
				}
			}
		}
	}

	private void drawOne(int c, int r) {
		graphics.fillText("1", c * cellSize + 5, r * cellSize + 15);
	}
	
	private void drawTwo(int c, int r) {
		graphics.fillText("2", c * cellSize + 19, r * cellSize + 15);
	}
	
	private void drawThree(int c, int r) {
		graphics.fillText("3", c * cellSize + 33, r * cellSize + 15);
	}
	
	private void drawFour(int c, int r) {
		graphics.fillText("4", c * cellSize + 5, r * cellSize + 30);
	}
	
	private void drawFive(int c, int r) {
		graphics.fillText("5", c * cellSize + 19, r * cellSize + 30);
	}
	
	private void drawSix(int c, int r) {
		graphics.fillText("6", c * cellSize + 33, r * cellSize + 30);
	}
	
	private void drawSeven(int c, int r) {
		graphics.fillText("7", c * cellSize + 5, r * cellSize + 45);
	}
	
	private void drawEight(int c, int r) {
		graphics.fillText("8", c * cellSize + 19, r * cellSize + 45);
	}
	
	private void drawNine(int c, int r) {
		graphics.fillText("9", c * cellSize + 33, r * cellSize + 45);
	}
	
	/*
	 * Description: sets the action handlers of the sudoku board view.
	 */
	private void clickCellHandler(SudokuCell cell) {
		this.setOnMouseMoved(event -> {
			if (game.getCell(row, column).getHeldValue() == 0) {
				selectCell(column, row);
				drawEverything();
				drawMouseOver(event);
			}
		});
		this.setOnMouseClicked(event -> {
			soundManager.playClickSound(); // This play the click.mp3 sound when you click on the cell.
			cellStack.push(cell);
			if (game.getCell(row, column).getHeldValue() == 0) {
				int xCell = (int) (event.getX() - (column * cellSize));
				int yCell = (int) (event.getY() - (row * cellSize));
				
				ArrayList<Integer> candidates = game.getCell(row, column).getCandidateValues();
				if (xCell > 0 && xCell < 20) {
					if (yCell > 0 && yCell < 20) {
						if (!candidates.contains(1)) {
							candidates.add(1);
						}
						else {
							candidates.removeIf(num -> num == 1);
						}
					}
					if (yCell > 20 && yCell < 35) {
						if (!candidates.contains(4)) {
							candidates.add(4);
						}
						else {
							candidates.removeIf(num -> num == 4);
						}
					}
					if (yCell > 35 && yCell < 60) {
						if (!candidates.contains(7)) {
							candidates.add(7);
						}
						else {
							candidates.removeIf(num -> num == 7);
						}
					}
				}
				if (xCell > 20 && xCell < 35) {
					if (yCell > 0 && yCell < 20) {
						if (!candidates.contains(2)) {
							candidates.add(2);
						}
						else {
							candidates.removeIf(num -> num == 2);
						}
					}
					if (yCell > 20 && yCell < 35) {
						if (!candidates.contains(5)) {
							candidates.add(5);
						}
						else {
							candidates.removeIf(num -> num == 5);
						}
					}
					if (yCell > 35 && yCell < 60) {
						if (!candidates.contains(8)) {
							candidates.add(8);
						}
						else {
							candidates.removeIf(num -> num == 8);
						}
					}
				}
				if (xCell > 35 && xCell < 60) {
					if (yCell > 0 && yCell < 20) {
						if (!candidates.contains(3)) {
							candidates.add(3);
						}
						else {
							candidates.removeIf(num -> num == 3);
						}
					}
					if (yCell > 20 && yCell < 35) {
						if (!candidates.contains(6)) {
							candidates.add(6);
						}
						else {
							candidates.removeIf(num -> num == 6);
						}
					}
					if (yCell > 35 && yCell < 60) {
						if (!candidates.contains(9)) {
							candidates.add(9);
						}
						else {
							candidates.removeIf(num -> num == 9);
						}
					}
				}
				else {
					double x = event.getX();
					double y = event.getY();
					column = (int) x/cellSize;
					row = (int) y/cellSize;
				}
				game.notifyObservers(game);
			}
			else {
				double x = event.getX();
				double y = event.getY();
				column = (int) x/cellSize;
				row = (int) y/cellSize;
			}
			game.notifyObservers(game);

		});
	}
	
	/*
	 * Description: When a user adds an invalid held value an error symbol is drawn
	 * on the bottom right corner of the cell and any other cells the value conflicts with.
	 */
	private void drawErrors() {
		graphics.setStroke(Color.DARKRED);
		for (int r=0; r < game.getBoard().size() ; r++) {
			for (int c=0; c < game.getBoard().get(r).size() ; c++) {
				if (!game.cellIsValid(r, c, game.getCell(r, c).getHeldValue()) && (game.getCell(r, c).getHeldValue() != 0)) {
					graphics.strokeOval((c+1) * cellSize - 10, (r+1) * cellSize - 10, 5, 5);
				}
			}
		}
		graphics.setStroke(Color.BLACK);
	}
	
	
	/*
	 * Description: when hovering over a selected cell, the candidate numbers are drawn.
	 */
	private void drawMouseOver(MouseEvent event) {
		graphics.setFill(Color.rgb(130, 103, 110, 0.5));
		graphics.setFont(new Font("Arial", 12));
		
		int xCell = (int) (event.getX() - (column * cellSize));
		int yCell = (int) (event.getY() - (row * cellSize));

		if (xCell > 0 && xCell < 20) {
			if (yCell > 0 && yCell < 20) {
				drawOne(column, row);
			}
			if (yCell > 20 && yCell < 35) {
				drawFour(column, row);
			}
			if (yCell > 35 && yCell < 60) {
				drawSeven(column, row);
			}
		}
		if (xCell > 20 && xCell < 35) {
			if (yCell > 0 && yCell < 20) {
				drawTwo(column, row);
			}
			if (yCell > 20 && yCell < 35) {
				drawFive(column, row);
			}
			if (yCell > 35 && yCell < 60) {
				drawEight(column, row);
			}
		}
		if (xCell > 35 && xCell < 60) {
			if (yCell > 0 && yCell < 20) {
				drawThree(column, row);
			}
			if (yCell > 20 && yCell < 35) {
				drawSix(column, row);
			}
			if (yCell > 35 && yCell < 60) {
				drawNine(column, row);
			}
		}
		
		graphics.setFill(Color.rgb(0, 0, 0, 1));
	}
	
	public void toggleNotes() {
		notes = !notes;
	}
	
	public boolean notesOn() {
		return notes;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setGame(SudokuGame game) {
		this.game = game;
		this.update(game);
	}
	

	
}
