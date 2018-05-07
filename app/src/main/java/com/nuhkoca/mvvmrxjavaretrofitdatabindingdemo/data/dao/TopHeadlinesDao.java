package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;

import java.util.List;

@Dao
public interface TopHeadlinesDao {

    @Query("SELECT * FROM top_headlines")
    LiveData<List<DbTopHeadlines>> getAll();

    @Insert
    void insertAll(DbTopHeadlines... topHeadlines);

    @Query("DELETE FROM top_headlines")
    void deleteAll();
}