package com.example.splitapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM Activity")
    List<Activity> getAllActivities();

    @Insert
    void insertActivity(Activity... activities);

    @Delete
    void delete(Activity activity);
}
