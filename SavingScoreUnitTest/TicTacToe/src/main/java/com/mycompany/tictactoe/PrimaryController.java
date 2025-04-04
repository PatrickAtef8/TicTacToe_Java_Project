package com.mycompany.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.application.Platform;

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
    
    private int[] selectedRows = {0, 0}; // [player1, player2]
    private int[] selectedCols = {0, 0};
    private boolean[] joystickEnabled = {true, true};
    private String[] playerColors = {"#3498db", "#e74c3c"}; // Blue for P1, Red for P2
@FXML
public void initialize() {
    buttons = new Button[][] {
        {button00, button01, button02},
        {button10, button11, button12},
        {button20, button21, button22}
    };
    
    // Remove focus highlights from all buttons
    for (Button[] row : buttons) {
        for (Button button : row) {
            button.setFocusTraversable(false);
            button.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        }
    }
    
    resetGame();
}

    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        if (!joystickEnabled[joystickId]) return;

        Platform.runLater(() -> {
            if (axisNumber == 5) { // UP/DOWN
                if (value == 32769 && selectedRows[joystickId] > 0) {
                    selectedRows[joystickId]--;
                } 
                else if (value == 32767 && selectedRows[joystickId] < 2) {
                    selectedRows[joystickId]++;
                }
                updateSelection(joystickId);
            } 
            else if (axisNumber == 4) { // LEFT/RIGHT
                if (value == 32769 && selectedCols[joystickId] > 0) {
                    selectedCols[joystickId]--;
                }
                else if (value == 32767 && selectedCols[joystickId] < 2) {
                    selectedCols[joystickId]++;
                }
                updateSelection(joystickId);
            }
        });
    }

    public void handleJoystickPress(int joystickId, int buttonNumber) {
        if (!joystickEnabled[joystickId]) return;

        Platform.runLater(() -> {
            if (buttonNumber == 0) { // Action button
                if ((joystickId == 0 && currentPlayer == 'X') || 
                    (joystickId == 1 && currentPlayer == 'O')) {
                    makeMove(selectedRows[joystickId], selectedCols[joystickId]);
                }
            }
            else if (buttonNumber == 1) { // Reset button
                resetGame();
            }
        });
    }

    public void handleJoystickRelease(int joystickId, int buttonNumber) {
        // Available for future use
    }

    private void updateSelection(int joystickId) {
    Platform.runLater(() -> {
        // Clear only joystick highlights (preserve any other styles)
        for (Button[] row : buttons) {
            for (Button button : row) {
                String style = button.getStyle();
                if (style.contains(playerColors[joystickId])) {
                    // Remove only our highlight styles
                    style = style.replace("-fx-border-color: " + playerColors[joystickId], "")
                                .replace("-fx-border-width: 3px", "")
                                .replace("-fx-border-radius: 5px", "")
                                .replace(";;", ";")
                                .trim();
                    button.setStyle(style.isEmpty() ? "" : style);
                }
            }
        }
        
        // Apply new highlight only if coming from joystick
        if (joystickId >= 0) { // -1 could mean keyboard
            Button selected = buttons[selectedRows[joystickId]][selectedCols[joystickId]];
            selected.setStyle("-fx-border-color: " + playerColors[joystickId] + 
                            "; -fx-border-width: 3px; -fx-border-radius: 5px;");
        }
    });
}

@FXML
private void handleButtonClick(javafx.event.ActionEvent event) {
    Button button = (Button) event.getSource();
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (buttons[i][j] == button) {
                // Clear any joystick highlights before mouse/keyboard move
                updateSelection(-1); // -1 indicates keyboard/mouse
                makeMove(i, j);
                return;
            }
        }
    }
}
    
    private void makeMove(int row, int col) {
        if (board[row][col] != '\0') return;
        
        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));
        buttons[row][col].setDisable(true);

        if (checkWinner(currentPlayer)) {
            statusLabel.setText("Player " + currentPlayer + " wins!");
            if (currentPlayer == 'X') xScore++;
            else oScore++;
            disableBoard();
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
        } else if (isBoardFull()) {
            statusLabel.setText("It's a draw!");
            joystickEnabled[0] = false;
            joystickEnabled[1] = false;
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s turn");
        }
        updateSelection(0);
        updateSelection(1);
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
        joystickEnabled[0] = true;
        joystickEnabled[1] = true;
        selectedRows = new int[]{0, 0};
        selectedCols = new int[]{0, 0};

        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText("");
                button.setDisable(false);
                button.setStyle("");
            }
        }
        updateSelection(0);
        updateSelection(1);
    }
}