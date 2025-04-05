package com.mycompany.tictactoegame;

import java.io.FileInputStream;
import java.io.InputStream;

public class JoystickReader implements Runnable {
    private Object activeController;  // Now tracks which controller is active
    private final String devicePath;
    private final int joystickId;
    private volatile boolean running = true;

    public JoystickReader(Object initialController, String devicePath, int joystickId) {
        this.activeController = initialController;
        this.devicePath = devicePath;
        this.joystickId = joystickId;
    }

    // Method to dynamically switch controllers
    public void setActiveController(Object controller) {
        this.activeController = controller;
        System.out.println("Switched joystick control to: " + controller.getClass().getSimpleName());
    }

    @Override
    public void run() {
        System.out.println("Starting joystick reader for " + devicePath);
        try (InputStream inputStream = new FileInputStream(devicePath)) {
            byte[] buffer = new byte[8];
            while (running) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) break;
                if (bytesRead > 0) {
                    processJoystickEvent(buffer);
                }
                Thread.sleep(5);
            }
        } catch (Exception e) {
            System.err.println("Error with " + devicePath + ": " + e.getMessage());
        } finally {
            System.out.println("Stopped reading from " + devicePath);
        }
    }

    private void processJoystickEvent(byte[] buffer) {
        int type = buffer[6] & 0xFF;
        int number = buffer[7] & 0xFF;
        int value = (buffer[4] & 0xFF) | ((buffer[5] & 0xFF) << 8);

        // Debug output
        System.out.printf("Joystick Event - Type: %d, Number: %d, Value: %d%n", type, number, value);

        if (activeController instanceof PlayerNameController) {
            PlayerNameController controller = (PlayerNameController) activeController;
            if (type == 2) {
                controller.handleJoystickMove(joystickId, number, value);
            } else if (type == 1 && value == 1) {
                controller.handleJoystickPress(joystickId, number);
            }
        } 
        else if (activeController instanceof VirtualKeyboardController) {
            VirtualKeyboardController controller = (VirtualKeyboardController) activeController;
            if (type == 2) {
                controller.handleJoystickMove(number, value);
            } else if (type == 1 && value == 1) {
                controller.handleJoystickPress(number);
            }
        }
        else if (activeController instanceof ScoreBoardController) {
            ScoreBoardController controller = (ScoreBoardController) activeController;
            if (type == 2) {
                controller.handleJoystickMove(joystickId, number, value);
            } else if (type == 1 && value == 1) {
                controller.handleJoystickPress(joystickId, number);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
