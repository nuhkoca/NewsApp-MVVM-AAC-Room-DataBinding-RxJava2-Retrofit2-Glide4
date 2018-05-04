package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

public class NewsFragmentViewModelFactory implements ViewModelProvider.Factory {

    private ObservableHelper observableHelper;

    NewsFragmentViewModelFactory(ObservableHelper observableHelper) {
        this.observableHelper = observableHelper;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public NewsFragmentViewModel create(@NonNull Class modelClass) {
        return new NewsFragmentViewModel(observableHelper);
    }
}
