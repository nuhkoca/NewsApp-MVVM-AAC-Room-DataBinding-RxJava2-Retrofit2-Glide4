package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.Sources;

import java.util.List;

@Dao
public interface SourcesDao {

    @Query("SELECT * FROM sources")
    List<Sources> getAll();

    @Insert
    void insertAll(Sources... sources);

    @Query("DELETE FROM sources")
    void deleteAll();

}