package com.mycompany.tictactoegame;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoardController implements JoystickControllable {
    @FXML private Label ticLabel, tacLabel, toeLabel, player1Label, player2Label, winnerLabel;
    @FXML private GridPane gameBoard;
    @FXML private Button playAgainButton, finishButton, backbutton;
    @FXML private VBox titleContainer;
    
    private String playerXName = "Player X";
    private String playerOName = "Player O"; 
    private GameLogic game;
    private Button[][] board = new Button[3][3];
    private TranslateTransition winnerAnimation;
    private String gameMode;
    private String difficultyLevel;
    
    // Joystick control variables
    private int[] selectedRows = {0, 0}; // [player1, player2]
    private int[] selectedCols = {0, 0};
    private boolean[] joystickEnabled = {true, true};
    private String[] playerColors = {"#3498db", "#e74c3c"}; // Blue for P1, Red for P2
    
    private static final String HIGHLIGHT_ADDON = 
    "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +  // Gold/yellow gradient
    "-fx-border-width: 3px;" +
    "-fx-border-radius: 20px;" +  // Matches your button's rounded corners
    "-fx-border-style: solid outside;" +
    "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";  // Gold glow
    
    // Add these to your class variables
    private Button[] actionButtons = new Button[0];
    private int selectedButtonIndex = 0;
    private boolean inActionMenu = false;
 @FXML
    public void initialize() {
        game = new GameLogic();
        setupUI();
        createGameBoard();
        
        // Initialize action buttons array after FXML injection
        actionButtons = new Button[]{playAgainButton, finishButton, backbutton};
        
        // Set initial button styles
        
        playAgainButton.setOnAction(e -> resetGame());
        finishButton.setOnAction(e -> switchToScoreBoardScene());
        backbutton.setOnAction(e -> switchToPlayerNameScene());
    }
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        
        if ("Player vs Computer".equals(gameMode) && joystickId == 1) {
        return;
    }
    
    if (!joystickEnabled[joystickId] && !inActionMenu) return;

    Platform.runLater(() -> {
        if (inActionMenu) {
            // Handle navigation in action menu
            if (axisNumber == 4 || axisNumber == 0) { // LEFT/RIGHT
                if (value == 32769) { // LEFT
                    selectedButtonIndex = (selectedButtonIndex - 1 + actionButtons.length) % actionButtons.length;
                } 
                else if (value == 32767) { // RIGHT
                    selectedButtonIndex = (selectedButtonIndex + 1) % actionButtons.length;
                }
                updateActionButtonSelection();
            }
        } else {
            // Original game board navigation
            if (axisNumber == 5 || axisNumber == 1) { // UP/DOWN
                if (value == 32769 && selectedRows[joystickId] > 0) {
                    selectedRows[joystickId]--;
                } 
                else if (value == 32767 && selectedRows[joystickId] < 2) {
                    selectedRows[joystickId]++;
                }
                updateSelection(joystickId);
            } 
            else if (axisNumber == 4 || axisNumber == 0) { // LEFT/RIGHT
                if (value == 32769 && selectedCols[joystickId] > 0) {
                    selectedCols[joystickId]--;
                }
                else if (value == 32767 && selectedCols[joystickId] < 2) {
                    selectedCols[joystickId]++;
                }
                updateSelection(joystickId);
            }
        }
    });
}

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) {
    
     if ("Player vs Computer".equals(gameMode) && joystickId == 1) {
        return;
    }
    if (!joystickEnabled[joystickId] && !inActionMenu) return;

    Platform.runLater(() -> {
        if (buttonNumber == 0) { // Action button
            if (inActionMenu) {
                actionButtons[selectedButtonIndex].fire();
            } else if ((joystickId == 0 && game.getCurrentPlayer().equals("X")) || 
                      (joystickId == 1 && game.getCurrentPlayer().equals("O"))) {
                handleCellClick(board[selectedRows[joystickId]][selectedCols[joystickId]], 
                              selectedRows[joystickId], selectedCols[joystickId]);
            }
        }
        // Remove the menu toggle button since we're automatically switching now
    });
}


