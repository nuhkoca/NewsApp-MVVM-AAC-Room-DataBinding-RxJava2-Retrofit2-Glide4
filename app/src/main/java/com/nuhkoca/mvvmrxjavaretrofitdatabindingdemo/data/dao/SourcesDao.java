package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;

import java.util.List;

@Dao
public interface SourcesDao {

    @Query("SELECT * FROM sources")
    LiveData<List<DbSources>> getAll();

    @Insert
    void insertAll(DbSources... sources);

    @Query("DELETE FROM sources")
    void deleteAll();

}