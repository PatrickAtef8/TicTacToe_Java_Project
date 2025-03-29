package com.mycompany.tictactoegame;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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

    
    private String player1Name;
    private String player2Name;

 public void setPlayerData(String player1, String player2, int player1Score, int player2Score, int drawCount) {
    player1ScoreLabel.setText(player1 + ": " + player1Score); // Name + Score
    player2ScoreLabel.setText(player2 + ": " + player2Score); // Name + Score
    drawScoreLabel.setText("Draws" + ": " + drawCount);

    
        this.player1Name = player1; 
        this.player2Name = player2;

    if (player1Score > player2Score) {
        winnerLabel.setText(player1 + " is Winning! 🏆");
    } else if (player2Score > player1Score) {
        winnerLabel.setText(player2 + " is Winning! 🏆");
    } else {
        winnerLabel.setText("It's a Tie! 🤝");
    }
}


    @FXML
    private void switchToPrimaryScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();
              GameBoardController gameController = loader.getController();
              gameController.setPlayerNames(player1Name, player2Name); 
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