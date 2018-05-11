package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper;

import android.support.annotation.Nullable;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BuildConfig;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;

import retrofit2.Retrofit;
import rx.Observable;

public class ObservableHelper {
    private INewsAPI iNewsAPI;
    private static ObservableHelper instance;

    private ObservableHelper() {
        iNewsAPI = getRetrofit().create(INewsAPI.class);
    }

    public static ObservableHelper getInstance(){
        if (instance == null){
            instance = new ObservableHelper();
        }

        return instance;
    }

    private static Retrofit getRetrofit() {
        return NewsApp.provideRetrofit();
    }

    public Observable<ArticlesWrapper> getTopHeadlines(@Nullable String countryCode, @Nullable String sources, @Nullable String category, @Nullable String query, int page) {
        return iNewsAPI.fetchTopHeadlines(countryCode, sources, category, query, page, BuildConfig.API_KEY);
    }

    public Observable<ArticlesWrapper> getEverything(String query, String sortBy, String language) {
        return iNewsAPI.fetchEverything(query, sortBy, language, BuildConfig.API_KEY);
    }

    public Observable<SourcesWrapper> getSources(@Nullable String language, @Nullable String country, @Nullable String category) {
        return iNewsAPI.fetchSources(language, country, category, BuildConfig.API_KEY);
    }
}