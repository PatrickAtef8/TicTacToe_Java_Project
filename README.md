# TicToeTacGame

## Contributors
- **Patrick Atef**
- **Yasmeen Yasser**
- **Abdallah Salah**

## Overview
TicToeTacGame is a Java-based Tic-Tac-Toe game that utilizes **JavaFX** for the UI and supports **joystick interfacing** for gameplay. The project includes multiple game modes, AI difficulty levels, and a score-saving system. Future enhancements include **client-server architecture** for online gameplay.

## Features
### Software (Java Logic)
- Supports single-player mode with an AI opponent
- Allows multiplayer mode (local & online in future updates)
- Provides AI difficulty selection
- Tracks and saves scores

### UI (JavaFX)
The application consists of **five UI screens**:
1. **Main Menu** - Displays a welcome screen
2. **Mode Selection** - Provides options for single-player or multiplayer
3. **AI Difficulty Selection** - Appears when playing against AI
4. **Game UI** - Manages the main gameplay interface
5. **Score UI** - Saves and displays scores

### Hardware (Joystick Interfacing)
- Supports gameplay using **joysticks** alongside standard input controls
- Interfaces with Java for a seamless experience

## Future Updates
- Adds online multiplayer with client-server architecture
- Improves AI for a better single-player experience
- Enhances joystick controls

**Progress is ongoing, stay tuned!** 🚀


# 🎮 Tic-Tac-Toe JavaFX Game

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-19-purple)](https://openjfx.io/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

A feature-rich Tic-Tac-Toe game with JavaFX, supporting joystick input, sound effects, and multiple game modes.

![Game Screenshot](screenshots/gameplay.gif) *(Add your screenshot later)*

## ✨ Features

- **Game Modes**
  - Player vs Player (Local)
  - Player vs Computer (AI)
- **Immersive UX**
  - Virtual keyboard for name entry
  - Animated win/draw effects (confetti, glowing tiles)
  - Sound effects (place X/O, win/lose, background music)
- **Input Support**
  - Mouse/touch controls
  - **Joystick/Gamepad** integration
- **Clean Architecture**
  - MVC pattern with FXML
  - Separated game logic (`model/`) from UI (`controllers/`)

## 🛠️ Technologies

- **Core**: Java 17+
- **UI**: JavaFX 19
- **Build**: Maven
- **Audio**: JavaFX `MediaPlayer`
- **Input**: JInput (Joystick)

## 📦 Installation

### Prerequisites
- JDK 17+
- Maven 3.8+

### Steps
1. Clone the repo:
   ```bash
   git clone https://github.com/PatrickAtef8/TicTacToe_Java_Project.git
