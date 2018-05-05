package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

public class NewsFragmentViewModelFactory implements ViewModelProvider.Factory {

    private ObservableHelper observableHelper;
    private Application application;

    NewsFragmentViewModelFactory(Application application, ObservableHelper observableHelper) {
        this.application = application;
        this.observableHelper = observableHelper;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsFragmentViewModel(application, observableHelper);
    }
}