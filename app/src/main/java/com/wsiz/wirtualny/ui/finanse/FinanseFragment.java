package com.wsiz.wirtualny.ui.finanse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.ui.Pocket.TokenPocket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FinanseFragment extends Fragment {
WebView webView;
String token;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_finanse, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        webView = root.findViewById(R.id.webView);


        textView.setText("Finanse: ");
        TokenPocket tokenPocket = new TokenPocket();
        tokenPocket.startRead(getContext());
        token = tokenPocket.getToken();
        connectNews(token);

        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("https://dziekanat.wsi.edu.pl/get/wd-czesne/czesne?wdauth=" +token);
        WebViewClient we = new WebViewClient();
        we.onLoadResource(webView, "https://dziekanat.wsi.edu.pl/get/wd-czesne/czesne?wdauth=" +token);
    webView.setWebViewClient(we);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                System.out.println("DOWNLOAD:");
                System.out.println(s);
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(s3);
                System.out.println(String.valueOf(l));
            }
        });
        // connectNews(token);
        return root;
    }

    public void connectNews(String token){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-czesne/czesne?wdauth=" +token);
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
}

 class MyWebViewClient extends WebViewClient implements MyWebViewClient2 {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url, Context ctx) {
        if ("https://dziekanat.wsi.edu.pl/czesne".equals(Uri.parse(url).getHost())) {
            // This is my website, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(intent);
        return true;
    }
}
