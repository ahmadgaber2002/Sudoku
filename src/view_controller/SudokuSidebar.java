package view_controller;


import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import model.SudokuCell;
import model.SudokuGame;

/**
 * GUI representation of an input menu for a Sudoku game.
 */
public class SudokuSidebar extends BorderPane{

	private SudokuGame game;
	private SudokuBoard board;

	private GridPane buttonsPane;
	private HBox toolsBox;
	private Button undoButton;
	private Button eraseButton;
	private Button notesButton;
	private Button resetButton;
	private Button revealCellButton;
	private Button revealButton;
	
	private int width, height;

	public SudokuSidebar(SudokuBoard board, SudokuGame game, int width, int height) {
		this.setPrefSize(width, height);
		this.game = game;
		this.board = board;
		
		this.width = width;
		this.height = height;
		
		setupView();
	}
	
	private void setupView() {
        setupNotesButton();
        setupUndoButton();
        setupEraseButton();
        setupResetButton();
        setupRevealCellButton();
        setupRevealButton();
		
		toolsBox = new HBox();
		toolsBox.getChildren().addAll(undoButton,eraseButton,notesButton, revealCellButton, revealButton);
		
		setupNumberPad();

		this.setTop(toolsBox);
		this.setCenter(buttonsPane);
		this.setBottom(resetButton);
	}
	
	private void setupNumberPad() {
		buttonsPane = new GridPane();
		
		for(int r =0; r < 3;r++) {
			for(int c = 0;c <3;c++) {
				Button button = new Button(String.valueOf(r*3+c+1));
				button.setFont(new Font(20));
				button.setPrefSize(100, 100);
				buttonsPane.add(button, c, r);
				
				button.setOnAction(e -> {
					
					int column = board.getColumn();
					int row = board.getRow();
					int value = game.getBoard().get(row).get(column).getHeldValue();
					
					int buttonValue = Integer.parseInt(button.getText());
					
					if (board.notesOn() && !game.getCell(row, column).isInitial()) {
						if (game.getCandidates(row, column).contains(buttonValue)) {
							game.getCandidates(row, column).removeIf(n -> n == buttonValue);
							board.update(board);
						}
						else {
							game.addCandidate(row, column, buttonValue);
						}
					}
					
					else if (!board.notesOn() && !game.getCell(row, column).isInitial() && value!=buttonValue) {
						game.addMove(row, column, buttonValue);
					}
				});
			}
		}
	}
	
	private void setupNotesButton() {
		notesButton = new Button();
        Image notesPath = new Image("./Images/Notes-icon.png");
        ImageView notesImage = new ImageView(notesPath);
        notesImage.setFitHeight(30);
        notesImage.setFitWidth(30);
        notesButton.setGraphic(notesImage);
        
        notesButton.setOnAction(e->{
        	board.toggleNotes();
        	if (board.notesOn()) {
        		notesButton.setOpacity(0.5);
        	}
        	else {
        		notesButton.setOpacity(1);
        	}
        });
        notesButton.setId("notesButton");
	}
	
	private void setupEraseButton() {
		eraseButton = new Button();
		Image erasePath = new Image("./Images/eraseButtonImage.jpg");
        ImageView eraseImage = new ImageView(erasePath);
        eraseImage.setFitHeight(30);
        eraseImage.setFitWidth(30);
        eraseButton.setGraphic(eraseImage);
        
        eraseButton.setOnAction(e->{
        	int row = board.getRow();
        	int col = board.getColumn();
            if (!game.getCell(row, col).isInitial() && game.getCell(row, col).getHeldValue() != 0) {
                game.deleteMove(row, col);
            }
            else if (!game.getCandidates(row, col).isEmpty()){
            	game.deleteCandidates(row, col);
            }
        });
	}
	
	private void setupUndoButton() {
		undoButton = new Button();
		Image undoPath = new Image("./Images/undoButton.png");
        ImageView undoImage = new ImageView(undoPath);
        undoImage.setFitHeight(30);
        undoImage.setFitWidth(30);
        
        undoButton.setGraphic(undoImage);
        undoButton.setOnAction(e->{
        	game.undoMove();
        });
	}
	
	public void setGame(SudokuGame game) {
		this.game = game;
	}
	
	public void setupResetButton() {
		resetButton = new Button();
		Image undoPath = new Image("./Images/resetButton.png");
        ImageView undoImage = new ImageView(undoPath);
        undoImage.setFitHeight(120);
        undoImage.setFitWidth(width);
        
        resetButton.setGraphic(undoImage);
        resetButton.setOnAction(e->{
        	game.resetGame();
        	for (ArrayList<SudokuCell> row : game.getBoard()) {
        		for (SudokuCell cell : row) {
        			cell.clearCandidates();
        		}
        	}
        	game.notifyObservers(game);
        });
	}
	
	public void setupRevealCellButton() {
		revealCellButton = new Button();
		Image undoPath = new Image("./Images/revealCellButton.png");
        ImageView undoImage = new ImageView(undoPath);
        undoImage.setFitHeight(30);
        undoImage.setFitWidth(30);
        
        revealCellButton.setGraphic(undoImage);
        revealCellButton.setOnAction(e->{
        	game.getCell(board.getRow(), board.getColumn()).clearCandidates();
        	game.showCorrectCell(board.getRow(), board.getColumn());
        });
	}
	
	public void setupRevealButton() {
		revealButton = new Button();
		Image undoPath = new Image("./Images/revealButton.png");
        ImageView undoImage = new ImageView(undoPath);
        undoImage.setFitHeight(30);
        undoImage.setFitWidth(30);
        
        revealButton.setGraphic(undoImage);
        revealButton.setOnAction(e->{
        	for (int row = 0 ; row < game.getBoard().size() ; row++) {
        		for (int col = 0 ; col < game.getBoard().get(row).size() ; col++) {
        			game.getCell(row, col).clearCandidates();
        		}
        	}
        	game.revealBoard();
        });
	}

}

