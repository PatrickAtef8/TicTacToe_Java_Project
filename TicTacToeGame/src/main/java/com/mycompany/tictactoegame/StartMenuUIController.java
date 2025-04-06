package com.mycompany.tictactoegame;

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
    
    // Joystick navigation variables
    private Button[] buttons;
    private int selectedButtonIndex = 0;
    private boolean joystickEnabled = true;
 // Vibrant glow border highlight (matches both buttons)
private static final String HIGHLIGHT_ADDON = 
    "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +  // Gold gradient
    "-fx-border-width: 3px;" +
    "-fx-border-radius: 25px;" +  // Matches both buttons' 25px radius
    "-fx-border-style: solid outside;" +
    "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";  // Gold glow

    @FXML
    public void initialize() {
        
        
    }
    
    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
     
    }

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) {
    
    }

    private void updateSelection() {
     
    }

    @FXML
    private void switchToGameModeUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelectionUI.fxml"));
            Parent root = loader.load();
            
            JoystickControllable controller = loader.getController();
            App.initializeJoysticks(controller);

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
        System.exit(0);
    }

    @Override
    public boolean requiresSecondJoystick() {
        return false; // Start menu only needs one joystick
    }
}