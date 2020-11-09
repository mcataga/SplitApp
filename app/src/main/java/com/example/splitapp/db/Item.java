package com.example.splitapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int itemID;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "cost")
    public double cost;

}
