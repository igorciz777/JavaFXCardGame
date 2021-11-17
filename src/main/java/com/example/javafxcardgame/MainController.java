package com.example.javafxcardgame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    public Label moneyText;
    @FXML
    public Label errorText;
    @FXML
    public HBox dealerCardsHBox;
    @FXML
    public HBox yourCardsHBox;
    public Text playerValue;
    public Text dealerValue;
    public Text cardCount;
    public Button standButton;
    public HBox buttonHBox;
    public Button hitButton;
    public Button quitButton;
    public Text endMoney;
    public Text roundsPlayed;
    public Text roundsWon;
    public Text roundsLost;
    public Button resetButton;


    public void initialize(){

    }

    public Label getMoneyText() {
        return moneyText;
    }

    public HBox getDealerCardsHBox() {
        return dealerCardsHBox;
    }

    public HBox getYourCardsHBox() {
        return yourCardsHBox;
    }

    public Text getPlayerValue() {
        return playerValue;
    }

    public Text getDealerValue() {
        return dealerValue;
    }

    public Text getCardCount() {
        return cardCount;
    }

    public Button getStandButton() {
        return standButton;
    }

    public Button getHitButton() {
        return hitButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }

    public Text getEndMoney() {
        return endMoney;
    }

    public Text getRoundsPlayed() {
        return roundsPlayed;
    }

    public Text getRoundsWon() {
        return roundsWon;
    }

    public Text getRoundsLost() {
        return roundsLost;
    }

    public Button getResetButton() {
        return resetButton;
    }
}
