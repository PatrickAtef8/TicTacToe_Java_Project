package com.mycompany.mavenproject1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {

    @FXML
    private Label player1NameLabel, player2NameLabel, player1ScoreLabel, player2ScoreLabel, drawScoreLabel;

    public void setPlayerData(String player1Name, String player2Name, int player1Score, int player2Score, int drawCount) {
        if (player1NameLabel != null && player2NameLabel != null && player1ScoreLabel != null && player2ScoreLabel != null && drawScoreLabel != null) {
            player1NameLabel.setText("Player 1: " + player1Name);
            player1ScoreLabel.setText(String.valueOf(player1Score));

            player2NameLabel.setText("Player 2: " + player2Name);
            player2ScoreLabel.setText(String.valueOf(player2Score));

            drawScoreLabel.setText("Draws: " + drawCount);
        }
    }
}
