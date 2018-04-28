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
    private static INewsAPI getNewsAPI() {
        return getRetrofit().create(INewsAPI.class);
    }

    private static Retrofit getRetrofit() {
        return NewsApp.provideRetrofit();
    }

    public static Observable<ArticlesWrapper> getTopHeadlines(String countryCode, String sources, String category, String query) {
        INewsAPI iNewsAPI = getNewsAPI();

        return iNewsAPI.fetchTopHeadlines(countryCode, sources, category, query, BuildConfig.API_KEY);
    }

    public static Observable<ArticlesWrapper> getEverything(String query) {
        INewsAPI iNewsAPI = getNewsAPI();

        return iNewsAPI.fetchEverything(query, BuildConfig.API_KEY);
    }

    public static Observable<SourcesWrapper> getSources(@Nullable String language, @Nullable String country) {
        INewsAPI iNewsAPI = getNewsAPI();

        return iNewsAPI.fetchSources(language, country, BuildConfig.API_KEY);
    }
}