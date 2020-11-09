package com.example.splitapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SplitDao {
    @Query("SELECT * FROM split")
    List<Split> getAllSplits();

    @Insert
    void insertSplit(Split... splits);

    @Delete
    void delete(Split split);
}
