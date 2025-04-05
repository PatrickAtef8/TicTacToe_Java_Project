package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class PlayerNameController {
    
    @FXML
    private TextField playerXName, playerOName;
    
    @FXML
    private ChoiceBox<String> modeChoiceBox, difficultyChoiceBox;
    
    @FXML
    private Button startGameButton;
    
    @FXML
    private Label warningLabel;
    
    @FXML
    private Button backButton;
    
    private JoystickReader joystickReader;
private int currentFocusIndex = 0;

private javafx.scene.Node[] focusableNodes;


    
@FXML
private void startGame() {
    String playerX = playerXName.getText().trim();
    String playerO = playerOName.getText().trim();
    String mode = modeChoiceBox.getValue();
    String difficulty = difficultyChoiceBox.getValue();

    if (mode.equals("Player vs Computer")) {
        playerO = "Computer⚡"; // Set funny AI name
        playerOName.setText(playerO);
    }

    if (playerX.isEmpty() || (mode.equals("Player vs Player") && playerO.isEmpty())) {
        showWarningAlert("⚠ Missing Names", "Please enter names for both players!");
        return;
    }

    switchToGameBoardScene(playerX, playerO, mode, difficulty);
}

private void showWarningAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);

    // Apply custom styles
    alert.getDialogPane().setStyle("-fx-background-color: #2B0B33; -fx-border-color: yellow;");
    alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: yellow; -fx-font-size: 18px; -fx-font-weight: bold;");
    alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
            "-fx-background-color: linear-gradient(to right, #FF3333, #FF9966); " +
            "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;");

    alert.showAndWait();
}

@FXML
public void initialize() {
    modeChoiceBox.getItems().addAll("Player vs Player", "Player vs Computer");
    modeChoiceBox.setValue("Player vs Player");

    difficultyChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
    difficultyChoiceBox.setValue("Medium");
    difficultyChoiceBox.setDisable(true);

    modeChoiceBox.setOnAction(event -> {
        boolean isAI = modeChoiceBox.getValue().equals("Player vs Computer");
        difficultyChoiceBox.setDisable(!isAI);
        playerOName.setDisable(isAI);
        if (isAI) {
            playerOName.setText("Computer⚡");
        } else {
            playerOName.clear();
        }
    });

    // Focusable controls order
    focusableNodes = new javafx.scene.Node[] {
        playerXName, playerOName, modeChoiceBox, difficultyChoiceBox, startGameButton, backButton
    };

    updateFocus();

    startJoystick();
}

// In your PlayerNameController.java

public void handleJoystickMove(int joystickId, int axis, int value) {
    
    
    // Debug output to see joystick values
    System.out.println("Joystick Axis: " + axis + " Value: " + value);
    
    // Try different axis numbers - some joysticks use different numbering
    if (axis == 1 || axis == 4) {  // Common Y-axis numbers
               if (value == 32767) { //down
                moveFocusDown();
            } else if (value == 32769) { //up
                moveFocusUp();
            }
    }

}

private void moveFocusDown() {
    Platform.runLater(() -> {
        currentFocusIndex = (currentFocusIndex + 1) % focusableNodes.length;
        updateFocus();
    });
    sleepToPreventBounce();
}

private void moveFocusUp() {
    Platform.runLater(() -> {
        currentFocusIndex = (currentFocusIndex - 1 + focusableNodes.length) % focusableNodes.length;
        updateFocus();
    });
    sleepToPreventBounce();
}

private void updateFocus() {
    Platform.runLater(() -> {
        for (int i = 0; i < focusableNodes.length; i++) {
            javafx.scene.Node node = focusableNodes[i];
            if (i == currentFocusIndex) {
                node.setEffect(createGlowEffect());
                // Request focus for better visual feedback
                if (node instanceof TextField) {
                    ((TextField) node).requestFocus();
                } else if (node instanceof ChoiceBox) {
                    ((ChoiceBox<?>) node).requestFocus();
                } else if (node instanceof Button) {
                    ((Button) node).requestFocus();
                }
            } else {
                node.setEffect(null);
            }
        }
    });
}


private void startJoystick() {
    String devicePath = "/dev/input/js0"; // adjust if needed
    int joystickId = 0;
    joystickReader = new JoystickReader(this, devicePath, joystickId);
    new Thread(joystickReader).start();
}


public void handleJoystickPress(int joystickId, int button) {
    if (button == 0) { // Usually the "A" or "Enter" button
        Platform.runLater(() -> {
            javafx.scene.Node node = focusableNodes[currentFocusIndex];

            if (node instanceof TextField) 
            {
                TextField textField = (TextField) node;
                showVirtualKeyboard(textField);
            } 
            else if (node instanceof ChoiceBox) {
                @SuppressWarnings("unchecked")
                ChoiceBox<String> box = (ChoiceBox<String>) node;
                int index = box.getItems().indexOf(box.getValue());
                int next = (index + 1) % box.getItems().size();
                box.setValue(box.getItems().get(next));
            } else if (node instanceof Button) {
                ((Button) node).fire();
            }
        });
    }
}


@FXML
private void showVirtualKeyboard(TextField targetTextField) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VirtualKeyboard.fxml"));
        Parent root = loader.load();
        VirtualKeyboardController keyboardController = loader.getController();
        keyboardController.setTargetTextField(targetTextField);
        keyboardController.setJoystickReader(joystickReader);
        
        // Switch control to keyboard
        joystickReader.setActiveController(keyboardController);

        Stage keyboardStage = new Stage();
        keyboardStage.setScene(new Scene(root));
        keyboardStage.setTitle("Virtual Keyboard");
        
        // Restore control when keyboard closes
        keyboardStage.setOnHidden(e -> {
            joystickReader.setActiveController(this);
            targetTextField.setEditable(true);
        });
        
        keyboardStage.show();
        targetTextField.setEditable(false);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


private void sleepToPreventBounce() {
    try {
        Thread.sleep(150);
    } catch (InterruptedException ignored) {}
}

private DropShadow createGlowEffect() {
    DropShadow glow = new DropShadow();
    glow.setColor(Color.PURPLE);
    glow.setRadius(20);
    glow.setSpread(0.5);
    return glow;
}



public void setGameMode(String mode) {
    modeChoiceBox.setValue(mode);

    boolean isAI = mode.equals("Player vs Computer");
    difficultyChoiceBox.setDisable(!isAI);
    playerOName.setDisable(isAI);

    if (isAI) {
        playerOName.setText("Computer⚡");
    } else {
        playerOName.clear();
    }
}



    private void switchToGameBoardScene(String playerX, String playerO, String mode, String difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();

            GameBoardController gameController = loader.getController();
            gameController.setGameSettings(playerX, playerO, mode, difficulty);

            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading GameBoardUI.fxml!");
        }
    }

    private void showWarningAlert() {
        System.out.println("Please enter player names before starting.");
    }
    


@FXML
private void switchToGameModeUI() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameModeUI.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error loading GameModeUI.fxml!");
    }
}

} 