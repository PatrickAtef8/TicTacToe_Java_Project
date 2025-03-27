package com.mycompany.tictactoegame;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ScoreBoardController {

    @FXML
    private Label winnerLabel;

    @FXML
    private Label player1ScoreLabel;

    @FXML
    private Label player2ScoreLabel;

    @FXML
    private Label drawScoreLabel;

    @FXML
    private Button yesButton, noButton;

    public void setPlayerData(String player1, String player2, int player1Score, int player2Score, int drawCount) {
        player1ScoreLabel.setText(String.valueOf(player1Score));
        player2ScoreLabel.setText(String.valueOf(player2Score));
        drawScoreLabel.setText(String.valueOf(drawCount));

        if (player1Score > player2Score) {
            winnerLabel.setText(player1 + " Wins!");
        } else if (player2Score > player1Score) {
            winnerLabel.setText(player2 + " Wins!");
        } else {
            winnerLabel.setText("It's a Draw!");
        }
    }

    @FXML
    private void switchToPrimaryScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) yesButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }
}
