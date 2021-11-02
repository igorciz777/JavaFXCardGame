package com.example.javafxcardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MainApplication extends Application {
    Game game;
    Stage stage;
    MainController mainController;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-menu.fxml"));
        fxmlLoader.setController(mainController);
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);

        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();
    }
}
