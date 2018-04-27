package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class EverythingModelFactory implements ViewModelProvider.Factory {

    private String query;

    EverythingModelFactory(@NonNull String query) {
        this.query = query;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(query);
    }
}
