package view_controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.OurObserver;
import model.SoundManager;
import model.SudokuCell;
import model.SudokuGame;
/**
 * GUI representation of the entire Sudoku game.
 */
public class SudokuGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private SudokuGame game;
	
	private Button newGameButton;
	
	private SoundManager soundManager;
	
	// board size must be multiple of 9
	private final int boardSize = 450;
	private final int sidebarWidth = 300;
	private final int sidebarHeight = 600;
	
	private Stage stage;
	private Pane pane;
	private StackPane stackPane;
	private Timeline timeline;
	private Text timerText;
	private Button pauseButton;
	private HBox hbar;
	
	private String difficulty;
	private Button easy, medium;
	
	private OurObserver board;
	private SudokuSidebar sidebar;
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		initializeView();
		
		game = new SudokuGame(difficulty);
		board = new SudokuBoard(game, boardSize, soundManager);
		game.addObserver(board);
		stackPane.setId("stackPane");
		
		sidebar = new SudokuSidebar((SudokuBoard) board, game, sidebarWidth, sidebarHeight);
		sidebar.setId("sidebar");
		
		newGameButton = new Button();
		pauseButton = new Button();
		Image pausePath = new Image("./Images/Pause-Button.png");
        ImageView undoImage = new ImageView(pausePath);
        undoImage.setFitHeight(20);
        undoImage.setFitWidth(20);
        
        pauseButton.setGraphic(undoImage);
		setupNewGame();

		hbar.getChildren().addAll(timerText,pauseButton, easy, medium);

		stackPane.getChildren().add((Node)board);
		pane.getChildren().addAll(stackPane, sidebar, newGameButton,hbar);
		
		setLayouts();
	}
	
	/*
	 * Description: Initializes necessary visual componenets
	 */
	private void initializeView() {
		pane = new Pane();
		pane.setId("mainPane");
		stackPane = new StackPane();
		Scene scene = new Scene(pane, 900, 700);
		scene.getStylesheets().add("view_controller/style.css");
		
		hbar = new HBox();
		
		difficulty = "EASY";
		easy = new Button("Easy");
		easy.setOnAction(e ->{
			difficulty = "EASY";
		});
		medium = new Button("Medium");
		medium.setOnAction(e ->{
			difficulty = "MEDIUM";
		});

		
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}
	
	/*
	 * Description: Places the visual objects in the main Pane.
	 */
	private void setLayouts() {
		stackPane.setLayoutX(50);
		stackPane.setLayoutY((700 - 450) / 2);
		sidebar.setLayoutX(550);
		sidebar.setLayoutY(50);
		
		newGameButton.setLayoutX(50);
		newGameButton.setLayoutY(boardSize + (700 - 450) / 2 + 10);
		
		HBox.setMargin(pauseButton,new Insets(0,0,0,5));
		hbar.setLayoutX(50);
		hbar.setLayoutY(50);
	}
	
	/*
	 * Description: creates the components for a new game such as the button, and the timer.
	 * 				Also sets the action events for a new game to begin.
	 */
	private void setupNewGame() {
		newGameButton = new Button();
		Image undoPath = new Image("./Images/newGameButton.png");
        ImageView undoImage = new ImageView(undoPath);
        undoImage.setFitHeight(100);
        undoImage.setFitWidth(boardSize-10);
        

		timerText = new Text("00:00");
		timerText.setFont(Font.font("Arial",24));
		timeline = new Timeline(new KeyFrame(Duration.seconds(1),event->{
			int seconds = Integer.parseInt(timerText.getText().substring(3));
			int minutes = Integer.parseInt(timerText.getText().substring(0, 2));
			seconds++;
			if(seconds == 60) {
				seconds = 0;
				minutes++;
				
			}
			timerText.setText(String.format("%02d:%02d", minutes,seconds));
		}));
		
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		pauseButton.setOnAction(e->{
			if (timeline.getStatus() == Animation.Status.PAUSED) {
	            timeline.play();
	        } else {
	            timeline.pause();
	            ButtonType resumeButton = new ButtonType("Resume");
	            
	            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Click on Resume to continue playing.",resumeButton);
	            alert.setTitle("Game Paused");
	            alert.setHeaderText(null);
	            alert.showAndWait();
	            if(alert.getResult()==resumeButton) {
	            	timeline.play();
	            }
	        }
		});
        
        newGameButton.setGraphic(undoImage);
        newGameButton.setOnAction(e->{
        	game = new SudokuGame(difficulty);
        	((SudokuBoard) board).setGame(game);
        	sidebar.setGame(game);
        	game.addObserver(board);
        	timerText.setText("00:00");
        });
	}
	
}
