package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NewsFragmentViewModel extends ViewModel {
    private MutableLiveData<ArticlesWrapper> mNewsWrapper = new MutableLiveData<>();
    private MutableLiveData<SourcesWrapper> mSourcesWrapper = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private ObservableHelper observableHelper;

    NewsFragmentViewModel(ObservableHelper observableHelper) {
        this.observableHelper = observableHelper;
        isLoading.setValue(true);
    }

    public void getTopHeadlines(@Nullable String countryCode, @Nullable String sources, @Nullable String category, @Nullable String query) {
        mNewsWrapper.setValue(null);

       Observable<ArticlesWrapper> getTopHeadlines = observableHelper.getTopHeadlines(countryCode,
                sources, category, query);

       getTopHeadlines.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                            if (TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                    || TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                    || articlesWrapper.getArticles().get(i).getDescription() == null
                                    || articlesWrapper.getArticles().get(i).getUrlToImage() == null) {
                                Timber.d("Value is null, skipping...");

                                articlesWrapper.getArticles().remove(i);

                                i++;
                            }
                        }

                        mNewsWrapper.setValue(articlesWrapper);
                        isLoading.setValue(false);
                    }
                });
    }

    public void getEverything(@NonNull String query) {
        mNewsWrapper.setValue(null);

        Observable<ArticlesWrapper> getEverything = observableHelper.getEverything(query);

        getEverything.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                            if (TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                    || TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                    || articlesWrapper.getArticles().get(i).getDescription() == null
                                    || articlesWrapper.getArticles().get(i).getUrlToImage() == null) {
                                Timber.d("Value is null, skipping...");

                                articlesWrapper.getArticles().remove(i);

                                i++;
                            }
                        }

                        mNewsWrapper.setValue(articlesWrapper);
                        isLoading.setValue(false);
                    }
                });
    }

    public void getSources(@Nullable String language, @Nullable String countryCode) {
        Observable<SourcesWrapper> getSources = observableHelper.getSources(language, countryCode);

        getSources.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SourcesWrapper>>() {
                    @Override
                    public Observable<? extends SourcesWrapper> call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Subscriber<SourcesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SourcesWrapper sourcesWrapper) {
                        for (int i = 0; i < sourcesWrapper.getSources().size(); i++) {
                            if (TextUtils.isEmpty(sourcesWrapper.getSources().get(i).getDescription())) {

                                sourcesWrapper.getSources().remove(i);

                                i++;
                            }
                        }

                        mSourcesWrapper.setValue(sourcesWrapper);
                        isLoading.setValue(false);
                    }
                });
    }

    public MutableLiveData<ArticlesWrapper> fetchTopHeadlines() {
        return mNewsWrapper;
    }

    public MutableLiveData<ArticlesWrapper> fetchEverything() {
        return mNewsWrapper;
    }

    public MutableLiveData<SourcesWrapper> fetchSources() {
        return mSourcesWrapper;
    }

    public class Optional<M> {

        private final M optional;

        Optional(@Nullable M optional) {
            this.optional = optional;
        }

        public boolean isEmpty() {
            return this.optional == null;
        }

        public M get() {
            if (optional == null) {
                throw new NoSuchElementException("No value present");
            }
            return optional;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}