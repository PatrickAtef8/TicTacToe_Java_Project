package com.mycompany.tictactoegame;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoardController 
{
    @FXML
    private Label ticLabel, tacLabel, toeLabel, player1Label, player2Label, winnerLabel;
    @FXML
    private GridPane gameBoard;
    @FXML
    private Button playAgainButton, finishButton, backbutton;
    private String playerXName = "Player X";
    private String playerOName = "Player O"; 

    private GameLogic game;
    private Button[][] board = new Button[3][3];
    private TranslateTransition winnerAnimation;
    private String gameMode;
    private String difficultyLevel;


    public void initialize() {
        game = new GameLogic();
        setupUI();
        createGameBoard();
        playAgainButton.setOnAction(e -> resetGame());
        finishButton.setOnAction(e -> switchToSecondaryScene());
    }
    

   public void setGameSettings(String playerX, String playerO, String mode, String difficulty) {
    player1Label.setText(playerX);
    player2Label.setText(playerO);
    this.playerXName = playerX;
    this.playerOName = playerO;
    this.gameMode = mode;
    this.difficultyLevel = difficulty;

    System.out.println("Game Mode: " + mode + ", Difficulty: " + difficulty);
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
        return;
    }

    if (game.isGameOver()) {
        showDrawMessage();
        return;
    }

    // Handle computer move if in Player vs Computer mode
    if ("Player vs Computer".equals(gameMode)) {
        makeComputerMove();
    }
}
private void makeComputerMove() {
    int[] move = game.getComputerMove(difficultyLevel); // Get move based on difficulty
    if (move == null) return; // No valid move available

    int row = move[0];
    int col = move[1];

    Button cell = board[row][col];
    animateButton(cell);
    cell.setText(game.getCurrentPlayer());

    if (game.makeMove(row, col)) {
        highlightWinningLine();
        showWinnerLabel();
        showConfettiEffect();
        return;
    }

    if (game.isGameOver()) {
        showDrawMessage();
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
        private boolean checkWinner(String player) {
        for (int i = 0; i < 3; i++) {
            if (checkLine(player, board[i][0], board[i][1], board[i][2]) ||
                checkLine(player, board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }
        return checkLine(player, board[0][0], board[1][1], board[2][2]) || 
               checkLine(player, board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(String player, Button b1, Button b2, Button b3) {
        return player.equals(b1.getText()) && player.equals(b2.getText()) && player.equals(b3.getText());
    }

    private boolean isBoardFull() {
        for (Button[] row : board) {
            for (Button cell : row) {
                if (cell.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
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

    private void glowEffect(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #33CC33; -fx-border-color: yellow; -fx-border-width: 5;");
        }
    }

private void showWinnerLabel() {
    String winnerSymbol = game.getCurrentPlayer();
    String winnerName = winnerSymbol.equals("X") ? playerXName : playerOName;

    if (gameMode.equals("Player vs Computer") && winnerName.equals("🤖Computer⚡")) {
        // Computer Wins -> Show Losing Message
        winnerLabel.setText("😢 You Lost! Better luck next time!");
        ticLabel.setText("💔");
        tacLabel.setText("Oops,");
        toeLabel.setText("Try Again!");
    } else {
        // Normal Winner Message
        winnerLabel.setText("🎉 Winner: " + winnerName + "! 🎉");
        ticLabel.setText("🎊");
        tacLabel.setText("Congrats,");
        toeLabel.setText(winnerName + "! 🎊");
    }

    winnerLabel.setVisible(true);

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

        winnerAnimation = new TranslateTransition(Duration.millis(500), winnerLabel);
        winnerAnimation.setFromY(-80);
        winnerAnimation.setToY(40);
        winnerAnimation.setCycleCount(Animation.INDEFINITE);
        winnerAnimation.setAutoReverse(true);
        winnerAnimation.play();
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
        gameBoard.getChildren().clear();
        createGameBoard();
    }

    @FXML
    private void switchToSecondaryScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoreBoardUI.fxml"));
            Parent newRoot = loader.load();
            ScoreBoardController scoreController = loader.getController();
            scoreController.setGameData(playerXName, playerOName, game.getPlayerXScore(), game.getPlayerOScore(), game.getDrawScore() , gameMode , difficultyLevel);
            Stage stage = (Stage) finishButton.getScene().getWindow();
            stage.setScene(new Scene(newRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToPlayerNameScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerName.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) backbutton.getScene().getWindow();
            stage.setScene(new Scene(newRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

