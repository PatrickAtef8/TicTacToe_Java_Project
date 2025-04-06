package com.mycompany.tictactoegame.controllers;

import com.mycompany.tictactoegame.App;
import com.mycompany.tictactoegame.utils.MusicController;
import com.mycompany.tictactoegame.utils.input.JoystickControllable;
import com.mycompany.tictactoegame.utils.input.JoystickManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.Stage;
import javafx.application.Platform;

public class StartMenuUIController implements JoystickControllable {
    @FXML private Button startButton;
    @FXML private Button quitButton;
    @FXML private Button muteButton;
    
    // Joystick navigation variables
    private Button[] buttons;
    private int selectedButtonIndex = 0;
    private boolean joystickEnabled = true;
    
    private static final String HIGHLIGHT_ADDON = 
        "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +
        "-fx-border-width: 3px;" +
        "-fx-border-radius: 25px;" +
        "-fx-border-style: solid outside;" +
        "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";

    public void initialize() {
        buttons = new Button[]{startButton, quitButton, muteButton};
        
        // Store original styles
        for (Button button : buttons) {
            button.getProperties().put("originalStyle", button.getStyle());
        }
        
        updateSelection();
        try {
            App.initializeJoysticks(this);
        } catch (Exception e) {
            joystickEnabled = false;
            System.err.println("Joystick initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        if (!joystickEnabled) return;

        Platform.runLater(() -> {
            if (axisNumber == 5 || axisNumber == 1) { 
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
            if (buttonNumber == 0) {
                buttons[selectedButtonIndex].fire();
            }
            else if (buttonNumber == 1 && selectedButtonIndex == 2) {
                MusicController.toggleMute();
                updateMuteButtonText();
            }
        });
    }

    private void updateSelection() {
        Platform.runLater(() -> {
            for (Button button : buttons) {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            
            Button selected = buttons[selectedButtonIndex];
            String original = (String)selected.getProperties().get("originalStyle");
            selected.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
            selected.requestFocus();
        });
    }

    private void updateMuteButtonText() {
        muteButton.setText(MusicController.isMuted() ? "Unmute" : "Mute");
    }

@FXML
private void switchToGameModeUI() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/ModeSelectionUI.fxml"));
        Parent root = loader.load();
        
        // Get controller and update joystick reference
        JoystickControllable controller = (JoystickControllable) loader.getController();
        JoystickManager.updateController(controller);
        
        // Get current stage and set new scene
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        
    } catch (IOException e) {
        System.err.println("Error loading ModeSelectionUI: " + e.getMessage());
        e.printStackTrace();
    }
}

    @FXML
    private void exitApplication() {
                    MusicController.playSound(MusicController.SOUND_CLICK);
        MusicController.cleanup();
        System.exit(0);
    }

    @Override
    public boolean requiresSecondJoystick() {
        return false;
    }

  @FXML
private void toggleMute() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    MusicController.toggleMute();
    updateMuteButton();
}

private void updateMuteButton() {
    if (MusicController.isMuted()) {
        muteButton.setText("🔇 Unmute");
        muteButton.setStyle("-fx-background-color: linear-gradient(to right, #555555, #888888);" +
                           "-fx-text-fill: white; -fx-font-size: 22px; " +
                           "-fx-font-family: 'Comic Sans MS'; -fx-font-weight: bold;" +
                           "-fx-padding: 12px 30px; -fx-background-radius: 25px; " +
                           "-fx-border-radius: 25px;" +
                           "-fx-effect: dropshadow(gaussian, rgba(100,100,100,0.8), 15, 0, 0, 0);");
    } else {
        muteButton.setText("🔈 Mute");
        muteButton.setStyle("-fx-background-color: linear-gradient(to right, #9B59B6, #3498DB);" +
                           "-fx-text-fill: white; -fx-font-size: 22px; " +
                           "-fx-font-family: 'Comic Sans MS'; -fx-font-weight: bold;" +
                           "-fx-padding: 12px 30px; -fx-background-radius: 25px; " +
                           "-fx-border-radius: 25px;" +
                           "-fx-effect: dropshadow(gaussian, rgba(155,89,182,0.8), 15, 0, 0, 0);");
    }
}
}