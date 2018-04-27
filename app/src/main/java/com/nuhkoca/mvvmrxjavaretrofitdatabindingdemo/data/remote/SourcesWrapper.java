package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;

import java.util.List;

public class SourcesWrapper extends BaseObservable {
    @SerializedName("status")
    private String status;
    @SerializedName("sources")
    private List<Sources> sources;

    public SourcesWrapper() {
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public List<Sources> getSources() {
        return sources;
    }

    public void setSources(List<Sources> sources) {
        this.sources = sources;
        notifyPropertyChanged(BR.sources);
    }
}
