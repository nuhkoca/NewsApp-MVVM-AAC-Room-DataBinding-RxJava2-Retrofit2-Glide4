package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.EverythingDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.SourcesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.TopHeadlinesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;

@Database(entities = {DbSources.class, DbTopHeadlines.class, DbEverything.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SourcesDao sourcesDao();
    public abstract TopHeadlinesDao topHeadlinesDao();
    public abstract EverythingDao everythingDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, Constants.APP_DATABASE_NAME)
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}