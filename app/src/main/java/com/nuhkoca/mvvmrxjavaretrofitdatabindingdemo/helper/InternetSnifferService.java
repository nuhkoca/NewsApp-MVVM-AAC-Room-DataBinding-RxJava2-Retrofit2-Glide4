package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;

public class InternetSnifferService extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if (isConnected) {
            Toast.makeText(context, context.getString(R.string.urgent_internet_warning), Toast.LENGTH_SHORT).show();
        }
    }
}