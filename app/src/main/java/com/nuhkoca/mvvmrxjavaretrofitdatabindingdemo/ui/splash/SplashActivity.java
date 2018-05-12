package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.badoo.mobile.util.WeakHandler;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main.NewsActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int delayToActivity = getResources().getInteger(R.integer.delay_in_seconds_to_close);

        WeakHandler weakHandler = new WeakHandler(Looper.getMainLooper());

        weakHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent newsIntent = new Intent(SplashActivity.this, NewsActivity.class);

                newsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                newsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(newsIntent);
            }
        }, delayToActivity);
    }
}