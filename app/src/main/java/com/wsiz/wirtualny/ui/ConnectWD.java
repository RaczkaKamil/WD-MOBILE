package com.wsiz.wirtualny.ui;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ConnectWD {
String addresNews = "https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=";
String addresLogin = "https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=";
String addresLogin2= "&pass=";
JsonNews[] json;
boolean done = false;
Context ctx;


    public void startConnection(String action, String token,Context ctx){
        this.ctx = ctx;
    }

    public boolean getDone(){
        return  done;
    }
    public JsonNews[] getJson(){
        return json;
    }

    public JsonNews[] connectNews(String token) throws InterruptedException {
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
                        Gson gson = new Gson();
                        JsonNews[] jsonNews = gson.fromJson(line, JsonNews[].class);

                                json = jsonNews;
                                done=true;
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.join();


        return json;
    }

}
