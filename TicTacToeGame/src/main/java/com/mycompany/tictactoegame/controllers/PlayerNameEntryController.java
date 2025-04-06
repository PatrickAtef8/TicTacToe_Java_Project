package com.mycompany.tictactoegame.controllers;

import com.mycompany.tictactoegame.App;
import com.mycompany.tictactoegame.controllers.DifficultySelectionController;
import com.mycompany.tictactoegame.controllers.GameBoardController;
import com.mycompany.tictactoegame.controllers.ModeSelectionController;
import com.mycompany.tictactoegame.controllers.VirtualKeyboardController;
import com.mycompany.tictactoegame.utils.MusicController;
import com.mycompany.tictactoegame.utils.input.JoystickControllable;
import com.mycompany.tictactoegame.utils.input.JoystickManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Window;

public class PlayerNameEntryController implements JoystickControllable {
    @FXML private TextField playerXName;
    @FXML private TextField playerOName;
    @FXML private Button nextButton;
    @FXML private Button backButton;
    @FXML private Label gameModeLabel;

   private String gameMode;
    private Node[] focusableNodes; // Now includes text fields
    private int selectedIndex = 0;
    private boolean joystickEnabled = true;
    private boolean inAlert = false;
     
    private static final String HIGHLIGHT_ADDON = 
        "-fx-border-color: linear-gradient(to right, #f1c40f, #f39c12);" +
        "-fx-border-width: 3px;" +
        "-fx-border-radius: 20px;" +
        "-fx-border-style: solid outside;" +
        "-fx-effect: dropshadow(gaussian, rgba(241,196,15,0.7), 15, 0.5, 0, 0);";

 @FXML
    public void initialize() {
        // Initialize focusable nodes (text fields first, then buttons)
        focusableNodes = new Node[]{playerXName, playerOName, nextButton, backButton};
        
        for (Node node : focusableNodes) {
            node.getProperties().put("originalStyle", node.getStyle());
        }
        
        playerXName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) selectedIndex = 0;
            updateSelection();
        });
        
        playerOName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) selectedIndex = 1;
            updateSelection();
        });
        
        updateSelection();
        
        try {
            App.initializeJoysticks(this);
        } catch (Exception e) {
            joystickEnabled = false;
        }
    }

   @Override
    public void handleJoystickMove(int joystickId, int axisNumber, int value) {
        if (!joystickEnabled || inAlert) return;

        Platform.runLater(() -> {
            if (axisNumber == 5 || axisNumber == 1) { // UP/DOWN axis
                if (value == 32767) { // DOWN
                    selectedIndex = (selectedIndex + 1) % focusableNodes.length;
                } 
                else if (value == 32769) { // UP
                    selectedIndex = (selectedIndex - 1 + focusableNodes.length) % focusableNodes.length;
                }
                updateSelection();
            }
        });
    }

    @Override
    public void handleJoystickPress(int joystickId, int buttonNumber) {
        if (!joystickEnabled) return;

        Platform.runLater(() -> {
            if (inAlert && activeAlert != null) {
                Button okButton = (Button) activeAlert.getDialogPane().lookupButton(ButtonType.OK);
                if (okButton != null) {
                    okButton.fire();
                }
            } else {
                if (buttonNumber == 0) { // "A" button
                    if (selectedIndex == 0 || selectedIndex == 1) {
                        // Open keyboard for the selected text field
                        openVirtualKeyboard((TextField)focusableNodes[selectedIndex]);
                    } else {
                        // Fire the button action
                        ((Button)focusableNodes[selectedIndex]).fire();
                    }
                }
            }
        });
    
}
    
