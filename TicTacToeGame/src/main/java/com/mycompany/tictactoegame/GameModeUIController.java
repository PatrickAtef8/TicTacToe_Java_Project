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
/**
 * FXML Controller class
 *
 * @author abdallah
 */
public class GameModeUIController  
{


    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button backButton;
    /**
     * Initializes the controller class.
     */
    
    @FXML
    private void switchToPlayerNameFromSinglePlayer() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerName.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) singlePlayerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Error loading GameModeUI.fxml!");
        }
    }
    
    @FXML
    private void switchToPlayerNameFromMultiPlayer() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerName.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) multiPlayerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Error loading GameModeUI.fxml!");
        }
    }
    
    @FXML
    private void switchToStartMenu() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("Error loading StartMenuUI.fxml!");
        }
    }
}
