package com.example.compumovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class web_frame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_frame);
        ((WebView) findViewById(R.id.webView)).loadUrl("http://www.javeriana.edu.co");
    }
}
