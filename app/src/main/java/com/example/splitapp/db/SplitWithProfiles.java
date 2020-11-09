package com.example.splitapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SplitWithProfiles {
    @Embedded
    public Split split;
    @Relation(
            parentColumn ="splitID",
            entityColumn ="splitID"
    )
    public List<Profile> profiles;

    public SplitWithProfiles(Split split, List<Profile> profiles) {
        this.split = split;
        this.profiles=profiles;
    }
}
