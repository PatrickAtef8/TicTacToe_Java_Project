package com.mycompany.mavenproject1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PrimaryController 
{

    @FXML
    private Label ticLabel, tacLabel, toeLabel, player1Label, player2Label;

    @FXML
    private Button playAgainButton, finishButton;

    @FXML
    private GridPane gameBoard;

    private Button[][] board = new Button[3][3];

    public void initialize() 
    {
        setupUI();
        createGameBoard();
        
    }

    private void setupUI() 
    {
        ticLabel.setText("Tic.");
        tacLabel.setText("Tac.");
        toeLabel.setText("Toe.");
        player1Label.setText("Player X");
        player2Label.setText("Player O");
    }

    private void createGameBoard() 
    {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button cell = new Button();
                cell.setMinSize(100, 100);
                cell.setStyle("-fx-background-color: #AA44CC; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold; -fx-border-radius: 10;");
                gameBoard.add(cell, col, row);
                board[row][col] = cell;
            }
        }
    }
    
    
} 