private void openVirtualKeyboard(TextField targetField) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/VirtualKeyboard.fxml"));
        Parent root = loader.load();
        
        VirtualKeyboardController keyboardController = loader.getController();
        keyboardController.setTargetTextField(targetField);
        
        App.updateJoystickController((JoystickControllable) keyboardController);
        
        Stage keyboardStage = new Stage();
        keyboardStage.setTitle("Virtual Keyboard");
        keyboardStage.setScene(new Scene(root));
        keyboardStage.initOwner(playerXName.getScene().getWindow());
        keyboardStage.initModality(Modality.WINDOW_MODAL);
        
        Bounds textFieldBounds = targetField.localToScreen(targetField.getBoundsInLocal());
        double keyboardWidth = 1000; 
        double keyboardHeight = 400; 
        
        double keyboardX = textFieldBounds.getMinX() + (textFieldBounds.getWidth() - keyboardWidth) / 2;
        double keyboardY = textFieldBounds.getMaxY() + 10;
        
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        keyboardX = Math.max(0, Math.min(keyboardX, screen.getWidth() - keyboardWidth));
        keyboardY = Math.max(0, Math.min(keyboardY, screen.getHeight() - keyboardHeight));
        
        keyboardStage.setX(keyboardX);
        keyboardStage.setY(keyboardY);
        
        keyboardStage.setOnShown(e -> {
            keyboardController.requestFocus();
        });
        
        keyboardStage.setOnHidden(e -> {
            App.updateJoystickController(this);
            targetField.requestFocus();
            updateSelection();
        });
        
        keyboardStage.showAndWait();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
    private void updateSelection() {
        Platform.runLater(() -> {
            for (int i = 0; i < focusableNodes.length; i++) {
                Node node = focusableNodes[i];
                String original = (String)node.getProperties().get("originalStyle");
                
                if (i == selectedIndex) {
                    node.setStyle((original != null ? original : "") + HIGHLIGHT_ADDON);
                    node.requestFocus();
                } else {
                    node.setStyle(original != null ? original : "");
                }
            }
        });
    }

    @Override
    public boolean requiresSecondJoystick() {
        return false;
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
        gameModeLabel.setText(mode);
        
        if (mode.equals("Player vs Computer")) {
            playerOName.setText("Computer⚡");
            playerOName.setDisable(true);
            playerOName.setStyle("-fx-font-size: 22px; "
                    + "-fx-padding: 12px 25px; "
                    + "-fx-background-radius: 15px; "
                    + "-fx-min-width: 300px; "
                    + "-fx-min-height: 50px; "
                    + "-fx-text-fill: #00FF7F; "
                    + "-fx-font-weight: bold; "
                    + "-fx-border-color: #00FF7F; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-radius: 15px; "
                    + "-fx-background-color: #2B0B33; "
                    + "-fx-opacity: 1.0;");
        } else {
            playerOName.setText("");
            playerOName.setDisable(false);
            playerOName.setPromptText("Opponent name");
            playerOName.setStyle("-fx-font-size: 22px; "
                    + "-fx-padding: 12px 25px; "
                    + "-fx-background-radius: 15px; "
                    + "-fx-min-width: 300px; "
                    + "-fx-min-height: 50px; "
                    + "-fx-text-fill: #00FF7F; "
                    + "-fx-font-weight: bold; "
                    + "-fx-border-color: #00FF7F; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-radius: 15px; "
                    + "-fx-background-color: #2B0B33;");
        }
    }

    @FXML
    private void goToNextScreen() {
        MusicController.playSound(MusicController.SOUND_CLICK);
        String playerX = playerXName.getText().trim();
        String playerO = playerOName.getText().trim();

        if (playerX.isEmpty() || playerO.isEmpty()) {
            showWarningAlert("⚠ Missing Names", "Please enter names for both players!");
        } else {
            if ("Player vs Player".equals(gameMode)) {
                switchToGameBoard();
            } else {
                switchToDifficultySelection();
            }
        }
    }
    
private Alert activeAlert; // Track the current alert

private void showWarningAlert(String title, String content) {
    Platform.runLater(() -> {
        inAlert = true;
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        this.activeAlert = alert; // Store the active alert
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Style the alert (your existing code)
        alert.getDialogPane().setStyle("-fx-background-color: #2B0B33; -fx-border-color: yellow;");
        alert.getDialogPane().lookup(".content.label").setStyle(
            "-fx-text-fill: yellow; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF3333, #FF9966); " +
            "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
            "-fx-background-radius: 10px;" + HIGHLIGHT_ADDON);

        // ===== FOCUS ENFORCEMENT =====
        // 1. Force the Alert to take focus when shown
        alert.initOwner(backButton.getScene().getWindow()); // Set owner window
        alert.initModality(Modality.WINDOW_MODAL); // Ensure it blocks input

        // 2. Auto-focus the OK button when the alert appears
        alert.setOnShown(e -> {
            okButton.requestFocus(); // Explicitly focus the OK button
        });

        // 3. Handle alert closing
        alert.setOnHidden(e -> {
            inAlert = false;
            activeAlert = null; // Clear the reference
            updateSelection(); // Restore focus to main UI
        });

        alert.show(); // Use show() (not showAndWait) to avoid blocking
    });
}
  private void switchToDifficultySelection() {
                  MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/DifficultySelectionUI.fxml"));
        Parent root = loader.load();

        DifficultySelectionController controller = loader.getController();
        controller.setPlayerNames(playerXName.getText(), playerOName.getText());
        controller.setGameMode(gameMode);
        
        // Update joystick controller reference
        JoystickManager.updateController(controller);
        
        // Get current stage and set new scene
        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private void switchToGameBoard() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/GameBoardUI.fxml"));
        Parent root = loader.load();

        GameBoardController controller = loader.getController();
        controller.setGameSettings(playerXName.getText(), playerOName.getText(), "Player vs Player", "");
        
        // Update joystick controller reference
        JoystickManager.updateController(controller);
        
        // Get current stage and set new scene
        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
private void goBack() {
                MusicController.playSound(MusicController.SOUND_CLICK);
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoegame/ModeSelectionUI.fxml"));
        Parent root = loader.load();
        
        // Get controller and update joystick reference
        ModeSelectionController controller = loader.getController();
        JoystickManager.updateController(controller);
        
        // Get current stage and set new scene
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}