/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegame;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameModeUIController {

    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button backButton;

    @FXML
    private void switchToPlayerNameFromSinglePlayer() {
        switchToPlayerName("Player vs Computer");
    }

    @FXML
    private void switchToPlayerNameFromMultiPlayer() {
        switchToPlayerName("Player vs Player");
    }

    private void switchToPlayerName(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerName.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the game mode
            PlayerNameController controller = loader.getController();
            controller.setGameMode(mode);

            Stage stage = (Stage) singlePlayerButton.getScene().getWindow(); // can use any button here
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading PlayerName.fxml!");
        }
    }

    @FXML
    private void switchToStartMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading StartMenuUI.fxml!");
        }
    }
}
