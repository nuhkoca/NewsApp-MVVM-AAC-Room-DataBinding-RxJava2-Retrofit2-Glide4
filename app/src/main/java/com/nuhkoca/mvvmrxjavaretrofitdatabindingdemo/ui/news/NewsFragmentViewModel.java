package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NewsFragmentViewModel extends ViewModel {
    private MutableLiveData<ArticlesWrapper> mTopHeadlines = new MutableLiveData<>();
    private MutableLiveData<ArticlesWrapper> mEverything = new MutableLiveData<>();
    private MutableLiveData<SourcesWrapper> mSources = new MutableLiveData<>();

    public MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsErrorShown = new MutableLiveData<>();

    private ObservableHelper observableHelper;

    NewsFragmentViewModel(ObservableHelper observableHelper) {
        this.observableHelper = observableHelper;
        mIsLoading.setValue(true);
    }

    public void getTopHeadlines(@Nullable String countryCode,
                                @Nullable String sources,
                                @Nullable String category,
                                @Nullable String query) {
        Observable<ArticlesWrapper> getTopHeadlines = observableHelper.getTopHeadlines(countryCode,
                sources, category, query);

        getTopHeadlines.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIsErrorShown.postValue(true);
                        mIsLoading.setValue(false);
                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper.getArticles().size() == 0) {
                            mIsErrorShown.postValue(true);
                            mIsLoading.setValue(false);
                        } else {
                            List<Articles> articlesList = new ArrayList<>();
                            ArticlesWrapper wrapper = new ArticlesWrapper();

                            for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                                if (TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                        || TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                        || articlesWrapper.getArticles().get(i).getDescription() == null
                                        || articlesWrapper.getArticles().get(i).getUrlToImage() == null) {
                                    Timber.d("Top headlines value is null, skipping...");
                                } else {
                                    articlesList.add(articlesWrapper.getArticles().get(i));
                                }
                            }

                            wrapper.setArticles(articlesList);

                            mTopHeadlines.postValue(wrapper);
                            mIsErrorShown.setValue(false);
                            mIsLoading.setValue(false);
                        }
                    }
                });
    }

    public void getEverything(@NonNull String query) {
        Observable<ArticlesWrapper> getEverything = observableHelper.getEverything(query);

        getEverything.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ArticlesWrapper>>() {
                    @Override
                    public Observable<? extends ArticlesWrapper> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIsErrorShown.postValue(true);
                        mIsLoading.setValue(false);
                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper.getArticles().size() == 0) {
                            mIsErrorShown.postValue(true);
                            mIsLoading.setValue(false);
                        } else {
                            List<Articles> articlesList = new ArrayList<>();
                            ArticlesWrapper wrapper = new ArticlesWrapper();

                            for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                                if (TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                        || TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                        || articlesWrapper.getArticles().get(i).getDescription() == null
                                        || articlesWrapper.getArticles().get(i).getUrlToImage() == null) {
                                    Timber.d("Everything value is null, skipping...");
                                } else {
                                    articlesList.add(articlesWrapper.getArticles().get(i));
                                }
                            }

                            wrapper.setArticles(articlesList);

                            mEverything.setValue(wrapper);
                            mIsErrorShown.postValue(false);
                            mIsLoading.setValue(false);
                        }
                    }
                });
    }

    public void getSources(@Nullable String language,
                           @Nullable String countryCode) {
        Observable<SourcesWrapper> getSources = observableHelper.getSources(language, countryCode);

        getSources.subscribeOn(Schedulers.io())
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SourcesWrapper>>() {
                    @Override
                    public Observable<? extends SourcesWrapper> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SourcesWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIsErrorShown.postValue(true);
                        mIsLoading.setValue(false);
                    }

                    @Override
                    public void onNext(SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper.getSources().size() == 0) {
                            mIsErrorShown.postValue(true);
                            mIsLoading.setValue(false);
                        } else {

                            List<Sources> sourcesList = new ArrayList<>();
                            SourcesWrapper wrapper = new SourcesWrapper();

                            for (int i = 0; i < sourcesWrapper.getSources().size(); i++) {
                                if (TextUtils.isEmpty(sourcesWrapper.getSources().get(i).getDescription())) {
                                    Timber.d("Sources values is null, skipping...");
                                } else {
                                    sourcesList.add(sourcesWrapper.getSources().get(i));
                                }
                            }

                            wrapper.setSources(sourcesList);

                            mSources.setValue(wrapper);
                            mIsErrorShown.postValue(false);
                            mIsLoading.setValue(false);
                        }
                    }
                });
    }

    public MutableLiveData<ArticlesWrapper> fetchTopHeadlines() {
        return mTopHeadlines;
    }

    public MutableLiveData<ArticlesWrapper> fetchEverything() {
        return mEverything;
    }

    public MutableLiveData<SourcesWrapper> fetchSources() {
        return mSources;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}