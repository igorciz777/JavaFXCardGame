module com.example.javafxcardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.javafxcardgame to javafx.fxml;
    exports com.example.javafxcardgame;
}