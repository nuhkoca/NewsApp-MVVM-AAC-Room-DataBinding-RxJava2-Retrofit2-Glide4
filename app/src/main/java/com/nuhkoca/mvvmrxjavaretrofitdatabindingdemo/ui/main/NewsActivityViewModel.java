package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

public class NewsActivityViewModel extends ViewModel {

    public MutableLiveData<Fragment> mNewsFragment = new MutableLiveData<>();

    public void callFragment() {

    }
}