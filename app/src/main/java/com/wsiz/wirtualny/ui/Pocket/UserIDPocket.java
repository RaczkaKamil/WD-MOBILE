package com.wsiz.wirtualny.ui.Pocket;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserIDPocket {
    private Context ctx;
    private String data;
    private String token;
    private int studentID;
    private int finID;




    private int getUserFile(){
        int userFileNumber = ctx.fileList().length+1;
        for (int i = 0; i < ctx.fileList().length; i++) {
            if(ctx.fileList()[i].contains("AccountInfo")){
                System.out.println("znaleziono Account");
                userFileNumber = i;
                return userFileNumber;
            }

        }
        return userFileNumber;
    }

    public void startRead(Context context){
        ctx = context;
        IdReader();
    }

    private void IdReader(){
        System.out.println("IDREADER");
        if(getUserFile()<ctx.fileList().length){
            try {
               ;
                System.out.println( "otwieram: "+ctx.fileList()[getUserFile()]);
                FileInputStream fileInputStream = null;
                fileInputStream = ctx.openFileInput(ctx.fileList()[getUserFile()]);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();


                while ((data = bufferedReader.readLine()) != null) {
                    stringBuffer.append(data + "\n");
                    String splited = stringBuffer.toString();
                    System.out.println("otrzymano:"+splited);
                    String splited2[] = splited.split("/");
                    collectInfo(splited2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void collectInfo(String[] data){
        this.studentID =Integer.valueOf(data[0]);
        int album = Integer.valueOf(data[1]);
        String imie = data[2];
        String nazwisko = data[3];
        String dataRejestracji = data[4];
        boolean active = Boolean.getBoolean(data[5]);
        boolean star= Boolean.getBoolean(data[6]);;
       this.finID = Integer.valueOf(data[7]);
        String email = data[8];
        long phone = Integer.parseInt(data[9]);
        String comment = data[10];

        //UserIDPocket userIDPocket = new UserIDPocket(studentid,album,imie,nazwisko,dataRejestracji,active,star,finid,email,phone,comment);
    }

    public int getStudentid(){
        System.out.println("student id: "+String.valueOf(studentID));
        return this.studentID;
    }

    public int getFinid(){
        System.out.println("finanse id: "+String.valueOf(finID));
        return this.finID;
    }

}
