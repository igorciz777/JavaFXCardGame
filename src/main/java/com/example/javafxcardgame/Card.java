package com.example.javafxcardgame;

public class Card {
    private String code;
    private String value;
    private String suit;

    Card(String code, String value, String suit){
        this.code = code;
        this.value = value;
        this.suit = suit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }
    public int getConvertedValue(){
        switch(value){
            case "ACE":
                return 11;
            case "JACK":
            case "KING":
            case "QUEEN":
                return 10;
            default:
                return Integer.parseInt(value);

        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}
