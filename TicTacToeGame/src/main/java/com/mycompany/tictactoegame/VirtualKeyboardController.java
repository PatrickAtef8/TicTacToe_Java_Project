package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class VirtualKeyboardController {
    private TextField targetTextField;
    private JoystickReader joystickReader;
    private int currentFocusIndex = 0;
    
    // FXML injected buttons (must match your FXML exactly)
    @FXML private Button buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH, buttonI, buttonJ;
    @FXML private Button buttonK, buttonL, buttonM, buttonN, buttonO, buttonP, buttonQ, buttonR, buttonS, buttonT;
    @FXML private Button buttonU, buttonV, buttonW, buttonX, buttonY, buttonZ, buttonSpace, buttonBackspace;
    
    private Button[] keyboardButtons;

    @FXML
    public void initialize() {
        // Initialize the button array in the exact order they appear in the FXML
        keyboardButtons = new Button[] {
            buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH, buttonI, buttonJ,
            buttonK, buttonL, buttonM, buttonN, buttonO, buttonP, buttonQ, buttonR, buttonS, buttonT,
            buttonU, buttonV, buttonW, buttonX, buttonY, buttonZ, buttonSpace, buttonBackspace
        };
        
        // Set initial focus
        updateFocus();
    }

    @FXML
    private void addCharacter(ActionEvent event) {
        Button button = (Button) event.getSource();
        String text = button.getText();
        
        if (targetTextField != null) {
            if ("Space".equals(text)) {
                targetTextField.appendText(" ");
            } else {
                targetTextField.appendText(text);
            }
        }
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
            // Clear all effects first
            for (Button button : keyboardButtons) {
                if (button != null) {
                    button.setEffect(null);
                    button.setStyle("-fx-border-width: 0; -fx-background-color: transparent;");
                }
            }
            
            // Apply effect to current button
            if (currentFocusIndex >= 0 && currentFocusIndex < keyboardButtons.length) {
                Button focusedButton = keyboardButtons[currentFocusIndex];
                if (focusedButton != null) {
                    focusedButton.setEffect(createGlowEffect());
                    focusedButton.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-background-color: #4B0082;");
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