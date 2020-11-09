package com.example.splitapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profile {

    @PrimaryKey(autoGenerate = true)
    public long profileID;
    public long activityID;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "phoneNumber")
    public long phoneNumber;
    @ColumnInfo(name = "email")
    public String emailAddress;
    @ColumnInfo(name = "address")
    public String address;


}