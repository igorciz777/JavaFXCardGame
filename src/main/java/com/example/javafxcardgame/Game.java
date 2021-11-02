package com.example.javafxcardgame;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private DeckService deckService;
    private CardDeck deck;
    private int counter;
    private final Scanner scanner = new Scanner(System.in);
    private List<Card> playerCardList;
    private List<Card> dealerCardList;
    private int playerMoney;
    private int playerBet;
    private int roundCounter=0;
    private int winCounter=0;
    private int loseCounter=0;


    public void start(int deckNum) throws IOException, ParseException {
        startNewGame(deckNum);
        System.out.println("Current money: "+getPlayerMoney());
        startRound(getPlayerBet());
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
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Dealer cards:");
        dealerCardList.forEach(card -> System.out.println(card.getValue() + " " + card.getSuit()));
        System.out.println("Total value: " + getDealerValue());
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Your cards:");
        playerCardList.forEach(card -> System.out.println(card.getValue() + " " + card.getSuit()));
        System.out.println("Total value: " +  + getPlayerValue());
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Your money: " + getPlayerMoney());
        System.out.println("Cards in deck remaining: " + (deck.getCardDeckList().size() - counter));
        System.out.println("---------------------------------------------------------------------------");
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }
    public int getPlayerBet(){
        System.out.println("How much do you want to bet?: ");
        return scanner.nextInt();
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
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public CardDeck getDeck() {
        return deck;
    }

    public void setDeck(CardDeck deck) {
        this.deck = deck;
    }

    @Override
    public String toString() {
        return "Game{" +
                "deckService=" + deckService +
                ", deck=" + deck +
                ", counter=" + counter +
                ", scanner=" + scanner +
                ", playerCardList=" + playerCardList +
                ", dealerCardList=" + dealerCardList +
                ", playerMoney=" + playerMoney +
                ", playerBet=" + playerBet +
                '}';
    }
}
