package com.wsiz.wirtualny.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.SelectedActivity;
import com.wsiz.wirtualny.ui.JsonArray;
import com.wsiz.wirtualny.ui.JsonObiect;
import com.wsiz.wirtualny.ui.TokenPocket;
import com.wsiz.wirtualny.ui.JsonNews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    CustomAdapter customAdapterr;
    JsonNews[] jsonNews;
    String token;
    JsonObiect jsonObiects;
    JsonArray jsonArray = new JsonArray();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aktualnosci, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        textView.setText("Wiadomości: ");

        customAdapterr = new CustomAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.online_list);
        online_list.setAdapter(customAdapterr);

        online_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ll = (int) l;
                Toast.makeText(getContext(), "Wybrano element: "+ll, Toast.LENGTH_SHORT).show();
            }
        });
        customAdapterr.notifyDataSetChanged();
        TokenPocket tokenPocket = new TokenPocket();
        tokenPocket.startRead(getContext());
        token = tokenPocket.getToken();
        connectNews(token);

        online_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ll = (int) l;
                connectToChosedMessage(ll);
            }
        });

        return root;
    }

    private void connectToChosedMessage(int number){
        Intent intent = new Intent(getActivity(), SelectedActivity.class);
        String btmS[] = new String[6];
            btmS[0]= jsonNews[number].getTytyl();
            btmS[1]= String.valueOf(jsonNews[number].getDataut());
            btmS[2]= jsonNews[number].getTresc();
            btmS[3]= String.valueOf(jsonNews[number].getOgloszenieid());
            btmS[4]=  jsonNews[number].getFilename();
            btmS[5]= jsonNews[number].getFileuuid();

        intent.putExtra("select", btmS);
        startActivity(intent);
    }

    public void connectNews(String token)  {
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
                        Gson gson = new Gson();
                        jsonNews = gson.fromJson(line, JsonNews[].class);
                        setJson(jsonNews);
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void setJson(JsonNews[] jsonNews) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < jsonNews.length; i++) {
                    MessageslistOfString.add(jsonNews[i].getTytyl() +"\n" + jsonNews[i].getDataut());
                }
                customAdapterr.notifyDataSetChanged();
            }
        });



    }
}