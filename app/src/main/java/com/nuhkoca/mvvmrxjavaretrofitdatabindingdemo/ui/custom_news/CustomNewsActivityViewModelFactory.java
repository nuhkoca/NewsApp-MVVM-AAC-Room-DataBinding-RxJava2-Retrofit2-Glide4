package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.custom_news;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

public class CustomNewsActivityViewModelFactory implements ViewModelProvider.Factory {

    private ObservableHelper observableHelper;
    private Application application;

    CustomNewsActivityViewModelFactory(Application application, ObservableHelper observableHelper) {
        this.application = application;
        this.observableHelper = observableHelper;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CustomNewsActivityViewModel(application, observableHelper);
    }
}
