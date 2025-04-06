package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class ModeSelectionController implements JoystickControllable {
    @FXML private Button playerVsPlayerButton;
    @FXML private Button playerVsComputerButton;
    @FXML private VBox titleContainer;
    @FXML private Label ticLabel;
    @FXML private Label tacLabel;
    @FXML private Label toeLabel;
    @FXML private Label winnerLabel;

    private String gameMode;
    private int selectedButtonIndex = 0;
    private Button[] buttons;
    private boolean joystickEnabled = true;
    
   private static final String HIGHLIGHT_ADDON = 
    "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +  // Gold gradient
    "-fx-border-width: 3px;" +
    "-fx-border-radius: 20px;" +  // Matches your 20px rounded corners
    "-fx-border-style: solid outside;" +
    "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0), " +
    "            innershadow(gaussian, rgba(255,255,255,0.3), 5, 0.5, 0, 0);";  // Gold glow + inner shine

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
    private void selectPlayerVsPlayer() {
        gameMode = "Player vs Player";
        switchToNextScreen();
    }

    @FXML
    private void selectPlayerVsComputer() {
        gameMode = "Player vs Computer";
        switchToNextScreen();
    }

     @FXML
    private void switchToNextScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerNameEntry.fxml"));
            Parent root = loader.load();
            
            PlayerNameEntryController controller = loader.getController();
            controller.setGameMode(gameMode); // Make sure this line is present
            App.initializeJoysticks((JoystickControllable) controller);
            
            Stage stage = (Stage) playerVsPlayerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean requiresSecondJoystick() {
return false;    }
}