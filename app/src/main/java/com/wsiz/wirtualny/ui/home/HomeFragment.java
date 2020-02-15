package com.wsiz.wirtualny.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.wsiz.wirtualny.LoginActivity;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.SelectedActivity;
import com.wsiz.wirtualny.ui.CustomAdapter;
import com.wsiz.wirtualny.ui.Pocket.TokenPocket;
import com.wsiz.wirtualny.ui.JsonNews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class HomeFragment extends Fragment {

    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    ArrayList<String> MessageslistOfString2 = new ArrayList<String>();
    ArrayList<String> MessageslistOfString3 = new ArrayList<String>();
    ArrayList<String> MessageslistOfString4 = new ArrayList<String>();
    CustomAdapter customAdapterr;
    JsonNews[] jsonNews;
    String token;

    ConstraintLayout constraintLayout;
    ProgressBar progressBar2;
    EditText et_search;
    ImageView btn_option;

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aktualnosci, container, false);


        final TextView textView = root.findViewById(R.id.text_home);
        constraintLayout= root.findViewById(R.id.tolbarek);
        progressBar2= root.findViewById(R.id.progressBar2);
        et_search= root.findViewById(R.id.et_search);
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

        progressBar2.setVisibility(View.GONE);
        textView.setText("Wiadomości: ");




        customAdapterr = new CustomAdapter(MessageslistOfString3, getContext());
        final ListView online_list = root.findViewById(R.id.online_list);
        online_list.setAdapter(customAdapterr);
        progressBar2.setVisibility(View.VISIBLE);
        online_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ll = (int) l;
                Toast.makeText(getContext(), "Wybrano element: "+ll, Toast.LENGTH_SHORT).show();
            }
        });

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

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("Kliknieto: " + s.toString());
                searchWord(s.toString());
            }
        });



        return root;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {

                return true;
            }

            case R.id.login: {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return false;
            }


        }
        return super.onOptionsItemSelected(item);
    }



    public void searchWord(String word) {
        MessageslistOfString2.clear();
        MessageslistOfString3.clear();
        MessageslistOfString4.clear();
        for (int i = 0; i < MessageslistOfString.size(); i++) {
            if(MessageslistOfString.get(i).toLowerCase().contains(word)){
                MessageslistOfString2.add(MessageslistOfString.get(i));
            }

        }

        MessageslistOfString3.addAll(MessageslistOfString2);
        System.out.println("DLUGOSC LISY: " + MessageslistOfString3.size());
        customAdapterr.notifyDataSetChanged();



    }


    private void connectToChosedMessage(int number){
        Intent intent = new Intent(getActivity(), SelectedActivity.class);
        String btmS[] = new String[6];
             btmS=MessageslistOfString3.get(number).split("~~");
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
                    MessageslistOfString.add(jsonNews[i].getTytyl() +"~~" + jsonNews[i].getDataut()
                            +"~~"+jsonNews[i].getTresc()+"~~"+jsonNews[i].getOgloszenieid()+"~~"+jsonNews[i].getFilename()+"~~"+jsonNews[i].getFileuuid());

                }


             MessageslistOfString3.addAll(MessageslistOfString);
                System.out.println("DLUGOSC LISY: " + MessageslistOfString3.size());
                customAdapterr.notifyDataSetChanged();
                progressBar2.setVisibility(View.GONE);
            }
        });



    }
}