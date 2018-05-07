package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.TopHeadlinesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;

import java.util.List;

public class TopHeadlinesRepository {
    private TopHeadlinesDao mTopHeadlinesDao;
    private LiveData<List<DbTopHeadlines>> mDbTopHeadlines;

    public TopHeadlinesRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mTopHeadlinesDao = appDatabase.topHeadlinesDao();

        mDbTopHeadlines = mTopHeadlinesDao.getAll();
    }

    public void insertTopHeadlines(DbTopHeadlines dbTopHeadlines) {
        new insertTopHeadlines(mTopHeadlinesDao).execute(dbTopHeadlines);
    }

    public LiveData<List<DbTopHeadlines>> getAllTopHeadlines() {
        return mDbTopHeadlines;
    }

    public void deleteAllTopHeadlines() {
        new deleteTopHeadlines(mTopHeadlinesDao).execute();
    }

    private static class insertTopHeadlines extends AsyncTask<DbTopHeadlines, Void, Void> {

        private TopHeadlinesDao mAsyncTaskDao;

        insertTopHeadlines(TopHeadlinesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbTopHeadlines... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class deleteTopHeadlines extends AsyncTask<DbTopHeadlines, Void, Void> {

        private TopHeadlinesDao mAsyncTaskDao;

        deleteTopHeadlines(TopHeadlinesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbTopHeadlines... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}