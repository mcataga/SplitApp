package com.example.splitapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithActivities {
    @Embedded public User user;
    @Relation(
        parentColumn ="userID",
        entityColumn ="userID"
    )
    public List<Activity> activities;

    public UserWithActivities(User user, List<Activity> activities) {
        this.user = user;
        this.activities=activities;
    }
}