private void updateActionButtonSelection() {
    if (actionButtons == null) return;
    
    for (int i = 0; i < actionButtons.length; i++) {
        if (actionButtons[i] == null) continue;
        
        // First reset all buttons to their original style (remove any previous highlights)
        actionButtons[i].setStyle(actionButtons[i].getStyle().replace(HIGHLIGHT_ADDON, ""));
        
        if (i == selectedButtonIndex) {
            // Add highlight to selected button while preserving original style
            String originalStyle = actionButtons[i].getStyle();
            actionButtons[i].setStyle(originalStyle + HIGHLIGHT_ADDON);
            
            // Keep the animation
            ScaleTransition st = new ScaleTransition(Duration.millis(100), actionButtons[i]);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(1.05);
            st.setToY(1.05);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
        }
    }
}
    public void handleJoystickRelease(int joystickId, int buttonNumber) {
      
    }

    private void updateSelection(int joystickId) {
        
        
         if ("Player vs Computer".equals(gameMode) && joystickId == 1) {
        return;
    }
        Platform.runLater(() -> {
            // Clear previous selection for this player
            for (Button[] row : board) {
                for (Button button : row) {
                    String style = button.getStyle();
                    if (style.contains(playerColors[joystickId])) {
                        style = style.replace("-fx-border-color: " + playerColors[joystickId], "")
                                    .replace("-fx-border-width: 3px", "")
                                    .replace("-fx-border-radius: 5px", "")
                                    .replace(";;", ";")
                                    .trim();
                        if (style.isEmpty()) {
                            style = "-fx-background-color: #AA44CC; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold; -fx-border-radius: 10;";
                        }
                        button.setStyle(style);
                    }
                }
            }
            
            // Highlight new selection
            Button selected = board[selectedRows[joystickId]][selectedCols[joystickId]];
            String baseStyle = "-fx-background-color: #AA44CC; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold; -fx-border-radius: 10;";
            selected.setStyle(baseStyle + " -fx-border-color: " + playerColors[joystickId] + "; -fx-border-width: 3px; -fx-border-radius: 5px;");
        });
    }

    public void setGameSettings(String playerX, String playerO, String mode, String difficulty) {
        player1Label.setText(playerX);
        player2Label.setText(playerO);
        this.playerXName = playerX;
        this.playerOName = playerO;
        this.gameMode = mode;
        this.difficultyLevel = difficulty;
        game.setDifficulty(difficulty);
    }

    private void setupUI() {
        ticLabel.setText("Tic.");
        tacLabel.setText("Tac.");
        toeLabel.setText("Toe.");
        winnerLabel.setVisible(false);
        player1Label.setText(playerXName);
        player2Label.setText(playerOName);
    }

    private void createGameBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button cell = new Button();
                cell.setMinSize(100, 100);
                cell.setStyle("-fx-background-color: #AA44CC; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold; -fx-border-radius: 10;");

                int finalRow = row, finalCol = col;
                cell.setOnAction(e -> handleCellClick(cell, finalRow, finalCol));

                gameBoard.add(cell, col, row);
                board[row][col] = cell;
            }
        }
    }

    private void handleCellClick(Button cell, int row, int col) {
        if (game.isGameOver() || !cell.getText().isEmpty()) return;

        animateButton(cell);
        cell.setText(game.getCurrentPlayer());

        if (game.makeMove(row, col)) {
            highlightWinningLine();
            showWinnerLabel();
            showConfettiEffect();
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
            return;
        }

        if (game.isGameOver()) {
            showDrawMessage();
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
            return;
        }

        if ("Player vs Computer".equals(gameMode)) {
            makeComputerMove();
        }
    }

    private void makeComputerMove() {
        int[] move = game.getComputerMove(difficultyLevel);
        if (move == null) return;

        int row = move[0];
        int col = move[1];
        Button cell = board[row][col];
        
        animateButton(cell);
        cell.setText(game.getCurrentPlayer());

        if (game.makeMove(row, col)) {
            highlightWinningLine();
            showWinnerLabel();
            showConfettiEffect();
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
        } else if (game.isGameOver()) {
            showDrawMessage();
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
        }
    }

    private void animateButton(Button button) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void highlightWinningLine() {
        for (int i = 0; i < 3; i++) {
            if (checkLine(game.getCurrentPlayer(), board[i][0], board[i][1], board[i][2])) {
                glowEffect(board[i][0], board[i][1], board[i][2]);
                return;
            }
            if (checkLine(game.getCurrentPlayer(), board[0][i], board[1][i], board[2][i])) {
                glowEffect(board[0][i], board[1][i], board[2][i]);
                return;
            }
        }

        if (checkLine(game.getCurrentPlayer(), board[0][0], board[1][1], board[2][2])) {
            glowEffect(board[0][0], board[1][1], board[2][2]);
        } else if (checkLine(game.getCurrentPlayer(), board[0][2], board[1][1], board[2][0])) {
            glowEffect(board[0][2], board[1][1], board[2][0]);
        }
    }

    private boolean checkLine(String player, Button b1, Button b2, Button b3) {
        return player.equals(b1.getText()) && player.equals(b2.getText()) && player.equals(b3.getText());
    }

    private void glowEffect(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #33CC33; -fx-border-color: yellow; -fx-border-width: 5;");
        }
    }

 private void showWinnerLabel() {
    String winnerSymbol = game.getCurrentPlayer();
    String winnerName = winnerSymbol.equals("X") ? playerXName : playerOName;

    if ("Player vs Computer".equals(gameMode) && "Computer⚡".equals(winnerName)) {
        winnerLabel.setText("😢 You Lost! Better luck next time!");
        ticLabel.setText("💔");
        tacLabel.setText("Oops,");
        toeLabel.setText("Try Again!");
        MusicController.playSound("lose");
    } else {
        winnerLabel.setText("🎉 Winner: " + winnerName + "! 🎉");
        ticLabel.setText("🎊");
        tacLabel.setText("Congrats,");
        toeLabel.setText(winnerName + "! 🎊");
        MusicController.playSound("win");
    }

    winnerLabel.setVisible(true);
    animateWinnerLabel();
    
    // Automatically switch to action menu when game ends
    inActionMenu = true;
    selectedButtonIndex = 0;
    updateActionButtonSelection();
    
    // Disable board navigation
    joystickEnabled[0] = false;
    joystickEnabled[1] = false;
}

    private void animateWinnerLabel() {
        winnerAnimation = new TranslateTransition(Duration.millis(500), winnerLabel);
        winnerAnimation.setFromY(-80);
        winnerAnimation.setToY(20);
        winnerAnimation.setCycleCount(Animation.INDEFINITE);
        winnerAnimation.setAutoReverse(true);
        winnerAnimation.play();
    }

    private void showConfettiEffect() {
        for (int i = 0; i < 15; i++) {
            Button confetti = new Button(getRandomEmoji());
            confetti.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700; -fx-font-size: 20px;");
            confetti.setTranslateX(Math.random() * 400 - 200);
            confetti.setTranslateY(Math.random() * 200 - 100);
            gameBoard.getChildren().add(confetti);
            animateConfetti(confetti);
        }
    }

    private String getRandomEmoji() {
        String[] emojis = {"🎉", "✨", "🎊", "🎶", "⭐"};
        return emojis[(int) (Math.random() * emojis.length)];
    }

    private void animateConfetti(Button confetti) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(1000), confetti);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.5);
        scale.setToY(0.5);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }


