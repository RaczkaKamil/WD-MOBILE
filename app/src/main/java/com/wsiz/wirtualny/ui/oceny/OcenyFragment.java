package com.wsiz.wirtualny.ui.oceny;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.ui.JsonNews;
import com.wsiz.wirtualny.ui.TokenPocket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OcenyFragment extends Fragment {
String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_oceny, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        textView.setText("Oceny: ");
        TokenPocket tokenPocket = new TokenPocket();
        tokenPocket.startRead(getContext());
        token = tokenPocket.getToken();
        connectNews(token);
        return root;
    }

   public void connectNews(String token){
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-semestr/semestr?wdauth=" +token);
                   HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                   conn.setDoOutput(true);
                   conn.setDoInput(true);


                   InputStream stream = conn.getInputStream();
                   BufferedReader reader = null;
                   reader = new BufferedReader(new InputStreamReader(stream));

                   StringBuffer buffer = new StringBuffer();
                   String line = "";
                   System.out.println(conn.getResponseMessage());

                   while ((line = reader.readLine()) != null) {
                       buffer.append(line + "\n");
                       Log.d("Response: ", "> " + line);
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