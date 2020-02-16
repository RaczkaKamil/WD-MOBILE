package com.wsiz.wirtualny.ui.finanse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wsiz.wirtualny.LoginActivity;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.ui.CustomAdapter2;
import com.wsiz.wirtualny.ui.CustomAdapterFinances;
import com.wsiz.wirtualny.ui.JsonFinances;
import com.wsiz.wirtualny.ui.JsonNotes;
import com.wsiz.wirtualny.ui.Pocket.TokenPocket;
import com.wsiz.wirtualny.ui.Pocket.UserIDPocket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class FinanseFragment extends Fragment {
String token;
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    CustomAdapterFinances customAdapterr;
    JsonFinances[] jsonFinances;
    ImageView btn_option;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_finanse, container, false);
        UserIDPocket userIDPocket = new UserIDPocket();
        userIDPocket.startRead(getContext());
        int finID=userIDPocket.getFinid();

        customAdapterr = new CustomAdapterFinances(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.finanse_list);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);
        customAdapterr.notifyDataSetChanged();


        TokenPocket tokenPocket = new TokenPocket();
        tokenPocket.startRead(getContext());
        token = tokenPocket.getToken();


        connectFin(String.valueOf(finID),token);

        btn_option= root.findViewById(R.id.btn_option);

        btn_option.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), btn_option);
            popup.getMenuInflater().inflate(R.menu.upper_nav_menu, popup.getMenu());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    System.out.println("WYLOGOWANO");
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("login","dont");
                    startActivity(intent);
                    return true;
                }
            });

            try {
                Field mFieldPopup=popup.getClass().getDeclaredField("mPopup");
                mFieldPopup.setAccessible(true);
                MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                mPopup.setForceShowIcon(true);
            } catch (Exception e) {

            }

            popup.show();//showing popup menu

        });

        return root;
    }

    public void connectFin(String finid,String token){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/fin/txs/"+finid+"?wdauth=" +token);
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
                        System.out.println(line);
                        Gson gson = new Gson();
                        jsonFinances = gson.fromJson(line, JsonFinances[].class);
                        setJson(jsonFinances);
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void setJson(JsonFinances[] jsonFinances){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < jsonFinances.length; i++) {
                    MessageslistOfString.add(
                            jsonFinances[i].getDate()+"~~"+
                            jsonFinances[i].getType()+"~~"+
                            jsonFinances[i].getDetails()+"~~"+
                            jsonFinances[i].getAmount()+" zł"+"~~");
                    customAdapterr.notifyDataSetChanged();
                }

            }
        });
    }
}

