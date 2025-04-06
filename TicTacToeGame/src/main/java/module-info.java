module com.mycompany.tictactoegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jinput;  // Warning: jinput is an automatic module (filename-based)
    
    opens com.mycompany.tictactoegame to javafx.fxml;
    opens com.mycompany.tictactoegame.controllers to javafx.fxml; 
opens com.mycompany.tictactoegame.utils.input to javafx.fxml;
opens com.mycompany.tictactoegame.model to javafx.fxml;
    exports com.mycompany.tictactoegame;
}