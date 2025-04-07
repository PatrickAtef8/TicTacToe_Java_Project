package com.mycompany.tictactoegame.utils.input;

public interface JoystickControllable {
    void handleJoystickMove(int joystickId, int axisNumber, int value);
    void handleJoystickPress(int joystickId, int buttonNumber);  

    public boolean requiresSecondJoystick();
}