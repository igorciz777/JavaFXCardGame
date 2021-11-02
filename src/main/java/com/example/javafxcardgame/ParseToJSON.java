package com.example.javafxcardgame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParseToJSON {
    private JSONParser parser;
    private String inline;

    ParseToJSON(String inline){
        this.inline = inline;
        parser = new JSONParser();
    }
    public JSONObject getJsonObject() throws ParseException {
        return (JSONObject) parser.parse(inline);
    }
    public JSONArray getJsonArray(JSONObject data_obj, String key){
        return (JSONArray) data_obj.get(key);
    }
}
