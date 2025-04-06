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
    public void initialize() 
    {  
        buttons = new Button[]{startButton, quitButton};
        
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
            System.err.println("Joystick initialization failed: " + e.getMessage());
        }
    }
    
    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) 
    {
        if (!joystickEnabled) return;

        Platform.runLater(() -> {
            if (axisNumber == 5 || axisNumber == 1) 
            { 
                if (value == 32769 && selectedButtonIndex > 0) 
                {
                    selectedButtonIndex--;
                    updateSelection();
                } 
                else if (value == 32767 && selectedButtonIndex < buttons.length - 1) 
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
            // Reset all buttons to original style
            for (Button button : buttons) 
            {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            
            // Highlight selected button
            Button selected = buttons[selectedButtonIndex];
            String original = (String)selected.getProperties().get("originalStyle");
            selected.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
            selected.requestFocus();
        });
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
    public boolean requiresSecondJoystick() 
    {
        return false; // Start menu only needs one joystick
    }
}