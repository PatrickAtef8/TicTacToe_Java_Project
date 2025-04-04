package com.mycompany.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private JoystickReader js0Reader, js1Reader;
    private Thread js0Thread, js1Thread;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = fxmlLoader.load();
        PrimaryController controller = fxmlLoader.getController();
        
        initializeJoysticks(controller);
        
        stage.setScene(new Scene(root));
        stage.setTitle("Dual Joystick Tic-Tac-Toe");
        stage.show();
    }

    private void initializeJoysticks(PrimaryController controller) {
        try {
            // Player 1 (js0)
            js0Reader = new JoystickReader(controller, "/dev/input/js0", 0);
            js0Thread = new Thread(js0Reader, "Joystick-0-Thread");
            js0Thread.setDaemon(true);
            js0Thread.start();
            
            // Player 2 (js1)
            js1Reader = new JoystickReader(controller, "/dev/input/js1", 1);
            js1Thread = new Thread(js1Reader, "Joystick-1-Thread");
            js1Thread.setDaemon(true);
            js1Thread.start();
        } catch (Exception e) {
            System.err.println("Joystick initialization failed: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (js0Reader != null) js0Reader.stop();
        if (js1Reader != null) js1Reader.stop();
        
        try {
            if (js0Thread != null) js0Thread.join(100);
            if (js1Thread != null) js1Thread.join(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}