package com.cleanPhone.mobileCleaner.games;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;

public class GamesActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private WebView webView;

    @Override
    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
            return;
        }
        this.webView.stopLoading();
        super.onBackPressed();
    }

    @Override
    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_games);
        this.webView = (WebView) findViewById(R.id.webView);
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.progressDialog = progressDialog;
        progressDialog.setTitle("Loading...");
        this.progressDialog.setProgressStyle(0);
        this.webView.setWebViewClient(new WebViewClient());
        this.webView.setWebChromeClient(new WebChromeClient());
        this.webView.clearHistory();
        this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.mobiclean.phoneclean.games.GamesActivity.1
            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                if (GamesActivity.this.isFinishing()) {
                    return;
                }
                if (i == 100) {
                    GamesActivity.this.progressDialog.dismiss();
                } else {
                    GamesActivity.this.progressDialog.show();
                }
            }
        });
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (new SharedPrefUtil(getBaseContext()).getInt(SharedPrefUtil.GAME_TYPE) == 0) {
            this.webView.loadUrl("https://www.atmegame.com/?utm_source=AdcountyMk91u&utm_medium=AdcountyMk91u");
        } else {
            this.webView.loadUrl("https://251.set.predchamp.com/");
        }

    }
}
