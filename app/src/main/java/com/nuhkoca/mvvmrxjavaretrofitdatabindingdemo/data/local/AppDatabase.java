package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao.SourcesDao;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.Sources;

@Database(entities = {Sources.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SourcesDao sourcesDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "news-database")
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