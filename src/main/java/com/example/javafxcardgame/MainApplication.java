package com.example.javafxcardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainApplication extends Application {
    Stage stage;
    Scene scene;
    MainController mainController;
    private DeckService deckService;
    private CardDeck deck;
    private int counter;
    private final Scanner scanner = new Scanner(System.in);
    private List<Card> playerCardList;
    private List<Card> dealerCardList;
    private HBox dealerCardsHBox;
    private HBox playerCardsHBox;
    private Text moneyText;
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

        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();

        Button startButton = (Button) scene.lookup("#startButton");
        TextField textField = (TextField) scene.lookup("#deckNumField");
        Text errorText = (Text) scene.lookup("#errorText");
        startButton.setOnAction(actionEvent -> {
            if(checkField(textField)){
                try {
                    startNewGame(Integer.parseInt(textField.getText()));
                    changeView();
                    startRound(getPlayerBet());
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }else {
                errorText.setText("Enter correct number of decks!");
                errorText.setStyle("-fx-font-size:20px");
            }
        });
    }
    public boolean checkField(TextField textField){
        try{
            int num = Integer.parseInt(textField.getText());
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public void quit(){
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Game Over!");
        System.out.println("Money: "+getPlayerMoney());
        System.out.println("Rounds played: "+roundCounter);
        System.out.println("Rounds won: "+winCounter);
        System.out.println("Rounds lost: "+loseCounter);
        System.out.println("---------------------------------------------------------------------------");
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
    private void startRound(int bet){
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
    private void playerHit(){
        checkIfShuffle();
        playerCardList.add(drawCard());
        if(getPlayerValue() >= 21){
            printInfo();
            checkWin();
        }else{
            continueRound();
        }
    }
    private void playerStand(){
        if(getPlayerValue() <= getDealerValue()){
            System.err.println("Cant stand");
            playerHit();
        }else {
            dealerHit();
        }
    }
    private void dealerHit(){
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
        dealerCardList.forEach(card -> dealerCardsHBox.getChildren().add(new ImageView(new Image(card.getImage()))));
        //System.out.println("Total value: " + getDealerValue());
        playerCardList.forEach(card -> playerCardsHBox.getChildren().add(new ImageView(new Image(card.getImage()))));
        //System.out.println("Total value: " +  + getPlayerValue());
        moneyText.setText("Your money: " + getPlayerMoney());
        //System.out.println("Cards in deck remaining: " + (deck.getCardDeckList().size() - counter));
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }
    public int getPlayerBet(){
        TextInputDialog inputDialog = new TextInputDialog("Enter a number");
        inputDialog.setHeaderText("How much money do you want to bet?");
        inputDialog.setContentText("Your money: " + playerMoney);
        inputDialog.showAndWait();
        return Integer.parseInt(inputDialog.getEditor().getText());
    }
    private void canContinue(){
        if(getPlayerMoney() > 0){
            startRound(getPlayerBet());
        }
        else{
            quit();
        }
    }
    private void checkIfShuffle() {
        if((deck.getCardDeckList().size() - counter) <= 4){
            System.out.println("Reshuffling the deck!");
            counter=0;
            try {
                deck = deckService.reshuffleDeck();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void checkWin(){
        if(getPlayerValue() > 21 || (getPlayerValue() < getDealerValue() && getDealerValue() <= 21)){
            System.err.println("You lose!");
            loseCounter++;
            System.out.println("Your money: "+getPlayerMoney());
        }
        else if(getPlayerValue() == 21 || getDealerValue() > 21 || getPlayerValue() > getDealerValue()){
            System.out.println("You win!");
            winCounter++;
            playerMoney += playerBet * 2;
            System.out.println("Your money: "+getPlayerMoney());
        }
        canContinue();
    }
    private void continueRound() {
        checkIfShuffle();
        printInfo();
        System.out.println("STAND,HIT or QUIT game?");
        switch (scanner.next().toUpperCase()) {
            case "STAND":
                playerStand();
                break;
            case "HIT":
                playerHit();
                break;
            case "QUIT":
                quit();
                break;
            default:
                System.out.println("Wrong option, try again");
                continueRound();
                break;
        }
    }
    private void changeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-view.fxml"));
        Parent parent = fxmlLoader.load();
        mainController = fxmlLoader.getController();
        dealerCardsHBox = mainController.getDealerCardsHBox();
        playerCardsHBox = mainController.getYourCardsHBox();
        moneyText = mainController.getMoneyText();
        stage.setScene(new Scene(parent,800,800));
    }
}
