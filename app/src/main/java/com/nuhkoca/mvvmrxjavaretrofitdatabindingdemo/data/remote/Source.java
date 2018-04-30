package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;

public class Source extends BaseObservable {
    @SerializedName("name")
    private String name;

    public Source() {
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}