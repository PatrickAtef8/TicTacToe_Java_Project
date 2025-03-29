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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoardController {

    @FXML
    private Label ticLabel, tacLabel, toeLabel, player1Label, player2Label, winnerLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Button playAgainButton, finishButton;

    @FXML
    private VBox titleContainer;
    
        private static int playerXScore = 0;
    private static int playerOScore = 0;
    private static int drawScore = 0;

    
    private String playerXName = "Player X";
    private String playerOName = "Player O"; 

    private String currentPlayer = "X";
    private Button[][] board = new Button[3][3];
    private boolean gameOver = false;
    private TranslateTransition winnerAnimation; // Store the animation so we can stop it
    @FXML
    private Button backbutton;

    public void initialize() {
        setupUI();
        createGameBoard();
        playAgainButton.setOnAction(e -> resetGame());
        finishButton.setOnAction(e -> switchToSecondaryScene());
    }
   
   public void setPlayerNames(String playerX, String playerO) {
    player1Label.setText(playerX);
    player2Label.setText(playerO);
    this.playerXName = playerX;
    this.playerOName = playerO;
}


private void setupUI() {
    ticLabel.setText("Tic.");
    tacLabel.setText("Tac.");
    toeLabel.setText("Toe.");
    player1Label.setText(playerXName);
    player2Label.setText(playerOName);
    winnerLabel.setVisible(false);
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
        if (gameOver || !cell.getText().isEmpty()) return;

        animateButton(cell);
        cell.setText(currentPlayer);

        if (checkWinner(currentPlayer)) {
            gameOver = true;
            highlightWinningLine();
            showWinnerLabel();
            showConfettiEffect();
            
            if (currentPlayer.equals("X")) {
                playerXScore++;
            } else {
                playerOScore++;
            }
            return;
        }

        if (isBoardFull()) {
            gameOver = true;
            showDrawMessage();
            drawScore++;
            return;
        }

        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
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
            if (checkLine(currentPlayer, board[i][0], board[i][1], board[i][2])) {
                glowEffect(board[i][0], board[i][1], board[i][2]);
                return;
            }
            if (checkLine(currentPlayer, board[0][i], board[1][i], board[2][i])) {
                glowEffect(board[0][i], board[1][i], board[2][i]);
                return;
            }
        }

        if (checkLine(currentPlayer, board[0][0], board[1][1], board[2][2])) {
            glowEffect(board[0][0], board[1][1], board[2][2]);
        } else if (checkLine(currentPlayer, board[0][2], board[1][1], board[2][0])) {
            glowEffect(board[0][2], board[1][1], board[2][0]);
        }
    }

    private void glowEffect(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-background-color: #33CC33; -fx-border-color: yellow; -fx-border-width: 5;");
        }
    }

private void showWinnerLabel() {
    winnerLabel.setText("🎉 Winner: " + currentPlayer + "! 🎉");
    winnerLabel.setVisible(true);

    // Show a fun congratulatory message
    ticLabel.setText("🎊");
    tacLabel.setText("Congrats,");
    toeLabel.setText(currentPlayer + "! 🎊");

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
    gameOver = false;
    currentPlayer = "X";
    winnerLabel.setVisible(false);

    // Restore original Tic Tac Toe text
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
        scoreController.setPlayerData(playerXName, playerOName, playerXScore, playerOScore, drawScore);            
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