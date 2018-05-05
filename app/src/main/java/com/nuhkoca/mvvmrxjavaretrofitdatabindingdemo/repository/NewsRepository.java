package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.SourcesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;

import java.util.List;

import timber.log.Timber;

public class NewsRepository {

    private SourcesDao mSourceDao;
    private List<DbSources> mDbSources;

    public NewsRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mSourceDao = appDatabase.sourcesDao();
    }

    public void insertSources(DbSources dbSources) {
        new insertSources(mSourceDao).execute(dbSources);
    }

    public List<DbSources> getAllSources() {
        new getSources(mSourceDao).execute();

        return mDbSources;
    }

    public void deleteAllSources(){
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

    @SuppressLint("StaticFieldLeak")
    private class getSources extends AsyncTask<Void, Void, List<DbSources>> {

        private SourcesDao mAsyncTaskDao;

        getSources(SourcesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<DbSources> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAll();
        }

        @Override
        protected void onPostExecute(List<DbSources> dbSourcesList) {
            super.onPostExecute(dbSourcesList);
            mDbSources = dbSourcesList;

            for (int i = 0; i < dbSourcesList.size(); i++) {
                Timber.d(dbSourcesList.get(i).getName());
            }
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