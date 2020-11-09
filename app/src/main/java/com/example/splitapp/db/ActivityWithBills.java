package com.example.splitapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ActivityWithBills {
    @Embedded
    public Activity activity;
    @Relation(
            parentColumn ="activityID",
            entityColumn ="activityID"
    )
    public List<Bill> bills;

    public ActivityWithBills(Activity activity, List<Bill> bills) {
        this.activity = activity;
        this.bills=bills;
    }
}
