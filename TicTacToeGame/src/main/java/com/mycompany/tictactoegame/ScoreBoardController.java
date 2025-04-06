package com.mycompany.tictactoegame;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ScoreBoardController implements JoystickControllable {

    @FXML private Label winnerLabel;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    @FXML private Label drawScoreLabel;
    @FXML private Button yesButton, noButton, homeButton;
    
    private String player1Name;
    private String player2Name;
    private String gameMode;
    private String difficultyLevel;
    
    // Joystick control variables
    private Button[] buttons;
    private int selectedButtonIndex = 0;
    private boolean joystickEnabled = true;
    private static final String HIGHLIGHT_ADDON = 
        "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +
        "-fx-border-width: 3px;" +
        "-fx-border-radius: 20px;" +
        "-fx-border-style: solid outside;" +
        "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";

    @FXML
    public void initialize() {
        buttons = new Button[]{yesButton, noButton, homeButton};
        
        // Store original styles
        for (Button button : buttons) {
            button.getProperties().put("originalStyle", button.getStyle());
        }
        
        updateSelection();
        
        try {
            App.initializeJoysticks(this);
        } catch (Exception e) {
            joystickEnabled = false;
        }
    }

    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        if (!joystickEnabled) return;

        Platform.runLater(() -> {
            if (axisNumber == 5 || axisNumber == 1){ // UP/DOWN axis
                if (value == 32769 && selectedButtonIndex > 0) {
                    selectedButtonIndex--;
                    updateSelection();
                } 
                else if (value == 32767 && selectedButtonIndex < buttons.length - 1) {
                    selectedButtonIndex++;
                    updateSelection();
                }
            }
            else if (axisNumber == 4 || axisNumber == 0){ // LEFT/RIGHT axis
                if (value == 32769 && selectedButtonIndex > 0) {
                    selectedButtonIndex--;
                    updateSelection();
                } 
                else if (value == 32767 && selectedButtonIndex < buttons.length - 1) {
                    selectedButtonIndex++;
                    updateSelection();
                }
            }
        });
    }

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) {
        if (!joystickEnabled) return;
        Platform.runLater(() -> {
            if (buttonNumber == 0) { // Primary action button
                buttons[selectedButtonIndex].fire();
            }
        });
    }

    private void updateSelection() {
        Platform.runLater(() -> {
            // Restore original style for all buttons
            for (Button button : buttons) {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            
            // Add highlight to selected button
            Button selected = buttons[selectedButtonIndex];
            String original = (String)selected.getProperties().get("originalStyle");
            selected.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
            selected.requestFocus();
        });
    }

    @Override
    public boolean requiresSecondJoystick() {
        return false; // Scoreboard only needs one joystick
    }

    public void setGameData(String player1, String player2, int player1Score, 
                          int player2Score, int drawCount, String mode, String difficulty) {
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
            winnerLabel.setText("     It's a Tie! 🤝");
        }
    }

    @FXML
    private void switchToGameBoardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();
            GameBoardController gameController = loader.getController();
            gameController.setGameSettings(player1Name, player2Name, gameMode, difficultyLevel);
            App.initializeJoysticks(gameController);

            Stage stage = (Stage) yesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    @FXML
    private void goHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
            Parent root = loader.load();
            App.initializeJoysticks(loader.getController());
            
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading StartMenuUI.fxml");
        }
    }
}