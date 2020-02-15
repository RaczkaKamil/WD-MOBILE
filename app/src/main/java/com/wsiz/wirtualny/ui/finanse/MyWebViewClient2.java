package com.wsiz.wirtualny.ui.finanse;

import android.content.Context;
import android.webkit.WebView;

interface MyWebViewClient2 {
    boolean shouldOverrideUrlLoading(WebView view, String url, Context ctx);
}
