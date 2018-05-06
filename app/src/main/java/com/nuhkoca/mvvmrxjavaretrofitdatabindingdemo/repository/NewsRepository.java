package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.SourcesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;

import java.util.List;

public class NewsRepository {

    private SourcesDao mSourceDao;
    private LiveData<List<DbSources>> mDbSources;

    public NewsRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mSourceDao = appDatabase.sourcesDao();

        mDbSources = mSourceDao.getAll();
    }

    public void insertSources(DbSources dbSources) {
        new insertSources(mSourceDao).execute(dbSources);
    }

    public LiveData<List<DbSources>> getAllSources() {
        return mDbSources;
    }

    public void deleteAllSources() {
        new deleteSources(mSourceDao).execute();
    }

    private static class insertSources extends AsyncTask<DbSources, Void, Void> {

        private SourcesDao mAsyncTaskDao;

        insertSources(SourcesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbSources... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class deleteSources extends AsyncTask<DbSources, Void, Void> {

        private SourcesDao mAsyncTaskDao;

        deleteSources(SourcesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbSources... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}