package com.mycompany.tictactoegame;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class App extends Application {
    private static volatile JoystickReader js0Reader;
    private static volatile JoystickReader js1Reader;

    @Override
    public void start(Stage stage) throws IOException {
        MusicController.initializeMusic();        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
        Parent root = loader.load();
        
        Object controller = loader.getController();
        if (controller instanceof JoystickControllable) {
            JoystickManager.startHotplugDetection((JoystickControllable) controller);
        }
        
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static synchronized void initializeJoysticks(JoystickControllable controller) {
        try {
            // Initialize primary joystick if available
            if (new File("/dev/input/js0").exists()) {
                if (js0Reader == null || !js0Reader.isRunning()) {
                    if (js0Reader != null) {
                        js0Reader.stop();
                    }
                    js0Reader = new JoystickReader(controller, "/dev/input/js0", 0);
                    js0Reader.start();
                } else {
                    js0Reader.setController(controller);
                }
            }

            // Initialize secondary joystick if needed and available
            if (controller.requiresSecondJoystick() && new File("/dev/input/js1").exists()) {
                if (js1Reader == null || !js1Reader.isRunning()) {
                    if (js1Reader != null) {
                        js1Reader.stop();
                    }
                    js1Reader = new JoystickReader(controller, "/dev/input/js1", 1);
                    js1Reader.start();
                } else {
                    js1Reader.setController(controller);
                }
            }
        } catch (Exception e) {
            System.err.println("Joystick initialization failed: " + e.getMessage());
        }
    }
    public static synchronized JoystickReader getPrimaryJoystickReader() {
        return js0Reader;
    }

    public static synchronized JoystickReader getSecondaryJoystickReader() {
        return js1Reader;
    }

    public static synchronized void updateJoystickController(JoystickControllable controller) {
        if (js0Reader != null) {
            js0Reader.setController(controller);
        }
        if (js1Reader != null) {
            js1Reader.setController(controller);
        }
    }

    @Override
    public void stop() {
        JoystickManager.stopHotplugDetection();
        if (js0Reader != null) js0Reader.stop();
        if (js1Reader != null) js1Reader.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}