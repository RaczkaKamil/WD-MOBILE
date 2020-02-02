package com.wsiz.wirtualny;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SelectedActivity extends AppCompatActivity {

    TextView tf_tytul;
    TextView tf_data;
    EditText tf_tresc;
    Button btn_download;
    String chosed[]=new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wiadomość");

        toolbar.showOverflowMenu();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Wiadomość:");



        tf_tytul=findViewById(R.id.tf_tytul);
        tf_data=findViewById(R.id.tf_data);
        tf_tresc=findViewById(R.id.tf_tresc);
        btn_download=findViewById(R.id.btn_download);
        btn_download.setVisibility(View.GONE);


        try{
            this.chosed = getIntent().getStringArrayExtra("select");
        }catch (NumberFormatException e)
        {
            System.out.println("bład otwierania");
        }

        tf_tytul.setText(chosed[0]);
        tf_data.setText(chosed[1]);
        tf_tresc.setText(chosed[2]);
        tf_tresc.setKeyListener(null);

        for (int i = 3; i < chosed.length; i++) {
            System.out.println(i+". "+ chosed[i]);
        }


        try{
            if(!chosed[4].contains("null")){
                btn_download.setText(chosed[4]);
                btn_download.setVisibility(View.VISIBLE);
                btn_download.setOnClickListener(view -> {
                    startDownload(chosed[4],chosed[5]);
                });
            }

        }catch (NullPointerException e){
            e.fillInStackTrace();
        }




    }

    private void startDownload(String fileName, String fileUUID){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://dziekanat.wsi.edu.pl/news/file/"+fileUUID+"/"+fileName+"/"));
                    startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectedActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
