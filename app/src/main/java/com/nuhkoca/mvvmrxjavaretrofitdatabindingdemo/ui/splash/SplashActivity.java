package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main.NewsActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newsIntent = new Intent(SplashActivity.this, NewsActivity.class);

                newsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                newsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(newsIntent);
            }
        }, 1500);
    }
}