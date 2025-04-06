package com.mycompany.tictactoegame;

import java.io.FileInputStream;
import java.io.InputStream;

public class JoystickReader implements Runnable {
    private  JoystickControllable controller;
    private final String devicePath;
    private final int joystickId;
    private volatile boolean running = true;

    public JoystickReader(JoystickControllable controller, String devicePath, int joystickId) {
        this.controller = controller;
        this.devicePath = devicePath;
        this.joystickId = joystickId;
    }

    
    @Override
    public void run() {
        try (InputStream joystick = new FileInputStream(devicePath)) {
            byte[] event = new byte[8];
            while (running && joystick.read(event) != -1) {
                int type = event[6] & 0xFF;
                int number = event[7] & 0xFF;
                int value = (event[4] & 0xFF) | ((event[5] & 0xFF) << 8);

                if (type == 2) {
                    controller.handleJoystickMove(joystickId, number, value);
                } else if (type == 1 && value == 1) {
                    controller.handleJoystickPress(joystickId, number);
                }
            }
        } catch (Exception e) {
            if (running) System.err.println("Joystick error: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
    }

    void setController(JoystickControllable controller) {
    this.controller = controller;    }
}