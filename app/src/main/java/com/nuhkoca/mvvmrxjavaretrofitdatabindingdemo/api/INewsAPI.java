package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.api;

import android.support.annotation.Nullable;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.NewsWrapper;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface INewsAPI {

    @GET("/v2/top-headlines")
    Observable<NewsWrapper> fetchTopHeadlines(@Query("country") @Nullable String countryCode,
                                                    @Query("source") @Nullable String sources,
                                                    @Query("category") @Nullable String category,
                                                    @Query("q") @Nullable String query,
                                                    @Query("apiKey") String apiKey);

    @GET("/v2/everything")
    Observable<NewsWrapper> fetchEverything(@Query("apiKey") String apiKey,
                                       @Query("q") String query);

    @GET("/v2/sources")
    Observable<NewsWrapper> fetchSources(@Query("apiKey") String apiKey,
                                    @Path("language") String language,
                                    @Path("country") String country);
}