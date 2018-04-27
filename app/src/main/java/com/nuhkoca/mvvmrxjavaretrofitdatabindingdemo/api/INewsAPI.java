package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface INewsAPI {

    enum Endpoints {
        TOP_HEADLINES(0),
        EVERYTHING(1),
        SOURCES(2);

        private int value;

        Endpoints(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @GET("/v2/top-headlines")
    Observable<ArticlesWrapper> fetchTopHeadlines(@Query("country") @Nullable String countryCode,
                                                  @Query("source") @Nullable String sources,
                                                  @Query("category") @Nullable String category,
                                                  @Query("q") @Nullable String query,
                                                  @Query("apiKey") @NonNull String apiKey);

    @GET("/v2/everything")
    Observable<ArticlesWrapper> fetchEverything(@Query("q") @Nullable String query,
                                                @Query("apiKey") @NonNull String apiKey);

    @GET("/v2/sources")
    Observable<SourcesWrapper> fetchSources(@Query("language") @Nullable  String language,
                                            @Query("country") @Nullable  String country,
                                            @Query("apiKey") @NonNull String apiKey);
}