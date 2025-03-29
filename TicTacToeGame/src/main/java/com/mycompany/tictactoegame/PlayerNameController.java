package com.mycompany.tictactoegame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import javafx.scene.control.ButtonType;

public class PlayerNameController {

    @FXML
    private TextField playerXName;
    
    @FXML
    private TextField playerOName;
    
    @FXML
    private Button startGameButton;

@FXML
private void startGame() {
    String playerX = playerXName.getText().trim();
    String playerO = playerOName.getText().trim();

    if (playerX.isEmpty() || playerO.isEmpty()) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("⚠ Missing Names");
        alert.setHeaderText(null);
        alert.setContentText("Please enter names for both players!");

        // Apply custom styles
        alert.getDialogPane().setStyle("-fx-background-color: #2B0B33; -fx-border-color: yellow;");

        // Styling the content label
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: yellow; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Styling the buttons
        alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: linear-gradient(to right, #FF3333, #FF9966); " +
                "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;");

        alert.showAndWait();
    } else {
        switchToGameBoardScene(playerX, playerO);
    }
}


    private void switchToGameBoardScene(String playerX, String playerO) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoardUI.fxml"));
            Parent root = loader.load();

            GameBoardController gameController = loader.getController();
            gameController.setPlayerNames(playerX, playerO);

            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading GameBoard.fxml!");
        }
    }
}
