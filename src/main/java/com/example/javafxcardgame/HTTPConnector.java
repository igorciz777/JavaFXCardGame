package com.example.javafxcardgame;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HTTPConnector {
    private URL url;
    private HttpURLConnection connection;
    private Scanner scanner;

    HTTPConnector(URL url) throws IOException {
        this.url = url;
        scanner = new Scanner(url.openStream());
        connect();
    }
    private void connect(){
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public int getResponseCode() {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public URL getUrl() {
        return url;
    }

    public String contentToString(){
        String inline = "";
        while(scanner.hasNext()){
            inline += scanner.nextLine();
        }
        scanner.close();
        return inline;
    }
}
