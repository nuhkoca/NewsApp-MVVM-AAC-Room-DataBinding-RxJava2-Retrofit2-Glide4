package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository.EverythingRepository;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository.SourcesRepository;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository.TopHeadlinesRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class NewsFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<ArticlesWrapper> mTopHeadlines = new MutableLiveData<>();
    private MutableLiveData<ArticlesWrapper> mEverything = new MutableLiveData<>();
    private MutableLiveData<SourcesWrapper> mSources = new MutableLiveData<>();

    public MutableLiveData<Boolean> mTopHeadlinesLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> mEverythingLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> mSourcesLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> mTopHeadlinesError = new MutableLiveData<>();
    public MutableLiveData<Boolean> mEverythingError = new MutableLiveData<>();
    public MutableLiveData<Boolean> mSourcesError = new MutableLiveData<>();

    private ObservableHelper observableHelper;

    private SourcesRepository mSourcesRepository;
    private TopHeadlinesRepository mTopHeadlinesRepository;
    private EverythingRepository mEverythingRepository;

    private LiveData<List<DbSources>> mDbSourcesList;
    private LiveData<List<DbTopHeadlines>> mDbTopHeadlines;
    private LiveData<List<DbEverything>> mDbEverything;

    NewsFragmentViewModel(Application application, ObservableHelper observableHelper) {
        super(application);
        this.observableHelper = observableHelper;

        mSourcesRepository = new SourcesRepository(application);
        mTopHeadlinesRepository = new TopHeadlinesRepository(application);
        mEverythingRepository = new EverythingRepository(application);

        mDbSourcesList = mSourcesRepository.getAllSources();
        mDbTopHeadlines = mTopHeadlinesRepository.getAllTopHeadlines();
        mDbEverything = mEverythingRepository.getAllEverything();

        mTopHeadlinesLoading.setValue(true);
        mEverythingLoading.setValue(true);
        mSourcesLoading.setValue(true);

        mTopHeadlinesError.setValue(false);
        mEverythingError.setValue(false);
        mSourcesError.setValue(false);
    }

    public void getTopHeadlines(@Nullable String countryCode,
                                @Nullable String sources,
                                @Nullable String category,
                                @Nullable String query,
                                int page) {
        Observable<ArticlesWrapper> getTopHeadlines = observableHelper.getTopHeadlines(countryCode,
                sources, category, query, page);

        getTopHeadlines.subscribeOn(Schedulers.io())
                .retry(1)
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
                            DbTopHeadlines dbTopHeadlines;

                            mTopHeadlinesRepository.deleteAllTopHeadlines();

                            for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                                if (!TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                        || !TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                        || articlesWrapper.getArticles().get(i).getDescription() != null
                                        || articlesWrapper.getArticles().get(i).getUrlToImage() != null) {
                                    articlesList.add(articlesWrapper.getArticles().get(i));

                                    dbTopHeadlines = new DbTopHeadlines(articlesWrapper.getArticles().get(i).getAuthor(),
                                            articlesWrapper.getArticles().get(i).getTitle(),
                                            articlesWrapper.getArticles().get(i).getDescription(),
                                            articlesWrapper.getArticles().get(i).getUrl(),
                                            articlesWrapper.getArticles().get(i).getUrlToImage(),
                                            articlesWrapper.getArticles().get(i).getPublishedAt(),
                                            articlesWrapper.getArticles().get(i).getSource().getName());

                                    mTopHeadlinesRepository.insertTopHeadlines(dbTopHeadlines);
                                    Timber.d("TopHeadlines successfully added to database");
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

    public void getEverything(@NonNull String query, @Nullable String sortBy, @Nullable String language) {
        Observable<ArticlesWrapper> getEverything = observableHelper.getEverything(query, sortBy, language);

        getEverything.subscribeOn(Schedulers.io())
                .retry(1)
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
                        mEverythingLoading.setValue(false);
                        mEverythingError.setValue(true);
                    }

                    @Override
                    public void onNext(ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper.getArticles().size() == 0) {
                            mEverythingLoading.setValue(false);
                            mEverythingError.setValue(true);
                        } else {
                            List<Articles> articlesList = new ArrayList<>();
                            ArticlesWrapper wrapper = new ArticlesWrapper();
                            DbEverything dbEverything;

                            mEverythingRepository.deleteAllEverything();

                            for (int i = 0; i < articlesWrapper.getArticles().size(); i++) {
                                if (!TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getDescription())
                                        || !TextUtils.isEmpty(articlesWrapper.getArticles().get(i).getUrlToImage())
                                        || articlesWrapper.getArticles().get(i).getDescription() != null
                                        || articlesWrapper.getArticles().get(i).getUrlToImage() != null) {
                                    articlesList.add(articlesWrapper.getArticles().get(i));

                                    dbEverything = new DbEverything(articlesWrapper.getArticles().get(i).getAuthor(),
                                            articlesWrapper.getArticles().get(i).getTitle(),
                                            articlesWrapper.getArticles().get(i).getDescription(),
                                            articlesWrapper.getArticles().get(i).getUrl(),
                                            articlesWrapper.getArticles().get(i).getUrlToImage(),
                                            articlesWrapper.getArticles().get(i).getPublishedAt(),
                                            articlesWrapper.getArticles().get(i).getSource().getName());

                                    mEverythingRepository.insertEverything(dbEverything);
                                    Timber.d("Everything successfully added to database");
                                }
                            }

                            wrapper.setArticles(articlesList);
                            mEverything.setValue(wrapper);

                            mEverythingLoading.setValue(false);
                            mEverythingError.setValue(false);
                        }
                    }
                });
    }

    public void getSources(@Nullable String language,
                           @Nullable String countryCode,
                           @Nullable String category) {
        Observable<SourcesWrapper> getSources = observableHelper.getSources(language, countryCode, category);

        getSources.subscribeOn(Schedulers.io())
                .retry(1)
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
                        mSourcesLoading.setValue(false);
                        mSourcesError.setValue(true);
                    }

                    @Override
                    public void onNext(SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper.getSources().size() == 0) {
                            mSourcesLoading.setValue(false);
                            mSourcesError.setValue(true);
                        } else {
                            List<Sources> sourcesList = new ArrayList<>();
                            SourcesWrapper wrapper = new SourcesWrapper();
                            DbSources dbSources;

                            mSourcesRepository.deleteAllSources();

                            for (int i = 0; i < sourcesWrapper.getSources().size(); i++) {
                                if (!TextUtils.isEmpty(sourcesWrapper.getSources().get(i).getDescription())
                                        || sourcesWrapper.getSources().get(i).getDescription() != null) {
                                    sourcesList.add(sourcesWrapper.getSources().get(i));

                                    dbSources = new DbSources(sourcesWrapper.getSources().get(i).getId(),
                                            sourcesWrapper.getSources().get(i).getName(),
                                            sourcesWrapper.getSources().get(i).getDescription(),
                                            sourcesWrapper.getSources().get(i).getUrl(),
                                            sourcesWrapper.getSources().get(i).getCategory(),
                                            sourcesWrapper.getSources().get(i).getLanguage(),
                                            sourcesWrapper.getSources().get(i).getCountry());

                                    mSourcesRepository.insertSources(dbSources);
                                    Timber.d("Sources successfully added to database");
                                }
                            }

                            wrapper.setSources(sourcesList);
                            mSources.setValue(wrapper);

                            mSourcesLoading.setValue(false);
                            mSourcesError.setValue(false);
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

    public LiveData<List<DbSources>> getAllSources() {
        return mDbSourcesList;
    }

    public LiveData<List<DbTopHeadlines>> getAllTopHeadlines() {
        return mDbTopHeadlines;
    }

    public LiveData<List<DbEverything>> getAllEverything() {
        return mDbEverything;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        AppDatabase.destroyInstance();
    }
}