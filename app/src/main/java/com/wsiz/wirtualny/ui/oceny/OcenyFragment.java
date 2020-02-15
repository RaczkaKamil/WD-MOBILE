package com.wsiz.wirtualny.ui.oceny;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.ui.CustomAdapter2;
import com.wsiz.wirtualny.ui.Pocket.TokenPocket;
import com.wsiz.wirtualny.ui.Pocket.UserIDPocket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    CustomAdapter2 customAdapterr;


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


        customAdapterr = new CustomAdapter2(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.online_list2);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);


        MessageslistOfString.add("Elementy architektury komputerów - w. ~~"+" ~~"+" ~~"+"3~~"+" ~~");
        MessageslistOfString.add("Architektura systemów komputerowych - w. ~~"+"4~~"+" ~~"+" ~~"+" ~~");
        MessageslistOfString.add("Język angielski 1 (poziom 2) - lektorat ~~"+" ~~"+"5~~"+" ~~"+" ~~");
        MessageslistOfString.add("Podstawy programowania obiektowego- w. ~~"+"3~~"+"5~~"+" ~~"+" ~~");
        MessageslistOfString.add("Praktyka zawodowa_3_Ipol - w. ~~"+" ~~"+"3~~"+" ~~"+"5~~");
        MessageslistOfString.add("Praktyka- w. ~~"+" ~~"+"3~~"+" ~~"+"5~~");
        customAdapterr.notifyDataSetChanged();

        tabLayout.getTabAt(2).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("Selected: " + tab.getPosition());
                selectedTab(tab.getPosition());
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

                   }

                   conn.disconnect();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
       thread.start();
   }

   public void removeTab(int lastId){
       for (int i = 0; i < 6; i++) {
           try{
               tabLayout.removeTab(tabLayout.getTabAt(lastId));
               System.out.println("usunieto: "+i);
           }catch (NullPointerException e){
               e.fillInStackTrace();
           }

       }

   }

   public void selectedTab(int tab){
       System.out.println("Wybrano: " + tab);
       MessageslistOfString.clear();
       MessageslistOfString.add("Elementy architektury komputerów - w. ~~"+" ~~"+" ~~"+"3~~"+" ~~");
       MessageslistOfString.add("Architektura systemów komputerowych - w. ~~"+"4~~"+" ~~"+" ~~"+" ~~");
       MessageslistOfString.add("Język angielski 1 (poziom 2) - lektorat ~~"+" ~~"+"5~~"+" ~~"+" ~~");
       MessageslistOfString.add("Podstawy programowania obiektowego- w. ~~"+"3~~"+"5~~"+" ~~"+" ~~");
       MessageslistOfString.add("Praktyka zawodowa_3_Ipol - w. ~~"+" ~~"+"3~~"+" ~~"+"5~~");
       MessageslistOfString.add("Praktyka- w. ~~"+" ~~"+"3~~"+" ~~"+"5~~");
       customAdapterr.notifyDataSetChanged();
   }
}