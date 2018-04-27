package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SourcesViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    private String language;
    private String country;

    SourcesViewModelFactory(Application application, String language, String country) {
        this.application = application;
        this.language = language;
        this.country = country;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(application, language, country);
    }
}
