package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface INewsAPI {

    enum Endpoints {
        TOP_HEADLINES(Constants.TOP_NEWS_ID),
        EVERYTHING(Constants.EVERYTHING_ID),
        SOURCES(Constants.SOURCES_ID);

        private int value;

        Endpoints(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @GET("/v2/top-headlines")
    Observable<ArticlesWrapper> fetchTopHeadlines(@Query("country") @Nullable String country,
                                                  @Query("sources") @Nullable String sources,
                                                  @Query("category") @Nullable String category,
                                                  @Query("q") @Nullable String query,
                                                  @Query("page") int page,
                                                  @Query("apiKey") @NonNull String apiKey);

    @GET("/v2/everything")
    Observable<ArticlesWrapper> fetchEverything(@Query("q") @NonNull String query,
                                                @Query("sortBy") @Nullable String sortBy,
                                                @Query("language") @Nullable String language,
                                                @Query("apiKey") @NonNull String apiKey);

    @GET("/v2/sources")
    Observable<SourcesWrapper> fetchSources(@Query("language") @Nullable  String language,
                                            @Query("country") @Nullable  String country,
                                            @Query("category") @Nullable  String category,
                                            @Query("apiKey") @NonNull String apiKey);
}