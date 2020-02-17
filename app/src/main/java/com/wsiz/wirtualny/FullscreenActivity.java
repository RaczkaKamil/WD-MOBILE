package com.wsiz.wirtualny;

import android.animation.ObjectAnimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wsiz.wirtualny.ui.JsonUserID;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import static com.wsiz.wirtualny.LoginActivity.md5;

public class FullscreenActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 3000;
    TextView logo2;
    ImageView logo;
    private String login;
    private  String password;
    JsonUserID jsonUserID;
    Boolean connect=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        splashProgress = findViewById(R.id.splashProgress);
        splashProgress.setScaleY(3f);
        logo2 = findViewById(R.id.logo2);
        logo = findViewById(R.id.logo);
        playProgress();
        handDelay();
        logo.animate().alpha(1f).setDuration(2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView()
                .setSystemUiVisibility(mUIFlag);
        setLogin();
    }

    private void setLogin(){
        String data;
        for (int i = 0; i < fileList().length; i++) {
            if(this.fileList()[i].contains("Account")&&!this.fileList()[i].contains("AccountInfo")){
                try {
                    FileInputStream fileInputStream = null;
                    fileInputStream = this.openFileInput(this.fileList()[i]);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();


                    while ((data = bufferedReader.readLine()) != null) {
                        stringBuffer.append(data + "\n");
                        String splited = stringBuffer.toString();
                        String account[]=splited.split("/");
                        this.login=account[0];
                        this.password=account[1].trim();
                        String zaszyfrowaneHasło = md5(this.password);
                        connectLogin(login,zaszyfrowaneHasło,this.password);

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void connectLogin(String login, String md5haslo,String haslo) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" +login + "&pass="+md5haslo);

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
                            connectGetID(line);
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

    private void Auth(boolean succes,boolean connectFalsse){

        this.runOnUiThread(new Runnable() {
            public void run() {
                if(succes && !connectFalsse){
                    connect=true;
                }else if(!succes&&!connectFalsse) {
                    Toast.makeText(getApplicationContext(),"Błąd logowania",Toast.LENGTH_SHORT);
                }else if(!succes&& connectFalsse){
                    Toast.makeText(getApplicationContext(),"Błąd logowania",Toast.LENGTH_SHORT);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void connectGetID(String token) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/user?wdauth=" +token);

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
                        jsonUserID = gson.fromJson(line, JsonUserID.class);
                        saveUser(jsonUserID);
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

    private  void saveToken(String token){
        this.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = getApplicationContext().openFileOutput("Token", Context.MODE_PRIVATE);
                    fileOutputStream.write(token.getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private  void saveUser(JsonUserID jsonUserID){
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Uzyskano dostęp", Toast.LENGTH_SHORT).show();
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = getApplicationContext().openFileOutput("AccountInfo", Context.MODE_PRIVATE);
                    fileOutputStream.write(String.valueOf(jsonUserID.getStudentid()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getAlbum()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getImie()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getNazwisko()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getDataRejestracji()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.isActive()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.isStar()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getFinid()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getEmail()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getPhone()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.write(String.valueOf(jsonUserID.getComment()).getBytes());
                    fileOutputStream.write("/".getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void handDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!connect){
                    Intent mySuperIntent = new Intent(FullscreenActivity.this, LoginActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }else{
                    Intent mySuperIntent = new Intent(FullscreenActivity.this, MainActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }
        }, SPLASH_TIME);
    }
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(4000)
                .start();
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