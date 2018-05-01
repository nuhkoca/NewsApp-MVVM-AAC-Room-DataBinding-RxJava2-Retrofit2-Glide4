package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news.NewsFragment;

public class InternetSnifferService extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        NewsFragment newsFragment = new NewsFragment();

        if (isConnected) {
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

            //NewsFragment.getInstance(INewsAPI.Endpoints.TOP_HEADLINES).placeNews();
        }
    }
}