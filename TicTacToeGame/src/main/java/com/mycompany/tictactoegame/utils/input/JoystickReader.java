package com.mycompany.tictactoegame.utils.input;

import com.mycompany.tictactoegame.utils.input.JoystickControllable;
import java.io.FileInputStream;
import java.io.InputStream;

public class JoystickReader implements Runnable {
    private volatile JoystickControllable controller;
    private final String devicePath;
    private final int joystickId;
    private volatile boolean running = true;
    private Thread readerThread;
    
      
    public boolean isRunning() {
        return running;
    }

    public JoystickReader(JoystickControllable controller, String devicePath, int joystickId) {
        this.controller = controller;
        this.devicePath = devicePath;
        this.joystickId = joystickId;
    }

    public void start() {
        if (readerThread == null || !readerThread.isAlive()) {
            readerThread = new Thread(this);
            readerThread.setDaemon(true);
            readerThread.start();
        }
    }

    @Override
    public void run() {
        while (running) {
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
                if (running) {
                    System.err.println("Joystick error: " + e.getMessage());
                    try {
                        Thread.sleep(1000); // Wait before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public void stop() {
        running = false;
        if (readerThread != null) {
            readerThread.interrupt();
        }
    }

    public void setController(JoystickControllable controller) {
        this.controller = controller;
    }
}