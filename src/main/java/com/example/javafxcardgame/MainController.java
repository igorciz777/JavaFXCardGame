package com.example.javafxcardgame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    public Button startButton;
    @FXML
    public TextField deckNumField;
    @FXML
    public VBox opponentHBox;
    @FXML
    public VBox playerHBox;
    @FXML
    public Text moneyText;
    @FXML
    public Text errorText;
    @FXML
    public HBox dealerCardsHBox;
    @FXML
    public HBox yourCardsHBox;


    public void initialize(){

    }

    public Button getStartButton() {
        return startButton;
    }

    public TextField getDeckNumField() {
        return deckNumField;
    }

    public VBox getOpponentHBox() {
        return opponentHBox;
    }

    public VBox getPlayerHBox() {
        return playerHBox;
    }

    public Text getMoneyText() {
        return moneyText;
    }

    public Text getErrorText() {
        return errorText;
    }

    public HBox getDealerCardsHBox() {
        return dealerCardsHBox;
    }

    public HBox getYourCardsHBox() {
        return yourCardsHBox;
    }
}
