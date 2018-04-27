package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.NewsWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NewsFragmentViewModel extends ViewModel {
    public MutableLiveData<NewsWrapper> mNewsWrapper = new MutableLiveData<>();

    private String countryCode;
    private String sources;
    private String category;
    private String query;
    private String language;

    NewsFragmentViewModel(String countryCode, String sources, String category, String query) {
        this.countryCode = countryCode;
        this.sources = sources;
        this.category = category;
        this.query = query;
    }

    NewsFragmentViewModel(String query){
        this.query = query;
    }

    public NewsFragmentViewModel(String countryCode, String language) {
        this.countryCode = countryCode;
        this.language = language;
    }

    public void fetchTopHeadlines() {
        Observable<NewsWrapper> getTopHeadlines = ObservableHelper.getTopHeadlines(countryCode,
                sources, category, query);

        getTopHeadlines.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends NewsWrapper>>() {
                    @Override
                    public Observable<? extends NewsWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<NewsWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsWrapper newsWrapper) {
                        mNewsWrapper.postValue(newsWrapper);
                    }
                });
    }

    public void fetchEverything() {
        Observable<NewsWrapper> getEverything = ObservableHelper.getEverything(query);

        getEverything.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends NewsWrapper>>() {
                    @Override
                    public Observable<? extends NewsWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<NewsWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsWrapper newsWrapper) {
                        mNewsWrapper.postValue(newsWrapper);
                    }
                });
    }

    public void fetchSources() {
        Observable<NewsWrapper> getSources = ObservableHelper.getSources(language, countryCode);

        getSources.subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends NewsWrapper>>() {
                    @Override
                    public Observable<? extends NewsWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<NewsWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsWrapper newsWrapper) {
                        mNewsWrapper.postValue(newsWrapper);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}