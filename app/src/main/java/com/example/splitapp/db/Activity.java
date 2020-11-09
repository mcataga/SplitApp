package com.example.splitapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Activity {
    @PrimaryKey(autoGenerate = true)
    public int activityID;

    @ColumnInfo(name = "name")
    public String name;

}
