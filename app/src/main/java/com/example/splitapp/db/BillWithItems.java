package com.example.splitapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BillWithItems {
    @Embedded
    public Bill bill;
    @Relation(
            parentColumn ="billID",
            entityColumn ="billID"
    )
    public List<Item> items;

    public BillWithItems(Bill bill, List<Item> items) {
        this.bill = bill;
        this.items=items;
    }
}
