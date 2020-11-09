package com.example.splitapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Split {
    @PrimaryKey(autoGenerate = true)
    public long splitID;
    public long itemID;
    @ColumnInfo(name = "custom_amount")
    public double customAmount;
    @ColumnInfo(name = "remaining_cost")
    public double remainingCost;
    @ColumnInfo(name = "total_cost")
    public double totalCost;
    @ColumnInfo(name = "num_of_splitters")
    public int numSplitters;

}
