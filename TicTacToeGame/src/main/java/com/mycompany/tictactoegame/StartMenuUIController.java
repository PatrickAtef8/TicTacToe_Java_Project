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
public class StartMenuUIController 
{


    @FXML
    private Button startButton;
    @FXML
    private Button quitButton;
    
    @FXML
    private void switchToGameModeUI() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameModeUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) startButton.getScene().getWindow();
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
    private void exitApplication() 
    {
          System.exit(0);
    }

}
