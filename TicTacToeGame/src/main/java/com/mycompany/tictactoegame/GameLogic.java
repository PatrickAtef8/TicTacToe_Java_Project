package com.mycompany.tictactoegame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private String[][] board = new String[3][3];
    private String currentPlayer = "X";
    private boolean gameOver = false;
    private int playerXScore = 0;
    private int playerOScore = 0;
    private int drawScore = 0;
    private String difficulty = "Easy"; // Default difficulty

    public GameLogic() {
        resetBoard();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
        gameOver = false;
        currentPlayer = "X";
    }

    public boolean makeMove(int row, int col) {
        if (gameOver || !board[row][col].isEmpty()) {
            return false;
        }
        board[row][col] = currentPlayer;

        if (checkWinner(currentPlayer)) {
            gameOver = true;
            updateScore();
            return true;
        }

        if (isBoardFull()) {
            gameOver = true;
            drawScore++;
        } else {
            switchPlayer();
        }
        return false;
    }

    public void computeMove() {
        if (gameOver || !currentPlayer.equals("O")) return; // Ensure it's the computer's turn

        int[] move;
        switch (difficulty) {
            case "Easy":
                move = getRandomMove();
                break;
            case "Medium":
                move = getBestMoveMedium();
                break;
            case "Hard":
                move = getBestMoveMinimax();
                break;
            default:
                move = getRandomMove();
        }

        if (move != null) {
            makeMove(move[0], move[1]);
        }
    }

private int[] getRandomMove() {
    List<int[]> availableMoves = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (board[i][j].isEmpty()) availableMoves.add(new int[]{i, j});
        }
    }
    return availableMoves.get(new Random().nextInt(availableMoves.size()));
}

private int[] getBestMoveMedium() {
 
        return null;
 
}


private int[] getBestMoveMinimax() {
    int bestScore = Integer.MIN_VALUE;
    int[] bestMove = new int[]{-1, -1};

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (board[i][j].isEmpty()) {
                board[i][j] = "O";
                int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[i][j] = "";
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = new int[]{i, j};
                }
            }
        }
    }
    return bestMove;
}


private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
    if (checkWinner("O")) return 10 - depth; 
    if (checkWinner("X")) return depth - 10; 
    if (isBoardFull()) return 0;

    if (isMaximizing) {  // AI's turn (maximize)
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "O";
                    int score = minimax(depth + 1, false, alpha, beta);
                    board[i][j] = "";
                    bestScore = Math.max(bestScore, score);
                    alpha = Math.max(alpha, score); // Update alpha
                    if (beta <= alpha) return bestScore; // Prune
                }
            }
        }
        return bestScore;
    } else {  // Player's turn (minimize)
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "X";
                    int score = minimax(depth + 1, true, alpha, beta);
                    board[i][j] = "";
                    bestScore = Math.min(bestScore, score);
                    beta = Math.min(beta, score); // Update beta
                    if (beta <= alpha) return bestScore; // Prune
                }
            }
        }
        return bestScore;
    }
}


    private boolean checkWinner(String player) {
        for (int i = 0; i < 3; i++) {
            if (checkLine(player, board[i][0], board[i][1], board[i][2]) ||
                checkLine(player, board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }
        return checkLine(player, board[0][0], board[1][1], board[2][2]) || 
               checkLine(player, board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(String player, String b1, String b2, String b3) {
        return player.equals(b1) && player.equals(b2) && player.equals(b3);
    }

    private boolean isBoardFull() {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] getComputerMove(String difficulty) {
    if (gameOver) return null; // No move if the game is over

    switch (difficulty) {
        case "Easy":
            return getRandomMove();
        case "Medium":
            return getBestMoveMedium();
        case "Hard":
            return getBestMoveMinimax();
        default:
            return getRandomMove();
    }
}

    
    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
    }

    private void updateScore() {
        if (currentPlayer.equals("X")) {
            playerXScore++;
        } else {
            playerOScore++;
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPlayerXScore() {
        return playerXScore;
    }

    public int getPlayerOScore() {
        return playerOScore;
    }

    public int getDrawScore() {
        return drawScore;
    }
}

