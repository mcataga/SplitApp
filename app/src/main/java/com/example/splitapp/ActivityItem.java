package com.example.splitapp;

import com.example.splitapp.db.Activity;

import java.io.Serializable;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class ActivityItem {
    public static String TAG = "ActivityContent";
        private String mId;
        private String mName;

        public ActivityItem() {}
        public ActivityItem(String id, String name) {
            this.mId = id;
            this.mName = name;
        }

        public String getName() {
            return mName;
    }
        public void setName(String name) { this.mName = name; }
        public String getId() { return mId; }
        public void setId(String id) { this.mId = id; }
}