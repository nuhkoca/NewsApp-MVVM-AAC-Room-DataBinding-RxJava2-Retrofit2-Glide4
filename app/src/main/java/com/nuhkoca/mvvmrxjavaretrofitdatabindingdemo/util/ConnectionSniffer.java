package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;

public class ConnectionSniffer {
    public static boolean sniff() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NewsApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null &&
                networkInfo.isConnected() &&
                networkInfo.isConnectedOrConnecting() &&
                networkInfo.isAvailable();
    }
}