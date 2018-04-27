package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class EverythingModelFactory implements ViewModelProvider.Factory {

    private Application application;

    private String query;

    EverythingModelFactory(Application application, @NonNull String query) {
        this.application = application;
        this.query = query;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(application, query);
    }
}
