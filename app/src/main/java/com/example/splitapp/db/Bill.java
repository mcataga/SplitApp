package com.example.splitapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Bill {
    @PrimaryKey(autoGenerate = true)
    public long billID;

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name ="price")
    public double totalPrice;
    @ColumnInfo(name ="amount_paid")
    public double amountPaid;
    @ColumnInfo(name ="amount_owed")
    public double amountOwed;
    @ColumnInfo(name ="image")
    public String image;
}
