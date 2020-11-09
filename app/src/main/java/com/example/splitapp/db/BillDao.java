package com.example.splitapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillDao {
    @Query("SELECT * FROM bill")
    List<Bill> getAllBills();

    @Insert
    void insertUser(Bill... bills);

    @Delete
    void delete(Bill bill);
}
