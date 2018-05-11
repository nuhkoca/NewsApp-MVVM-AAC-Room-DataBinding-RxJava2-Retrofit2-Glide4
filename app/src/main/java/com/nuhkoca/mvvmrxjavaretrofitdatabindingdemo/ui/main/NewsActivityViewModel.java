package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.ConnectionSniffer;

public class NewsActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> isActiveConnection = new MutableLiveData<>();
    private MutableLiveData<Boolean> isActiveConnectionForPreference = new MutableLiveData<>();

    public NewsActivityViewModel() {
    }

    public void checkConnection() {
        isActiveConnection.setValue(ConnectionSniffer.sniff());
    }

    public void checkConnectionForPreference() {
        isActiveConnectionForPreference.setValue(ConnectionSniffer.sniff());
    }


    public MutableLiveData<Boolean> getConnectionStatus() {
        return isActiveConnection;
    }

    public MutableLiveData<Boolean> getConnectionStatusForPreference() {
        return isActiveConnectionForPreference;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
