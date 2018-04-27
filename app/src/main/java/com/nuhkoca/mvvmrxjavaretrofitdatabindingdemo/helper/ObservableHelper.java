package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BuildConfig;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.api.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.NewsWrapper;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

public class ObservableHelper {
    private static INewsAPI getNewsAPI() {
        return getRetrofit().create(INewsAPI.class);
    }

    private static Retrofit getRetrofit() {
        return NewsApp.provideRetrofit();
    }

    public static Observable<NewsWrapper> getTopHeadlines(String countryCode, String sources, String category, String query) {
        INewsAPI iNewsAPI = getNewsAPI();

        return iNewsAPI.fetchTopHeadlines(countryCode, sources, category, query, BuildConfig.API_KEY);
    }
}