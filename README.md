# 🎮 Tic-Tac-Toe JavaFX Game

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-19-purple)](https://openjfx.io/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

A feature-rich Tic-Tac-Toe game with JavaFX, supporting joystick input, sound effects, and multiple game modes.




## Contributors
- **Patrick Atef**
- **Yasmeen Yasser**
- **Abdallah Salah**
- 
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

Welcome to a fun, interactive, and joystick-enabled version of Tic Tac Toe built using **JavaFX**! This project features animated UI, emoji-based gameplay, multiple game modes, and a playful, kid-friendly theme.

## Features

- **Game Modes**
  - Player vs. Player
  - Player vs. Computer (Easy, Medium, Hard)

- **Joystick Support**
  - Full navigation and gameplay via one or two joysticks
  - Configurable interface for joystick-controlled scenes

- **Kid-Friendly UI**
  - Animated buttons with glowing effects
  - Emoji-based symbols instead of "X" and "O"
  - Playful background and celebration confetti
  - Cartoonish font with purple-themed styling

- **Smart AI**
  - Easy: Random moves
  - Medium: Basic win/block strategy
  - Hard: Minimax algorithm for unbeatable play

- **Virtual Keyboard**
  - For name entry using joystick

- **Modular MVC Architecture**
  - Clean separation between View, Controller, Model, and Utility layers

## Project Structure

. ├── App.java ├── controllers/ │ ├── StartMenuUIController.java │ ├── ModeSelectionController.java │ ├── DifficultySelectionController.java │ ├── PlayerNameEntryController.java │ ├── GameBoardController.java │ ├── ScoreBoardController.java │ └── VirtualKeyboardController.java ├── model/ │ └── GameLogic.java ├── utils/ │ ├── JoystickReader.java │ ├── JoystickManager.java │ └── MusicController.java ├── resources/com/mycompany/tictactoegame/ │ ├── StartMenuUI.fxml │ ├── ModeSelectionUI.fxml │ ├── DifficultySelectionUI.fxml │ ├── PlayerNameEntry.fxml │ ├── VirtualKeyboard.fxml │ ├── GameBoardUI.fxml │ └── ScoreBoardUI.fxml └── audio/ └── *.mp3 files for music and sound


## Getting Started

### Prerequisites

- Java 17 or later
- JavaFX SDK 17+
- (Optional) Joystick connected via USB

### Build & Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/javafx-tictactoe-joystick.git
cd javafx-tictactoe-joystick
Compile & Run (Using CLI or your IDE)

javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out $(find . -name "*.java")
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out App
Replace /path/to/javafx-sdk/lib with the actual path to your JavaFX installation.
```
Notes
On first run, joystick detection logs raw events to the console.

Fallbacks are available if joystick is not detected (use mouse/keyboard).

Contribution
Pull requests are welcome! Feel free to submit enhancements, bug fixes, or new features.

License
This project is licensed under the MIT License.



Let me know if you’d like to include a GIF or image of your UI in the README too—it’s a great touch for presentations or GitHub.





markdown
Copy code

## Getting Started

### Prerequisites

- Java 17 or later
- JavaFX SDK 17+
- (Optional) Joystick connected via USB

### Build & Run

1. Clone the repository:

```bash
git clone https://github.com/yourusername/javafx-tictactoe-joystick.git
cd javafx-tictactoe-joystick
Compile & Run (Using CLI or your IDE)

CLI Example (with JavaFX):

bash
Copy code
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out $(find . -name "*.java")
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out App
Replace /path/to/javafx-sdk/lib with the actual path to your JavaFX installation.
```
Notes
On first run, joystick detection logs raw events to the console.

Fallbacks are available if joystick is not detected (use mouse/keyboard).

Contribution
Pull requests are welcome! Feel free to submit enhancements, bug fixes, or new features.

