package com.example.splitapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ActivityWithProfiles {
    @Embedded
    public Activity activity;
    @Relation(
            parentColumn ="activityID",
            entityColumn ="activityID"
    )
    public List<Profile> profiles;

    public ActivityWithProfiles(Activity activity, List<Profile> profiles) {
        this.activity = activity;
        this.profiles=profiles;
    }
}
