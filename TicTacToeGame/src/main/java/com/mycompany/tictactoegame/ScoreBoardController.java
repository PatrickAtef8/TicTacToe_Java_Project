package com.mycompany.tictactoegame;

import javafx.application.Platform;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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

    
private JoystickReader joystickReader;
private boolean selectingYes = true; // true = yesButton, false = noButton

    
    private String player1Name;
    private String player2Name;

 private String gameMode;
private String difficultyLevel;

public void setGameData(String player1, String player2, int player1Score, int player2Score, int drawCount, String mode, String difficulty) {
    this.player1Name = player1;
    this.player2Name = player2;
    this.gameMode = mode;
    this.difficultyLevel = difficulty;

    player1ScoreLabel.setText(player1 + ": " + player1Score);
    player2ScoreLabel.setText(player2 + ": " + player2Score);
    drawScoreLabel.setText("Draws: " + drawCount);

    if (player1Score > player2Score) {
        winnerLabel.setText(player1 + " is Winning! 🏆");
    } else if (player2Score > player1Score) {
        winnerLabel.setText(player2 + " is Winning! 🏆");
    } else {
        winnerLabel.setText("It's a Tie! 🤝");
    }
}

public void initialize() {
    Platform.runLater(this::startJoystickNavigation);
}

public void handleJoystickMove(int joystickId, int axis, int value) {
    double threshold = 20000; // Adjust as needed

    // X-axis usually is axis 0
    if (axis == 0) {
        if (value > threshold || value < -threshold) {
            selectingYes = !selectingYes;
            updateButtonFocus();
            try {
                Thread.sleep(150); // prevent rapid toggling
            } catch (InterruptedException ignored) {}
        }
    }
}

public void handleJoystickPress(int joystickId, int button) {
    // Button 0 is usually the "A" or main action button
    if (button == 0) {
        Platform.runLater(() -> {
            if (selectingYes) {
                switchToPrimaryScene();
            } else {
                exitApplication();
            }
        });
    }
}



public void startJoystickNavigation() {
    String devicePath = "/dev/input/js0"; // Adjust if needed
    int joystickId = 0;

    joystickReader = new JoystickReader(this, devicePath, joystickId);
    new Thread(joystickReader).start();
}






private DropShadow createGlowEffect() {
    DropShadow glow = new DropShadow();
    glow.setColor(Color.PURPLE);
    glow.setRadius(20);
    glow.setSpread(0.5);
    return glow;
}


private void updateButtonFocus() {
    Platform.runLater(() -> {
        if (selectingYes) {
            yesButton.setEffect(createGlowEffect());
            noButton.setEffect(null);
        } else {
            noButton.setEffect(createGlowEffect());
            yesButton.setEffect(null);
        }
    });
}




    @FXML
    private void switchToPrimaryScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();
              GameBoardController gameController = loader.getController();
              gameController.setGameSettings(player1Name, player2Name, gameMode, difficultyLevel);
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
    
    public void stopJoystick() {
    if (joystickReader != null) {
        joystickReader.stop();
    }
}

}

