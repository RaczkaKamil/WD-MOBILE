package com.wsiz.wirtualny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    TextView tf_login;
    TextView tf_password;
    TextView tf_info;
    Button bt_login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tf_login = findViewById(R.id.tf_login);
        tf_password = findViewById(R.id.tf_password);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        tf_info = findViewById(R.id.tf_info);
        tf_info.setAlpha(0f);
        bt_login = findViewById(R.id.bt_login);
            bt_login.setOnClickListener(view -> {


                login();
                progressBar.setVisibility(View.VISIBLE);

         });

    }

    private void login(){
        String login = tf_login.getText().toString();
        String haslo = tf_password.getText().toString();
        String zaszyfrowaneHasło = md5(haslo);
        sendPost(login,zaszyfrowaneHasło);
    }

    private void Auth(boolean succes,boolean connectFalsse){

        this.runOnUiThread(new Runnable() {
            public void run() {
                tf_info.setAlpha(0f);
        if(succes && !connectFalsse){
            tf_info.setText("Zalogowano!");
            tf_info.setTextColor(Color.GREEN);
            tf_info.animate().alpha(1f).setDuration(500);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);

        }else if(!succes&&!connectFalsse) {
            progressBar.setVisibility(View.GONE);
            tf_info.setText("Błędny Login lub Hasło!");
            tf_info.setTextColor(Color.RED);
            tf_info.animate().alpha(1f).setDuration(500);
        }else if(!succes&& connectFalsse){
            progressBar.setVisibility(View.GONE);
            tf_info.setText("Błąd serwera!");
            tf_info.setTextColor(Color.RED);
            tf_info.animate().alpha(1f).setDuration(500);
        }
            }
        });
    }
private  void saveAccount(String login, String password){
    this.runOnUiThread(new Runnable() {
        public void run() {

            try {
                FileOutputStream fileOutputStream = null;
                fileOutputStream = getApplicationContext().openFileOutput("Account", Context.MODE_PRIVATE);
                fileOutputStream.write(login.getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(password.getBytes());
                fileOutputStream.close();
                System.out.println("-------------------ZAPISANO KONTO-----------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

}
    private  void saveToken(String token){
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Uzyskano dostęp", Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = getApplicationContext().openFileOutput("Token", Context.MODE_PRIVATE);
                    fileOutputStream.write(token.getBytes());
                    fileOutputStream.close();
                    System.out.println("-------------------ZAPISANO TOKEN-----------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("LISTA PLIKOW: ");
                for (int i = 0; i < fileList().length; i++) {
                    System.out.println(i+". "+ fileList()[i]);
                }
            }
        });

    }
    public void sendPost(String login, String haslo) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" +login + "&pass="+haslo);

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
                        if(line.length() == 36){
                            saveToken(line);
                            saveAccount(login, haslo);
                            Auth(true,false);
                        }else{

                           Auth(false,false);
                        }
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    Auth(false,true);
                    e.printStackTrace();
                }
            }
        });
        thread.start();



    }


    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
