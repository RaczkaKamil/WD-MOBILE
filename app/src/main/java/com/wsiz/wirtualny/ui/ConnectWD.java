package com.wsiz.wirtualny.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.wsiz.wirtualny.ui.oceny.JsonNews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ConnectWD {
String addresNews = "https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=";
String addresLogin = "https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=";
String addresLogin2= "&pass=";


    public void startConnection(String action, String token){
        if(action.contains("news")){
            connectNews(token);
        }
    }

    private void connectNews(String token){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=" +token);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);


                    InputStream stream = conn.getInputStream();
                    BufferedReader reader = null;
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        Log.d("Response: ", "> " + line);
                        System.out.println(line);
                        Gson gson = new Gson();
                        JsonNews[] jsonNews = gson.fromJson(line, JsonNews[].class);
                        System.out.println("----------------");
                        System.out.println(jsonNews[0].getOgloszenieid());
                        System.out.println(jsonNews[0].getTytyl());
                        System.out.println(jsonNews[0].getTresc());
                        System.out.println(jsonNews[0].getDataut());

                        //Currency currency = gson.fromJson(line, Currency.class);
                       // System.out.println(currency.rates);
                        //System.out.println(currency.getCurrencyCode());
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();



    }

}
