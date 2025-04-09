# TicTacToe Game with Joystick Support

![Game Screenshot](xo.png) 

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

👥 Contributors

    Patrick Atef - Game Logic & AI

    Yasmeen Yasser - UI/UX Design

    Abdallah Salah - Joystick Integration

🔜 Roadmap

    Online multiplayer support

    Enhanced AI personality profiles

    Joystick calibration UI

    Tournament mode
