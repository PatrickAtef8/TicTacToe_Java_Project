package com.mycompany.tictactoegame.controllers;

import com.mycompany.tictactoegame.App;
import com.mycompany.tictactoegame.utils.MusicController;
import com.mycompany.tictactoegame.utils.input.JoystickControllable;
import com.mycompany.tictactoegame.utils.input.JoystickManager;
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
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class ModeSelectionController implements JoystickControllable {
    
    @FXML private Button playerVsPlayerButton;
    @FXML private Button playerVsComputerButton;
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
    private Button backButton;

    public void initialize() 
    {
        buttons = new Button[]{playerVsComputerButton, playerVsPlayerButton,backButton};
        
        // Store original styles
        for (Button button : buttons) 
        {
            button.getProperties().put("originalStyle", button.getStyle());
        }
        
        updateSelection();
        
        try 
        {
            App.initializeJoysticks(this);
        } catch (Exception e) 
        {
            joystickEnabled = false;
        }
    }
    
    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) 
    {
        if (!joystickEnabled) return;

        Platform.runLater(() -> {
            if (axisNumber == 5 || axisNumber == 1) 
            { 
            	// UP/DOWN axis
                if (value == 32769 && selectedButtonIndex > 0) 
                {
                    selectedButtonIndex--;
                    updateSelection();
                } 
                else if (value == 32767 & selectedButtonIndex < buttons.length - 1) 
                {
                    selectedButtonIndex++;
                    updateSelection();
                }
            }
        });
    }

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) 
    {
        if (!joystickEnabled) return;
        Platform.runLater(() -> {
            if (buttonNumber == 0) 
            { 
            	// Primary action button
                buttons[selectedButtonIndex].fire();
            }
        });
    }

    private void updateSelection() 
    {
        Platform.runLater(() -> {
            // Reset all buttons
            for (Button button : buttons) 
            {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            
            // Highlight selected
            Button selected = buttons[selectedButtonIndex];
            String original = (String)selected.getProperties().get("originalStyle");
            selected.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
            selected.requestFocus();
        });
    } 

    @FXML
    private void selectPlayerVsPlayer() {
                    MusicController.playSound(MusicController.SOUND_CLICK);
        gameMode = "Player vs Player";
        switchToNextScreen();
    }

    @FXML
    private void selectPlayerVsComputer() {
                    MusicController.playSound(MusicController.SOUND_CLICK);
        gameMode = "Player vs Computer";
        switchToNextScreen();
    }

private void switchToNextScreen() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/PlayerNameEntry.fxml"));
        Parent root = loader.load();
        
        PlayerNameEntryController controller = loader.getController();
        controller.setGameMode(gameMode);
        
        // Update the joystick controller reference
        JoystickManager.updateController(controller);
        
        // Get the current stage
        Stage stage = (Stage) playerVsPlayerButton.getScene().getWindow();
        Scene scene = new Scene(root);
        
        // Set the new scene
        stage.setScene(scene);
        
        // Optional: If you want to maintain the same window size
        stage.sizeToScene();
        
    } catch (IOException e) {
        e.printStackTrace();
        // Consider showing an error message to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to load screen");
        alert.setContentText("Could not load the player name entry screen.");
        alert.showAndWait();
    }
}
 @Override
    public boolean requiresSecondJoystick() 
    {
        return false; // Start menu only needs one joystick
    }

      
  @FXML
private void goBack() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/StartMenuUI.fxml"));
        Parent root = loader.load();
        
        StartMenuUIController controller = loader.getController();
        controller.equals(root);
        
        // Update joystick controller reference
        JoystickManager.updateController(controller);
        
        // Get current stage and set new scene
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}