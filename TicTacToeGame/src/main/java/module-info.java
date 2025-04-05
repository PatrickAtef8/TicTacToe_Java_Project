module com.mycompany.tictactoegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    //requires javafx.media; 
    
    opens com.mycompany.tictactoegame to javafx.fxml;
    exports com.mycompany.tictactoegame;
    requires jinput;
}
