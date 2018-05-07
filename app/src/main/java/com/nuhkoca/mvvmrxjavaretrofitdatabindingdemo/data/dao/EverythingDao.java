package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;

import java.util.List;

@Dao
public interface EverythingDao {

    @Query("SELECT * FROM everything")
    LiveData<List<DbEverything>> getAll();

    @Insert
    void insertAll(DbEverything... everythings);

    @Query("DELETE FROM everything")
    void deleteAll();
}