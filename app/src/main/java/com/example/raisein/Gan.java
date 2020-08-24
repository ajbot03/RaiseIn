package com.example.raisein;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Gan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gan);
        String url = "https://bdf4b0ef23b2.ngrok.io/";
        WebView web = (WebView) findViewById(R.id.web);
        web.loadUrl(url);
    }
}