private void showDrawMessage() {
    winnerLabel.setText("🤝 It's a Draw! 🤝");
    winnerLabel.setVisible(true);
    animateWinnerLabel();
    
    // Automatically switch to action menu when game ends
    inActionMenu = true;
    selectedButtonIndex = 0;
    updateActionButtonSelection();
    
    // Disable board navigation
    joystickEnabled[0] = false;
    joystickEnabled[1] = false;
}

private void resetGame() {
    if (winnerAnimation != null) {
        winnerAnimation.stop();
    }
    game.resetBoard();
    winnerLabel.setVisible(false);
    ticLabel.setText("Tic.");
    tacLabel.setText("Tac.");
    toeLabel.setText("Toe.");
    
    // Reset navigation state
    joystickEnabled[0] = true;
    joystickEnabled[1] = true;
    inActionMenu = false;  // This is the key line - we're no longer in action menu
    
    // Reset button styles by removing highlights
    for (Button button : actionButtons) {
        if (button != null) {
            String currentStyle = button.getStyle();
            button.setStyle(currentStyle.replace(HIGHLIGHT_ADDON, ""));
        }
    }
    
    // Recreate the game board
    gameBoard.getChildren().clear();
    createGameBoard();
    updateSelection(0);
    updateSelection(1);
    
    // Reset the selected button index for next time
    selectedButtonIndex = 0;
}

    @FXML
    private void switchToScoreBoardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoreBoardUI.fxml"));
            Parent newRoot = loader.load();
            ScoreBoardController scoreController = loader.getController();
            scoreController.setGameData(playerXName, playerOName, 
                game.getPlayerXScore(), game.getPlayerOScore(), 
                game.getDrawScore(), gameMode, difficultyLevel);
            Stage stage = (Stage) finishButton.getScene().getWindow();
            stage.setScene(new Scene(newRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToPlayerNameScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
            Parent newRoot = loader.load();
           
            Stage stage = (Stage) backbutton.getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

@Override
public boolean requiresSecondJoystick() {
    return !"Player vs Computer".equals(gameMode);
}
} 