package com.example.javafxcardgame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    public Button startButton;
    @FXML
    private void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),800,800);
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setScene(scene);
    }
}
