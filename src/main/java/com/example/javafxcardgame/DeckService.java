package com.example.javafxcardgame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class DeckService {
    private final String url;
    private final int deckCount;

    DeckService(int deckCount){
        this.deckCount = deckCount;
        this.url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=" + deckCount;
    }

    private String getDeckID() throws IOException, ParseException {
        HTTPConnector connector = new HTTPConnector(new URL(url));
        ParseToJSON parser = new ParseToJSON(connector.contentToString());

        JSONObject data_obj = parser.getJsonObject();

        return data_obj.get("deck_id").toString();
    }
    public CardDeck getCardDeck() throws IOException, ParseException {
        CardDeck deck = new CardDeck();
        URL requestUrl = new URL("https://deckofcardsapi.com/api/deck/" + getDeckID() + "/draw/?count=" + (deckCount * 52));

        HTTPConnector connector = new HTTPConnector(requestUrl);
        ParseToJSON parser = new ParseToJSON(connector.contentToString());

        JSONObject data_obj = parser.getJsonObject();
        JSONArray jsonArray = (JSONArray) data_obj.get("cards");

        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            Card card = new Card(jsonObject.get("code").toString(), jsonObject.get("value").toString(),jsonObject.get("suit").toString(),jsonObject.get("image").toString());
            deck.saveToDeck(card);
        }
        return deck;
    }
    public CardDeck reshuffleDeck() throws IOException, ParseException {
        URL requestUrl = new URL("https://deckofcardsapi.com/api/deck/"+ getDeckID() +"/shuffle/");
        HTTPConnector connector = new HTTPConnector(requestUrl);

        return getCardDeck();
    }
}
