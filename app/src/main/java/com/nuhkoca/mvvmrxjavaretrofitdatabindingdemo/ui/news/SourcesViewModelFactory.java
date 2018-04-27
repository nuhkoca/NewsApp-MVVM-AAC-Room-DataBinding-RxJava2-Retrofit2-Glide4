package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SourcesViewModelFactory implements ViewModelProvider.Factory {
    private String language;
    private String country;

    SourcesViewModelFactory(String language, String country) {
        this.language = language;
        this.country = country;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(language, country);
    }
}
