package com.mycompany.tictactoegame;

public interface JoystickControllable {
    void handleJoystickMove(int joystickId, int axisNumber, int value);
    void handleJoystickPress(int joystickId, int buttonNumber);  // Exactly matches your method

    public boolean requiresSecondJoystick();
}