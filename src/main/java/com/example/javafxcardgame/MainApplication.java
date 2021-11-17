package com.example.javafxcardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainApplication extends Application {
    Stage stage;
    Scene scene;
    MainController mainController;
    private DeckService deckService;
    private CardDeck deck;
    private int counter;
    private List<Card> playerCardList;
    private List<Card> dealerCardList;
    private HBox dealerCardsHBox;
    private HBox playerCardsHBox;
    private Label moneyText;
    private Text playerValue;
    private Text dealerValue;
    private Text cardCount;
    private Button standButton;
    private Button hitButton;
    private Button quitButton;
    private int playerMoney;
    private int playerBet;
    private int roundCounter=0;
    private int winCounter=0;
    private int loseCounter=0;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-menu.fxml"));
        mainController = fxmlLoader.getController();

        scene = new Scene(fxmlLoader.load(), 800, 800);
        scene.getStylesheets().add( Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm() );

        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();

        Button startButton = (Button) scene.lookup("#startButton");
        TextField textField = (TextField) scene.lookup("#deckNumField");
        Label errorText = (Label) scene.lookup("#errorText");
        startButton.setOnAction(actionEvent -> {
            if(checkField(textField) && 0 < Integer.parseInt(textField.getText()) && Integer.parseInt(textField.getText()) < 21){
                try {
                    startNewGame(Integer.parseInt(textField.getText()));
                    changeView();
                    startRound(getPlayerBet());
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }else {
                errorText.setText("Enter correct number of decks!");
            }
        });
    }
    public boolean checkField(TextField textField){
        try{
            Integer.parseInt(textField.getText());
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public void quit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-over.fxml"));
        Parent parent = fxmlLoader.load();
        mainController = fxmlLoader.getController();

        mainController.getEndMoney().setText("Money: "+getPlayerMoney());
        mainController.getRoundsPlayed().setText("Rounds played: "+roundCounter);
        mainController.getRoundsWon().setText("Rounds won: "+winCounter);
        mainController.getRoundsLost().setText("Rounds lost: "+loseCounter);
        mainController.getResetButton().setOnAction(actionEvent -> {
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Scene quitScene = new Scene(parent,800,800);
        quitScene.getStylesheets().add( Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm() );
        stage.setScene(quitScene);
    }
    private void startNewGame(int deckCount) throws IOException, ParseException {
        deckService = new DeckService(deckCount);
        deck = deckService.getCardDeck();
        counter = 0;
        playerMoney = 1000;
    }
    private Card drawCard(){
        return deck.getCardDeckList().get(counter++);
    }
    private void clearLists(){
        playerCardList = new ArrayList<>();
        dealerCardList = new ArrayList<>();
        playerCardsHBox.getChildren().clear();
        dealerCardsHBox.getChildren().clear();
    }
    private void startRound(int bet) throws IOException {
        roundCounter++;
        clearLists();
        playerBet = bet;
        setPlayerMoney(playerMoney - playerBet);
        checkIfShuffle();
        playerCardList.add(drawCard());
        playerCardList.add(drawCard());
        dealerCardList.add(drawCard());
        dealerCardList.add(drawCard());
        if(getPlayerValue() >= 21 || getDealerValue() >= 21){
            printInfo();
            checkWin();
        }else{continueRound();}
    }
    private void playerHit() throws IOException {
        checkIfShuffle();
        playerCardList.add(drawCard());
        if(getPlayerValue() >= 21){
            printInfo();
            checkWin();
        }else{
            continueRound();
        }
    }
    private void playerStand() throws IOException {
        if(getPlayerValue() <= getDealerValue()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Warning!");
            alert.setContentText("Can't Stand! Hitting instead...");
            alert.showAndWait();
            playerHit();
        }else {
            dealerHit();
        }
    }
    private void dealerHit() throws IOException {
        checkIfShuffle();
        dealerCardList.add(drawCard());
        printInfo();
        if(getDealerValue() >= 21){
            checkWin();
        }else if(getDealerValue() > getPlayerValue()){
            checkWin();
        }
        else{
            dealerHit();
        }
    }
    private int getPlayerValue(){
        return playerCardList.stream()
                .mapToInt(Card::getConvertedValue)
                .sum();
    }
    private int getDealerValue(){
        return dealerCardList.stream()
                .mapToInt(Card::getConvertedValue)
                .sum();
    }
    private void printInfo(){
        dealerCardsHBox.getChildren().clear();
        playerCardsHBox.getChildren().clear();
        dealerCardList.forEach(card -> dealerCardsHBox.getChildren().add(new ImageView(new Image(card.getImage(),200,200,true,false))));
        dealerValue.setText("Cards value: " + getDealerValue());
        playerCardList.forEach(card -> playerCardsHBox.getChildren().add(new ImageView(new Image(card.getImage(),200,200,true,false))));
        playerValue.setText("Cards value: " + getPlayerValue());
        moneyText.setText("Your money: " + getPlayerMoney());
        cardCount.setText("Cards in deck remaining: " + (deck.getCardDeckList().size() - counter));
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }
    public int getPlayerBet() throws IOException {
        TextInputDialog inputDialog = new TextInputDialog("Enter a number");
        inputDialog.setHeaderText("How much money do you want to bet?");
        inputDialog.setContentText("Your money: " + playerMoney);
        inputDialog.showAndWait();
        if(inputDialog.getResult() == null){
            quit();
        }
        return Integer.parseInt(inputDialog.getEditor().getText());
    }
    private void canContinue() throws IOException {
        if(getPlayerMoney() > 0){
            startRound(getPlayerBet());
        }
        else{
            quit();
        }
    }
    private void checkIfShuffle() {
        if((deck.getCardDeckList().size() - counter) <= 4){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Warning!");
            alert.setContentText("Reshuffling the deck!");
            alert.showAndWait();
            counter=0;
            try {
                deck = deckService.reshuffleDeck();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void checkWin() throws IOException {
        if(getPlayerValue() > 21 || (getPlayerValue() < getDealerValue() && getDealerValue() <= 21)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("You lose the round!");
            loseCounter++;
            alert.setContentText("Your money: "+getPlayerMoney());
            alert.showAndWait();
        }
        else if(getPlayerValue() == 21 || getDealerValue() > 21 || getPlayerValue() > getDealerValue()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("You win the round!");
            winCounter++;
            playerMoney += playerBet * 2;
            alert.setContentText("Your money: "+getPlayerMoney());
            alert.showAndWait();
        }
        canContinue();
    }
    private void continueRound() {
        checkIfShuffle();
        printInfo();
        standButton.setOnAction(actionEvent -> {
            try {
                playerStand();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hitButton.setOnAction(actionEvent -> {
            try {
                playerHit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        quitButton.setOnAction(actionEvent -> {
            try {
                quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void changeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-view.fxml"));
        Parent parent = fxmlLoader.load();
        mainController = fxmlLoader.getController();
        dealerCardsHBox = mainController.getDealerCardsHBox();
        playerCardsHBox = mainController.getYourCardsHBox();
        moneyText = mainController.getMoneyText();
        playerValue = mainController.getPlayerValue();
        dealerValue = mainController.getDealerValue();
        cardCount = mainController.getCardCount();
        standButton = mainController.getStandButton();
        hitButton = mainController.getHitButton();
        quitButton = mainController.getQuitButton();
        Scene gameScene = new Scene(parent,800,800);
        gameScene.getStylesheets().add( Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm() );
        stage.setScene(gameScene);
    }
}
