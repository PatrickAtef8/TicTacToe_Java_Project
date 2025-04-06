package com.mycompany.tictactoegame.utils.input;

import com.mycompany.tictactoegame.App;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class JoystickManager {
    private static final long POLL_INTERVAL = 1000; // 1 second
    private static final long ALERT_DISPLAY_TIME = 2000; // 2 seconds
    private static Timer detectionTimer;
    private static boolean js0Connected = false;
    private static boolean js1Connected = false;
    private static JoystickControllable currentController;
    
    public static void startHotplugDetection(JoystickControllable controller) {
        stopHotplugDetection();
        currentController = controller;
        
        // Initial check
        checkJoysticks();
        
        detectionTimer = new Timer(true);
        detectionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkJoysticks();
            }
        }, POLL_INTERVAL, POLL_INTERVAL);
    }
    
    public static void updateController(JoystickControllable controller) {
        currentController = controller;
        if (App.getPrimaryJoystickReader() != null) {
            App.getPrimaryJoystickReader().setController(controller);
        }
        if (App.getSecondaryJoystickReader() != null) {
            App.getSecondaryJoystickReader().setController(controller);
        }
    }
    
    public static void stopHotplugDetection() {
        if (detectionTimer != null) {
            detectionTimer.cancel();
            detectionTimer = null;
        }
    }
    
    private static void checkJoysticks() {
        if (currentController == null) return;
        
        boolean currentJs0 = new File("/dev/input/js0").exists();
        boolean currentJs1 = new File("/dev/input/js1").exists();
        
        if (currentJs0 != js0Connected) {
            js0Connected = currentJs0;
            if (js0Connected) {
                System.out.println("Joystick 0 connected - initializing");
                showConnectionAlert("Joystick 1 Connected", "Primary joystick has been connected");
                App.initializeJoysticks(currentController);
            } else {
                System.out.println("Joystick 0 disconnected");
                showConnectionAlert("Joystick 1 Disconnected", "Primary joystick has been disconnected");
                if (App.getPrimaryJoystickReader() != null) {
                    App.getPrimaryJoystickReader().stop();
                }
            }
        }
        
        if (currentJs1 != js1Connected && currentController.requiresSecondJoystick()) {
            js1Connected = currentJs1;
            if (js1Connected) {
                System.out.println("Joystick 1 connected - initializing");
                showConnectionAlert("Joystick 2 Connected", "Secondary joystick has been connected");
                App.initializeJoysticks(currentController);
            } else {
                System.out.println("Joystick 1 disconnected");
                showConnectionAlert("Joystick 2 Disconnected", "Secondary joystick has been disconnected");
                if (App.getSecondaryJoystickReader() != null) {
                    App.getSecondaryJoystickReader().stop();
                }
            }
        }
    }
    
    private static void showConnectionAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            
            // Apply custom styling
            alert.getDialogPane().setStyle("-fx-background-color: #2B0B33; -fx-border-color: yellow;");
            alert.getDialogPane().lookup(".content.label").setStyle(
                "-fx-text-fill: yellow; -fx-font-size: 18px; -fx-font-weight: bold;");
            
            // Show the alert
            alert.show();
            
            // Create a timer to close the alert after 2 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> alert.close());
                    timer.cancel();
                }
            }, ALERT_DISPLAY_TIME);
        });
    }
}