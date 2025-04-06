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
    @FXML private Button backButton;
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
   

    public void initialize() 
    {
        buttons = new Button[]{playerVsComputerButton, playerVsPlayerButton, backButton};
        
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
    private void selectPlayerVsPlayer() 
    {
        MusicController.playSound(MusicController.SOUND_CLICK);

        gameMode = "Player vs Player";
        switchToNextScreen();
    }

    @FXML
    private void selectPlayerVsComputer() 
    {
        MusicController.playSound(MusicController.SOUND_CLICK);
        gameMode = "Player vs Computer";
        switchToNextScreen();
    }
    
    @FXML
    private void switchBackToStartMenu()
    {
        try 
        {
            MusicController.playSound(MusicController.SOUND_CLICK);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
            Parent root = loader.load();

            // Get controller and initialize joysticks if needed
            StartMenuUIController controller = loader.getController();
            App.initializeJoysticks((JoystickControllable) controller);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Error loading StartMenuUI.fxml");
        }
    }


    private void switchToNextScreen() 
    {
        try 
        {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerNameEntry.fxml"));
            Parent root = loader.load();
            
            PlayerNameEntryController controller = loader.getController();
            controller.setGameMode(gameMode); // Make sure this line is present
            App.initializeJoysticks((JoystickControllable) controller);
            
            Stage stage = (Stage) playerVsPlayerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

 @Override
    public boolean requiresSecondJoystick() 
    {
        return false; // Start menu only needs one joystick
    }
}