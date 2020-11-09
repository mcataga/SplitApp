package com.example.splitapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Transaction @Insert
    void insertUser(User... users);

    @Delete
    void delete(User user);

    @Insert
    void insertActivities(List<Activity> activities);
}