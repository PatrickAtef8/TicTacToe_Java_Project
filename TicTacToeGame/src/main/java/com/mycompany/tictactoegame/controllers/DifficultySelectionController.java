package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DifficultySelectionController implements JoystickControllable {
    @FXML private Button easyButton;
    @FXML private Button mediumButton;
    @FXML private Button hardButton;
    @FXML private Button backButton;
    @FXML private Button homeButton;
    @FXML private Label ticLabel;
    @FXML private Label tacLabel;
    @FXML private Label toeLabel;
    @FXML private Label winnerLabel;
    
    private Button[] difficultyButtons;
    private Button[] navButtons;
    private boolean isInNavMode = false;

    
    
    

    private String playerX, playerO;
    private String gameMode;
    private int selectedButtonIndex = 0;
    private Button[] buttons;
    private boolean joystickEnabled = true;
    
    // Modern game border highlight (clean and minimal)
// Modern gradient border highlight that matches your button style
private static final String HIGHLIGHT_ADDON = 
    "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +  // Gold/yellow gradient
    "-fx-border-width: 3px;" +
    "-fx-border-radius: 20px;" +  // Matches your button's rounded corners
    "-fx-border-style: solid outside;" +
    "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";  // Gold glow
 
    @FXML
    public void initialize() {
        difficultyButtons = new Button[]{easyButton, mediumButton, hardButton};
        navButtons = new Button[]{backButton, homeButton};
        
        // Store original styles
        for (Button button : difficultyButtons) {
            button.getProperties().put("originalStyle", button.getStyle());
        }
        for (Button button : navButtons) {
            button.getProperties().put("originalStyle", button.getStyle());
        }
        
        updateSelection();
        
        try {
            App.initializeJoysticks(this);
        } catch (Exception e) {
            joystickEnabled = false;
            System.err.println("Joystick initialization failed: " + e.getMessage());
        }
    }

    @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        if (!joystickEnabled) return;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (axisNumber == 5 || axisNumber == 1) { // Vertical axis
                    if (value == 32769) { // Up
                        if (isInNavMode) {
                            // Move from nav buttons to last difficulty button
                            isInNavMode = false;
                            selectedButtonIndex = difficultyButtons.length - 1;
                        } else if (selectedButtonIndex > 0) {
                            selectedButtonIndex--;
                        }
                        updateSelection();
                    }
                    else if (value == 32767) { // Down
                        if (!isInNavMode && selectedButtonIndex == difficultyButtons.length - 1) {
                            // Move from last difficulty to nav buttons
                            isInNavMode = true;
                            selectedButtonIndex = 0;
                        } else if (!isInNavMode && selectedButtonIndex < difficultyButtons.length - 1) {
                            selectedButtonIndex++;
                        }
                        updateSelection();
                    }
                }
                else if (axisNumber == 4 || axisNumber == 0 && isInNavMode) { // Horizontal axis only in nav mode
                    if (value == 32769 && selectedButtonIndex > 0) { // Left
                        selectedButtonIndex--;
                        updateSelection();
                    }
                    else if (value == 32767 && selectedButtonIndex < navButtons.length - 1) { // Right
                        selectedButtonIndex++;
                        updateSelection();
                    }
                }
            }
        });
    }

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) {
        if (!joystickEnabled) return;
        Platform.runLater(() -> {
            if (buttonNumber == 0) { // Primary action button
                if (isInNavMode) {
                    navButtons[selectedButtonIndex].fire();
                } else {
                    difficultyButtons[selectedButtonIndex].fire();
                }
            }
        });
    }

    private void updateSelection() {
        Platform.runLater(() -> {
            // Reset all buttons
            for (Button button : difficultyButtons) {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            for (Button button : navButtons) {
                String original = (String)button.getProperties().get("originalStyle");
                button.setStyle(original != null ? original : "");
            }
            
            // Highlight selected button
            Button selected = isInNavMode ? 
                navButtons[selectedButtonIndex] : 
                difficultyButtons[selectedButtonIndex];
                
            String original = (String)selected.getProperties().get("originalStyle");
            selected.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
            selected.requestFocus();
        });
    }


    public void setPlayerNames(String playerX, String playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    @FXML 
    private void selectEasy() { 
        MusicController.playSound(MusicController.SOUND_CLICK);
        switchToGameBoard("Easy"); }
    
    @FXML 
    private void selectMedium() { 
        MusicController.playSound(MusicController.SOUND_CLICK);
        switchToGameBoard("Medium"); }
    
    @FXML 
    private void selectHard() { 
        MusicController.playSound(MusicController.SOUND_CLICK);
        switchToGameBoard("Hard"); }

 private void switchToGameBoard(String difficulty) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
        Parent root = loader.load();

        GameBoardController controller = loader.getController();
        // Use the stored gameMode instead of hardcoding "Player vs Computer"
        controller.setGameSettings(playerX, playerO, gameMode, difficulty);
        App.initializeJoysticks(controller);

        Stage stage = (Stage) easyButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    @FXML
    private void goBack() {
        try {
            MusicController.playSound(MusicController.SOUND_CLICK);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerNameEntry.fxml"));
            Parent root = loader.load();
            
            PlayerNameEntryController controller = loader.getController();
            controller.setGameMode(gameMode);
           App.initializeJoysticks((JoystickControllable) controller);
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading PlayerNameEntry.fxml");
        }
    }

    @FXML
    private void goHome(ActionEvent event) {
        try {
            MusicController.playSound(MusicController.SOUND_CLICK);
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

    @Override
    public boolean requiresSecondJoystick() {
        return false; // Only game board needs two joysticks
    }
}