package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.EverythingDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;

import java.util.List;

public class EverythingRepository {

    private EverythingDao mEverythingDao;
    private LiveData<List<DbEverything>> mDbEverything;

    public EverythingRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        mEverythingDao = appDatabase.everythingDao();

        mDbEverything = mEverythingDao.getAll();
    }

    public void insertEverything(DbEverything dbEverything) {
        new insertEverything(mEverythingDao).execute(dbEverything);
    }

    public LiveData<List<DbEverything>> getAllEverything() {
        return mDbEverything;
    }

    public void deleteAllEverything() {
        new deleteEverything(mEverythingDao).execute();
    }

    private static class insertEverything extends AsyncTask<DbEverything, Void, Void> {

        private EverythingDao mAsyncTaskDao;

        insertEverything(EverythingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbEverything... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class deleteEverything extends AsyncTask<DbEverything, Void, Void> {

        private EverythingDao mAsyncTaskDao;

        deleteEverything(EverythingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DbEverything... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}