package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TopHeadlinesModelFactory implements ViewModelProvider.Factory {

    private Application application;

    private String countryCode;
    private String sources;
    private String category;
    private String query;

    TopHeadlinesModelFactory(Application application, @Nullable String countryCode, @Nullable String sources, @Nullable String category, @Nullable String query) {
        this.application = application;
        this.countryCode = countryCode;
        this.sources = sources;
        this.category = category;
        this.query = query;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(application, countryCode, sources, category, query);
    }
}