package com.mycompany.tictactoe;

import java.io.FileInputStream;
import java.io.InputStream;

public class JoystickReader implements Runnable {
    private final PrimaryController controller;
    private final String devicePath;
    private final int joystickId;
    private volatile boolean running = true;

    public JoystickReader(PrimaryController controller, String devicePath, int joystickId) {
        this.controller = controller;
        this.devicePath = devicePath;
        this.joystickId = joystickId;
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

        if (type == 2) { // Axis event
            controller.handleJoystickMove(joystickId, number, value);
        } else if (type == 1) { // Button event
            if (value == 1) controller.handleJoystickPress(joystickId, number);
            else if (value == 0) controller.handleJoystickRelease(joystickId, number);
        }
    }

    public void stop() {
        running = false;
    }
}