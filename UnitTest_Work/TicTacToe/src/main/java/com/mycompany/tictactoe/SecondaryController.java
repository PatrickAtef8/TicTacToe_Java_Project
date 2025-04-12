package com.mycompany.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SecondaryController {
    @FXML private Label xScoreLabel;
    @FXML private Label oScoreLabel;
    @FXML private Button restartButton;

    private PrimaryController primaryController;

    public void setScores(int xScore, int oScore) {
        xScoreLabel.setText("X Score: " + xScore);
        oScoreLabel.setText("O Score: " + oScore);
    }

    public void setPrimaryController(PrimaryController controller) {
        this.primaryController = controller;
    }

    @FXML
    private void restartGame() {
        if (primaryController != null) {
            primaryController.resetGame();
            ((Stage) restartButton.getScene().getWindow()).close();
        }
    }
}
