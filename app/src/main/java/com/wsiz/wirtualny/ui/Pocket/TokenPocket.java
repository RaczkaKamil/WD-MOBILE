package com.wsiz.wirtualny.ui.Pocket;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TokenPocket {
    private Context ctx;
    private String data;
    private String token;

    public void startRead(Context context){
        ctx = context;
        TokenReader();
    }

    public String getToken(){
        return token;
    }

    private void TokenReader(){
        if(getTokenFile()<ctx.fileList().length){
            try {
                FileInputStream fileInputStream = null;
                fileInputStream = ctx.openFileInput(ctx.fileList()[getTokenFile()]);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();


                while ((data = bufferedReader.readLine()) != null) {
                    stringBuffer.append(data + "\n");
                    String splited = stringBuffer.toString();
                    this.token = splited;
                }



            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
    private int getTokenFile(){
        int tokenFileNumber = ctx.fileList().length+1;
        for (int i = 0; i < ctx.fileList().length; i++) {
            if(ctx.fileList()[i].contains("Token")){
                tokenFileNumber = i;
                return tokenFileNumber;
            }
        }
        return tokenFileNumber;
    }
}
