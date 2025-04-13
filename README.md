# TicTacToe Game with Joystick Support

![Image](https://github.com/user-attachments/assets/1e68b0e2-db6e-42a0-ac3e-52d95c4262ed)

## 🎮 Project Overview
A modern JavaFX implementation of Tic-Tac-Toe featuring **joystick hardware integration** and AI opponents with multiple difficulty levels. Designed for both casual play and technical demonstration of Java hardware interfacing.

## ✨ Key Features

### 🖥️ Software Architecture
- **MVC Pattern** with clean separation of:
  - **Model**: `GameLogic.java` handles game state and AI
  - **View**: FXML files for all UI screens
  - **Controller**: Dedicated controllers for each view
- **Joystick API**:
  - Real-time device polling (`/dev/input/js*`)
  - Hotplug detection
  - Event routing to active controllers

### 🎨 UI Components
| Screen | Description |
|--------|-------------|
| **Start Menu** | Game entry point with play/exit options |
| **Mode Selection** | PvP (local) vs PvC (AI) selection |
| **Difficulty Selection** | Easy/Medium/Hard AI levels |
| **Player Setup** | Name entry with virtual keyboard |
| **Game Board** | Interactive 3x3 grid with joystick support |
| **Score Board** | Results display with rematch option |

### 🤖 AI Implementation
- **Minimax algorithm** with difficulty modifiers:
  - Easy: Random moves
  - Medium: Basic strategy
  - Hard: Unbeatable AI

### 🕹️ Joystick Integration
```java
public interface JoystickControllable {
    void handleJoystickMove(int joystickId, int axis, int value);
    void handleJoystickPress(int joystickId, int button);
    boolean requiresSecondJoystick();
}
```

   * Supports multiple joysticks

   * Visual feedback for device connection/disconnection

🛠️ Technical Stack

    Java 17 with JavaFX

    Linux input subsystem (/dev/input/js*)

    Gradle build system

🚀 Getting Started
Prerequisites

    Linux OS (for joystick support)

    Java 17 JDK

    Gradle 7+

Installation
``` bash

git clone https://github.com/your-repo/TicToeTacGame.git
cd TicToeTacGame
gradle run
```

Joystick Setup

    Connect your gamepad/joystick

    Ensure proper permissions for /dev/input/js*

    Launch the game - automatic detection enabled

📜 Game Flow
```mermaid


graph TD
    A[Start Menu] --> B[Mode Selection]
    B --> C{PvP?}
    C -->|Yes| D[Player Setup]
    C -->|No| E[AI Difficulty]
    E --> D
    D --> F[Game Board]
    F --> G[Score Board]
    G --> F
    G --> A
```



📜 Software Arch
```mermaid

---
config:
  theme: redux-dark
  fontFamily: Comic Sans MS
  themeVariables:
    primaryColor: '#2B0B33'
    nodeBorder: '#8E44AD'
    clusterBkg: '#1e1e3d'
    fontFamily: Comic Sans MS
---
flowchart TD
 subgraph VIEW["🎨 View Layer (resources/com/mycompany/tictactoegame)"]
        StartMenu[["StartMenuUI.fxml"]]
        ModeSelection[["ModeSelectionUI.fxml"]]
        Difficulty[["DifficultySelectionUI.fxml"]]
        PlayerEntry[["PlayerNameEntry.fxml"]]
        VirtualKB[["VirtualKeyboard.fxml"]]
        GameBoard[["GameBoardUI.fxml"]]
        ScoreBoard[["ScoreBoardUI.fxml"]]
  end
 subgraph CONTROLLER["🎮 Controller Layer (controllers/)"]
        StartCtrl[["StartMenuUIController.java"]]
        ModeCtrl[["ModeSelectionController.java"]]
        DiffCtrl[["DifficultySelectionController.java"]]
        PlayerCtrl[["PlayerNameEntryController.java"]]
        GameCtrl[["GameBoardController.java"]]
        ScoreCtrl[["ScoreBoardController.java"]]
        VKBCtrl[["VirtualKeyboardController.java"]]
  end
 subgraph MODEL["🧠 Model Layer (model/)"]
        GameLogic[["GameLogic.java
        ---
        - Board state
        - Win detection
        - AI moves"]]
  end
 subgraph UTILS["⚙️ Utilities (utils/)"]
        JoystickReader[["JoystickReader.java
        ---
        - Hardware polling"]]
        JoystickManager[["JoystickManager.java
        ---
        - Event routing"]]
        MusicController[["MusicController.java
        ---
        - Plays audio/*.mp3"]]
  end
    App[["App.java
    ---
    - Starts JavaFX
    - Initializes JoystickReader
    - Loads StartMenuUI.fxml"]] --> StartMenu & StartMenu
    StartMenu --> StartCtrl
    StartCtrl -- Play button --> ModeSelection
    ModeSelection --> ModeCtrl
    ModeCtrl -- PvP selected --> PlayerEntry
    PlayerEntry --> PlayerCtrl
    ModeCtrl -- PvC selected --> Difficulty
    Difficulty --> DiffCtrl
    DiffCtrl --> PlayerEntry
    PlayerCtrl -- Needs input --> VirtualKB
    VirtualKB --> VKBCtrl
    PlayerCtrl -- Names ready --> GameBoard
    GameBoard --> GameCtrl
    GameCtrl -- Game ends --> ScoreBoard
    ScoreBoard --> ScoreCtrl
    ScoreCtrl -- Rematch --> GameBoard
    ScoreCtrl -- Main menu --> StartMenu
    JoystickReader -- Raw events --> JoystickManager
    JoystickManager -- Routes to active --> StartCtrl & ModeCtrl & DiffCtrl & PlayerCtrl & GameCtrl & ScoreCtrl & VKBCtrl
    GameCtrl -- Makes moves --> GameLogic
    GameLogic -- Game state --> GameCtrl
    JoystickControllable[["«interface» 
    JoystickControllable.java
    ---
    + handleJoystickMove()
    + handleJoystickPress()
    + requiresSecondJoystick()"]] -. implements .-> StartCtrl & ModeCtrl & DiffCtrl & PlayerCtrl & GameCtrl & ScoreCtrl & VKBCtrl
    style JoystickControllable fill:#5b2576,stroke-dasharray:5 5,stroke:#f39c12
    style VIEW fill:#6b1e3d,stroke:#e74c3c
    style CONTROLLER fill:#3d6b1e,stroke:#2ecc71
    style MODEL fill:#1e3d6b,stroke:#3498db
    style UTILS fill:#5b2576,stroke:#f39c12
```



📜 Hardware sequence diagram

```mermaid

%%{init: {
  'theme': 'dark',
  'fontFamily': 'Comic Sans MS',
  'themeVariables': {
    'primaryColor': '#2B0B33',
    'nodeBorder': '#8E44AD',
    'clusterBkg': '#1e1e3d'
  }
}}%%

sequenceDiagram
    participant Hardware as 🎮 Hardware (js0/js1)
    participant JoystickReader as JoystickReader
    participant JoystickManager as JoystickManager
    participant ActiveController as Active Controller
    participant UI as Current UI

    Note over Hardware,UI: Initialization Phase
    App->>JoystickManager: startHotplugDetection(controller)
    JoystickManager->>Hardware: Check /dev/input/js0, js1
    Hardware-->>JoystickManager: Connection status
    JoystickManager->>JoystickReader: Initialize (if connected)
    JoystickReader->>Hardware: Open input stream

    loop Polling Thread
        Hardware->>JoystickReader: Raw event bytes
        JoystickReader->>JoystickReader: Parse event (type=2:axis/1:button)
        alt Axis Movement
            JoystickReader->>ActiveController: handleJoystickMove(joystickId, axis, value)
        else Button Press
            JoystickReader->>ActiveController: handleJoystickPress(joystickId, button)
        end
        ActiveController->>UI: Update visual state
    end

    Note over Hardware,UI: Hotplug Detection
    loop Every 1 Second
        JoystickManager->>Hardware: Check device files
        alt Joystick Connected
            Hardware-->>JoystickManager: /dev/input/jsX exists
            JoystickManager->>UI: Show connection alert
            JoystickManager->>JoystickReader: Start new reader
        else Joystick Disconnected
            Hardware-->>JoystickManager: /dev/input/jsX missing
            JoystickManager->>UI: Show disconnection alert
            JoystickManager->>JoystickReader: Stop reader
        end
    end

    Note over Hardware,UI: Controller Switching
    UI->>JoystickManager: updateController(newController)
    JoystickManager->>JoystickReader: setController(newController)
    JoystickReader->>ActiveController: All future events routed here
```


👥 Contributors

    Yasmeen Yasser 

    Patrick Atef 
    
    Abdallah Salah 

🔜 Roadmap

    Online multiplayer support

    Enhanced AI personality profiles

    Joystick calibration UI

    Tournament mode
