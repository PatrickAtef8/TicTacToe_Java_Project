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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
            playerOName.setText("Computer⚡"); // Fun AI name
        } else {
            playerOName.clear();
        }
    });
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
}
