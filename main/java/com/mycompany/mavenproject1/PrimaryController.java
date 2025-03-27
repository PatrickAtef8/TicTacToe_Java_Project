package com.mycompany.mavenproject1;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Random;
import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PrimaryController {

    @FXML
    private Label ticLabel, tacLabel, toeLabel, player1Label, player2Label, drawLabel;
    
    @FXML
    private Label player1ScoreLabel, player2ScoreLabel, drawScoreLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Button playAgainButton, finishButton;

    private String currentPlayer = "X";
    private Button[][] board = new Button[3][3];
    private boolean gameOver = false;
    private int player1Score = 0, player2Score = 0, drawCount = 0;

    public void initialize() {
        setupUI();
        createGameBoard();
        playAgainButton.setOnAction(e -> resetGame());
        finishButton.setOnAction(e -> switchToSecondaryScene());
    }

    private void setupUI() {
        ticLabel.setText("Tic.");
        tacLabel.setText("Tac.");
        toeLabel.setText("Toe.");

        player1Label.setText("Player X");
        player2Label.setText("Player O");
        drawLabel.setText("Draw");

        updateScores();
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
            if (currentPlayer.equals("X")) player1Score++;
            else player2Score++;

            updateScores();
            gameOver = true;
            highlightWinningLine();
            showConfettiEffect();
            return;
        }

        if (isBoardFull()) {
            drawCount++;
            updateScores();
            gameOver = true;
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

    // ✨ **Highlight Winning Line** ✨

// ✨ **Highlight Winning Buttons** ✨
private void glowEffect(Button... buttons) {
    for (Button button : buttons) {
        button.setStyle(button.getStyle() + "; -fx-border-color: yellow; -fx-border-width: 5;");
    }
}
private void highlightWinningLine() {
    // Check for horizontal, vertical, and diagonal wins
    for (int i = 0; i < 3; i++) {
        if (checkLine(currentPlayer, board[i][0], board[i][1], board[i][2])) {
            glowEffect(board[i][0], board[i][1], board[i][2]); // Highlight buttons
            colorWinningBoxes(board[i][0], board[i][1], board[i][2]); // Color the winning boxes
            return;
        }
        if (checkLine(currentPlayer, board[0][i], board[1][i], board[2][i])) {
            glowEffect(board[0][i], board[1][i], board[2][i]);
            colorWinningBoxes(board[0][i], board[1][i], board[2][i]);
            return;
        }
    }

    // Check for diagonal wins
    if (checkLine(currentPlayer, board[0][0], board[1][1], board[2][2])) {
        glowEffect(board[0][0], board[1][1], board[2][2]);
        colorWinningBoxes(board[0][0], board[1][1], board[2][2]);
    } else if (checkLine(currentPlayer, board[0][2], board[1][1], board[2][0])) {
        glowEffect(board[0][2], board[1][1], board[2][0]);
        colorWinningBoxes(board[0][2], board[1][1], board[2][0]);
    }
}

private void colorWinningBoxes(Button... buttons) {
    for (Button button : buttons) {
        button.setStyle(button.getStyle() + "; -fx-background-color: green;");
    }
}



    // 🎉 **Confetti Celebration** 🎉
    private void showConfettiEffect() {
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            Button confetti = new Button("🎉");
            confetti.setStyle("-fx-background-color: transparent; -fx-text-fill: #DDA0DD; -fx-font-size: 20px;");
            confetti.setTranslateX(random.nextInt(400) - 200);
            confetti.setTranslateY(random.nextInt(200) - 100);

            Platform.runLater(() -> gameBoard.getChildren().add(confetti));

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> gameBoard.getChildren().remove(confetti));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void updateScores() {
        player1ScoreLabel.setText(String.valueOf(player1Score));
        player2ScoreLabel.setText(String.valueOf(player2Score));
        drawScoreLabel.setText(String.valueOf(drawCount));
    }

    private void resetGame() {
        gameOver = false;
        currentPlayer = "X";
        for (Button[] row : board) {
            for (Button cell : row) {
                cell.setText("");
                cell.setStyle("-fx-background-color: #AA44CC; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");
            }
        }
    }

    // 🔄 **Scene Switching to Secondary Scene**
    private void switchToSecondaryScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();

            SecondaryController secondaryController = loader.getController();
            if (secondaryController != null) {
                secondaryController.setPlayerData(player1Label.getText(), player2Label.getText(), player1Score, player2Score, drawCount);
            } else {
                System.out.println("Error: SecondaryController is null!");
            }

            Stage stage = (Stage) finishButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





