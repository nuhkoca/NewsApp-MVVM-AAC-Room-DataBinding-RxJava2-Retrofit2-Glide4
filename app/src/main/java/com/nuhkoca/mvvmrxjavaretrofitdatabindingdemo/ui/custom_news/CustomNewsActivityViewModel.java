package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.custom_news;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CustomNewsActivityViewModel extends AndroidViewModel {

    private MutableLiveData<ArticlesWrapper> mTopHeadlines = new MutableLiveData<>();
    public MutableLiveData<Boolean> mTopHeadlinesLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> mTopHeadlinesError = new MutableLiveData<>();

    private ObservableHelper mObservableHelper;

    public CustomNewsActivityViewModel(@NonNull Application application, ObservableHelper observableHelper) {
        super(application);

        this.mObservableHelper = observableHelper;

        mTopHeadlinesLoading.setValue(true);
        mTopHeadlinesError.setValue(false);
    }

    public void getTopHeadlines(@Nullable String sources) {
        Observable<ArticlesWrapper> getTopHeadlines = mObservableHelper.getTopHeadlines(null,
                sources, null, null);

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
                        mTopHeadlinesLoading.setValue(false);
                        mTopHeadlinesError.setValue(true);
                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper.getArticles().size() == 0) {
                            mTopHeadlinesLoading.setValue(false);
                            mTopHeadlinesError.setValue(true);
                        } else {
                            List<Articles> articlesList = new ArrayList<>();
                            ArticlesWrapper wrapper = new ArticlesWrapper();

                            for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                                if (!TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                        || !TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                        || articlesWrapper.getArticles().get(i).getDescription() != null
                                        || articlesWrapper.getArticles().get(i).getUrlToImage() != null) {
                                    articlesList.add(articlesWrapper.getArticles().get(i));
                                }
                            }

                            wrapper.setArticles(articlesList);
                            mTopHeadlines.setValue(wrapper);

                            mTopHeadlinesLoading.setValue(false);
                            mTopHeadlinesError.setValue(false);
                        }
                    }
                });
    }

    public MutableLiveData<ArticlesWrapper> fetchTopHeadlines() {
        return mTopHeadlines;
    }
}