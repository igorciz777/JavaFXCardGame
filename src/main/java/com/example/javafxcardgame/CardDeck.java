package com.example.javafxcardgame;

import java.util.ArrayList;
import java.util.List;

public class CardDeck {
    private String deck_id;
    private List<Card> cardDeckList;

    CardDeck(){
        cardDeckList = new ArrayList<>();
    }
    public void saveToDeck(Card card){
        cardDeckList.add(card);
    }

    public String getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(String deck_id) {
        this.deck_id = deck_id;
    }

    public List<Card> getCardDeckList() {
        return cardDeckList;
    }

    public void setCardDeckList(List<Card> cardDeckList) {
        this.cardDeckList = cardDeckList;
    }
}
