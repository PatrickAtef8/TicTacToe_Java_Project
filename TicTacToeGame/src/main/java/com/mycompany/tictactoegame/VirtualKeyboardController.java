package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

public class VirtualKeyboardController {
    private TextField targetTextField;
    private JoystickReader joystickReader;
    private int currentFocusIndex = 0;
    
    // FXML injected buttons (must match your FXML exactly)
    @FXML private Button buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH, buttonI, buttonJ;
    @FXML private Button buttonK, buttonL, buttonM, buttonN, buttonO, buttonP, buttonQ, buttonR, buttonS, buttonT;
    @FXML private Button buttonU, buttonV, buttonW, buttonX, buttonY, buttonZ, buttonSpace, buttonBackspace, buttonCaps , buttonOK;
    
     private boolean isCapsLockOn = false;
     private Button[] letterButtons;  // Will store only the letter buttons (A-Z)
    private Button[] keyboardButtons;

    @FXML
    public void initialize() {
        // Initialize the button array (include Caps Lock button)
        keyboardButtons = new Button[] {
            buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH, buttonI, buttonJ,
            buttonK, buttonL, buttonM, buttonN, buttonO, buttonP, buttonQ, buttonR, buttonS, buttonT,
            buttonU, buttonV, buttonW, buttonX, buttonY, buttonZ, buttonSpace, buttonBackspace, 
            buttonCaps, buttonOK  // Added buttonCaps
        };
        
        // Store just the letter buttons (A-Z) for case toggling
        letterButtons = new Button[] {
            buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH, buttonI, buttonJ,
            buttonK, buttonL, buttonM, buttonN, buttonO, buttonP, buttonQ, buttonR, buttonS, buttonT,
            buttonU, buttonV, buttonW, buttonX, buttonY, buttonZ
        };
        
        // Set initial focus
        updateFocus();
    }
    
    @FXML
    private void toggleCapsLock(ActionEvent event) {
        isCapsLockOn = !isCapsLockOn;  // Toggle state
        
        // Update the Caps Lock button appearance
        if (isCapsLockOn) {
            buttonCaps.setStyle("-fx-font-size: 30px; -fx-padding: 15px; " +
                "-fx-background-color: #FFD700; -fx-text-fill: black; " +  // Gold color when active
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-border-color: #333; -fx-border-width: 2; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);");
        } else {
            buttonCaps.setStyle("-fx-font-size: 30px; -fx-padding: 15px; " +
                "-fx-background-color: #FFA500; -fx-text-fill: black; " +  // Orange when inactive
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-border-color: #333; -fx-border-width: 2; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);");
        }
        
        // Toggle case of all letter buttons
        for (Button button : letterButtons) {
            String currentText = button.getText();
            button.setText(isCapsLockOn ? currentText.toUpperCase() : currentText.toLowerCase());
        }
    }
    

    


        @FXML
    private void addCharacter(ActionEvent event) {
        Button button = (Button) event.getSource();
        String text = button.getText();
        
        if (targetTextField != null) {
            if ("Space".equalsIgnoreCase(text)) {
                targetTextField.appendText(" ");
            } else {
                // Use the current case (upper or lower) based on the button text
                targetTextField.appendText(text);
            }
        }
    }
    
    @FXML
private void closeKeyboard(ActionEvent event) {
    // Get the stage (window) containing the OK button
    Stage stage = (Stage) buttonOK.getScene().getWindow();
    // Close the keyboard window
    stage.close();
}

    @FXML
    private void removeCharacter(ActionEvent event) {
        if (targetTextField != null) {
            String currentText = targetTextField.getText();
            if (!currentText.isEmpty()) {
                targetTextField.setText(currentText.substring(0, currentText.length() - 1));
            }
        }
    }

    public void setTargetTextField(TextField targetTextField) {
        this.targetTextField = targetTextField;
    }

    public void setJoystickReader(JoystickReader joystickReader) {
        this.joystickReader = joystickReader;
    }

    public void handleJoystickMove(int axis, int value) {
        

        if (axis == 1) { // Y-axis (up/down)
            if (value == 32767) { //down
                moveFocusDown();
            } else if (value == 32769) { //up
                moveFocusUp();
            }
        } else if (axis == 0) { // X-axis (left/right)
            if (value == 32767) {
                moveFocusRight();
            } else if (value == 32769) {
                moveFocusLeft();
            }
        }
    }

    public void handleJoystickPress(int button) {
        if (button == 0) { // "A" or Enter button
            Platform.runLater(() -> {
                if (currentFocusIndex >= 0 && currentFocusIndex < keyboardButtons.length) {
                    keyboardButtons[currentFocusIndex].fire();
                }
            });
        }
    }

    private void moveFocusUp() {
        int columns = 10; // Matches your GridPane column count
        if (currentFocusIndex - columns >= 0) {
            currentFocusIndex -= columns;
            updateFocus();
        }
    }

    private void moveFocusDown() {
        int columns = 10;
        if (currentFocusIndex + columns < keyboardButtons.length) {
            currentFocusIndex += columns;
            updateFocus();
        }
    }

    private void moveFocusLeft() {
        if (currentFocusIndex > 0) {
            currentFocusIndex--;
            updateFocus();
        }
    }

    private void moveFocusRight() {
        if (currentFocusIndex < keyboardButtons.length - 1) {
            currentFocusIndex++;
            updateFocus();
        }
    }

private void updateFocus() {
    Platform.runLater(() -> {
        // Clear all focus effects first
        for (Button button : keyboardButtons) {
            if (button != null) {
                button.setEffect(null);
                // Reset to original gray style
                button.setStyle("-fx-font-size: 30px; -fx-padding: 15px; " +
                    "-fx-background-color: #C0C0C0; -fx-text-fill: black; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; " +
                    "-fx-border-color: #333; -fx-border-width: 2; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0, 0, 2);");
            }
        }
        
        // Apply focus effect to current button
        if (currentFocusIndex >= 0 && currentFocusIndex < keyboardButtons.length) {
            Button focusedButton = keyboardButtons[currentFocusIndex];
            if (focusedButton != null) {
                focusedButton.setEffect(createGlowEffect());
                focusedButton.setStyle("-fx-font-size: 30px; -fx-padding: 15px; " +
                    "-fx-background-color: #A9A9A9; -fx-text-fill: white; " +  // Slightly darker gray when focused
                    "-fx-border-radius: 8; -fx-background-radius: 8; " +
                    "-fx-border-color: yellow; -fx-border-width: 2; " +  // Yellow border for focus
                    "-fx-effect: dropshadow(gaussian, rgba(255, 255, 0, 0.7), 10, 0, 0, 3);");
                focusedButton.requestFocus();
            }
        }
    });
}


    private DropShadow createGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(20);
        glow.setSpread(0.7);
        return glow;
    }
    
    
    
}