package com.mycompany.tictactoegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class App extends Application {
    private static JoystickReader js0Reader;
    private static JoystickReader js1Reader;

    @Override
    public void start(Stage stage) throws IOException {
        
                MusicController.initializeMusic();        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuUI.fxml"));
        Parent root = loader.load();
        
        // Safe initialization
        Object controller = loader.getController();
        if (controller instanceof JoystickControllable) {
            initializeJoysticks((JoystickControllable) controller);
        }
        
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void initializeJoysticks(JoystickControllable controller) {
        try {
            // Clean up existing readers
            if (js0Reader != null) js0Reader.stop();
            if (js1Reader != null) js1Reader.stop();

            // Initialize primary joystick
            js0Reader = new JoystickReader(controller, "/dev/input/js0", 0);
            new Thread(js0Reader).start();

            // Initialize secondary joystick if needed
            if (controller.requiresSecondJoystick()) {
                js1Reader = new JoystickReader(controller, "/dev/input/js1", 1);
                new Thread(js1Reader).start();
            }
        } catch (Exception e) {
            System.err.println("Joystick initialization failed: " + e.getMessage());
        }
    }

    // Add these new methods to access the joystick readers
    public static JoystickReader getPrimaryJoystickReader() {
        return js0Reader;
    }

    public static JoystickReader getSecondaryJoystickReader() {
        return js1Reader;
    }

    public static void updateJoystickController(JoystickControllable controller) {
        if (js0Reader != null) {
            js0Reader.setController(controller);
        }
        if (js1Reader != null) {
            js1Reader.setController(controller);
        }
    }

    @Override
    public void stop() {
        if (js0Reader != null) js0Reader.stop();
        if (js1Reader != null) js1Reader.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}