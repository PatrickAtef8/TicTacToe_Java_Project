package com.mycompany.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryController {
    @FXML private Label statusLabel;

    @FXML private Button button00, button01, button02;
    @FXML private Button button10, button11, button12;
    @FXML private Button button20, button21, button22;

    private Button[][] buttons;
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';

    private int xScore = 0;
    private int oScore = 0;

    @FXML
    public void initialize() {
        buttons = new Button[][] {
            { button00, button01, button02 },
            { button10, button11, button12 },
            { button20, button21, button22 }
        };

        resetGame();
    }

    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = -1, col = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        if (row != -1 && col != -1 && board[row][col] == '\0') {
            board[row][col] = currentPlayer;
            button.setText(String.valueOf(currentPlayer));
            button.setDisable(true);

            if (checkWinner(currentPlayer)) {
                statusLabel.setText("Player " + currentPlayer + " wins!");
                
                if (currentPlayer == 'X') xScore++;
                else oScore++;

                disableBoard();
            } else if (isBoardFull()) {
                statusLabel.setText("It's a draw!");
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                statusLabel.setText("Player " + currentPlayer + "'s turn");
            }
        }
    }

    private boolean checkWinner(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '\0') return false;
            }
        }
        return true;
    }

    private void disableBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }

    @FXML
    private void openScores() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoe/secondary.fxml"));
        Parent root = loader.load();
        SecondaryController controller = loader.getController();
        
        controller.setScores(xScore, oScore);
        controller.setPrimaryController(this);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void resetGame() {
        board = new char[3][3];
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");

        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
                button.setDisable(false);
            }
        }
    }
}
