package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badoo.mobile.util.WeakHandler;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main.NewsActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeakHandler activityHandler = new WeakHandler();

        int delayToActivity = getResources().getInteger(R.integer.delay_in_seconds_to_close);

        final Intent newsIntent = new Intent(SplashActivity.this, NewsActivity.class);

        newsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activityHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(newsIntent);
            }
        }, delayToActivity);
    }
}