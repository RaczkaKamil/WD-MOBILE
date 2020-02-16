package com.wsiz.wirtualny.ui.oceny;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.wsiz.wirtualny.LoginActivity;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.ui.CustomAdapter2;
import com.wsiz.wirtualny.ui.JsonLectures;
import com.wsiz.wirtualny.ui.JsonNotes;
import com.wsiz.wirtualny.ui.Pocket.TokenPocket;
import com.wsiz.wirtualny.ui.Pocket.UserIDPocket;
import com.wsiz.wirtualny.ui.Translator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class OcenyFragment extends Fragment {
    String token;
    TabLayout tabLayout;
    TabItem sem1;
    TabItem sem2;
    TabItem sem3;
    TabItem sem4;
    TabItem sem5;
    TabItem sem6;
    TabItem sem7;
    ProgressBar progressBar3;
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    CustomAdapter2 customAdapterr;
    JsonNotes[] jsonNotes;
    JsonLectures[] jsonLectures;
    ArrayList<String> semestrList =  new ArrayList<String>();
    ImageView btn_option;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_oceny, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        sem1 = root.findViewById(R.id.sem1);
        sem2= root.findViewById(R.id.sem2);
        sem3 = root.findViewById(R.id.sem3);
        sem4 = root.findViewById(R.id.sem4);
        sem5 = root.findViewById(R.id.sem5);
        sem6 = root.findViewById(R.id.sem6);
        sem7 = root.findViewById(R.id.sem7);

        progressBar3 = root.findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.VISIBLE);

        customAdapterr = new CustomAdapter2(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.online_list2);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);
        customAdapterr.notifyDataSetChanged();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(progressBar3.getVisibility()==View.GONE){
                    System.out.println("Selected: " + tab.getPosition());
                    selectedTab(tab.getPosition(),Long.valueOf(semestrList.get(tab.getPosition())));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        UserIDPocket userIDPocket = new UserIDPocket();
        userIDPocket.startRead(getContext());
        int studentID=userIDPocket.getStudentid();

        TokenPocket tokenPocket = new TokenPocket();
        tokenPocket.startRead(getContext());
        token = tokenPocket.getToken();


        connectNews(studentID,token);
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

   public void connectNews(int id,String token){
       Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/student/" +id+"/notes?wdauth="+token);
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
                       Gson gson= new Gson();
                       jsonNotes = gson.fromJson(line, JsonNotes[].class);
                       setJson(jsonNotes);
                       System.out.println("ilosc ocen: " + jsonNotes.length);
                   }

                   conn.disconnect();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
       thread.start();
   }

    public void setJson(JsonNotes[] jsonNotes) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                int count = 1;
                long semestrid=0;

                System.out.println("SEMESTR ID: ");
                for (int i = 0; i < jsonNotes.length; i++) {
                    if(semestrid==0){
                        semestrid=jsonNotes[i].getSemestrid();
                        semestrList.add(String.valueOf(semestrid));
                    }

                    if(semestrid!=jsonNotes[i].getSemestrid()){
                        semestrid=jsonNotes[i].getSemestrid();
                        semestrList.add(String.valueOf(semestrid));
                        count++;
                    }
                }
                System.out.println("Ilosc semestrów: " + count);
                System.out.println("Ostatni semestr: " + semestrid);
                removeTab(count,semestrid);
            }
        });
    }

   public void removeTab(int lastId,long lastSemestr){
       for (int i = 0; i < 6; i++) {
           try{
               tabLayout.removeTab(tabLayout.getTabAt(lastId));
           }catch (NullPointerException e){
               e.fillInStackTrace();
           }

       }
       tabLayout.getTabAt(lastId-1).select();
       selectedTab(lastId-1,lastSemestr);
   }

   public void selectedTab(int tab, long semestr){
       System.out.println("Wybrano: " + tab);
       int count = 0;
       Translator translator = new Translator(0);
       for (int i = 0; i < jsonNotes.length; i++) {
           if(jsonNotes[i].getSemestrid()==semestr){
               translator.setNotesIn(jsonNotes[i].getOcenatypid());
               System.out.println("Przedmiot: "+jsonNotes[i].getPrzedmiotid()+", ocena: "+ translator.getNotesOut()  +", termin: "+jsonNotes[i].getTerminid());
               count++;
           }
       }
       System.out.println("ilosc ocen w semestrze: " + count);
       connectLectures(token,semestr);
   }


    public void connectLectures(String token,long semestr){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/lectures?wdauth=" +token);
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
                        System.out.println("LECTURES: ");
                        System.out.println(line);
                        Gson gson= new Gson();
                        jsonLectures = gson.fromJson(line, JsonLectures[].class);
                        setJsonLectures(jsonLectures,semestr);
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



    public void setJsonLectures(JsonLectures[] jsonLectures,long semestr) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                MessageslistOfString.clear();
                Translator translator = new Translator(0);
                for (int i = 0; i < jsonNotes.length; i++) {
                    for (int j = 0; j < jsonLectures.length; j++) {
                        if(jsonNotes[i].getPrzedmiotid()==jsonLectures[j].getPrzedmiotid()){
                            if(jsonNotes[i].getSemestrid()==semestr){
                                double t0 = 0.0;
                                double t1 =0.0;
                                double t2=0.0;
                                if(jsonNotes[i].getTerminid() == 1 ){
                                    translator.setNotesIn(jsonNotes[i].getOcenatypid());
                                    t0=translator.getNotesOut();
                                }else if(jsonNotes[i].getTerminid() == 2 ){
                                    translator.setNotesIn(jsonNotes[i].getOcenatypid());
                                    t1=translator.getNotesOut();
                                }else  if(jsonNotes[i].getTerminid() == 3){
                                    translator.setNotesIn(jsonNotes[i].getOcenatypid());
                                    t2=translator.getNotesOut();
                                }

                                System.out.println("dodano: "+jsonLectures[j].getNazwa()+"~~"+t0+"~~"+t1+"~~"+t2+"~~"+"0.0");
                                MessageslistOfString.add(jsonLectures[j].getNazwa()+"~~"+t0+"~~"+t1+"~~"+t2+"~~"+"0.0");
                                customAdapterr.notifyDataSetChanged();
                                progressBar3.setVisibility(View.GONE);
                            }
                        }
                    }
                }



            }
        });


    }
}