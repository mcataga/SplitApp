package com.example.splitapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM Activity")
    List<Activity> getAllActivities();

    @Transaction @Insert
    void insertActivity(Activity... activities);

    @Insert
    void insertProfiles(List<Profile> profiles);

    @Insert
    void insertBills(List<Bill> bills);

    @Delete
    void delete(Activity activity);
}
