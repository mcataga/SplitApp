package com.example.splitapp.dummy;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ActivityContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ActivityItem> ITEMS = new ArrayList<ActivityItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ActivityItem> ITEM_MAP = new HashMap<String, ActivityItem>();

    private static final int COUNT = 25;
    public static String TAG = "ActivityContent";

    public static void setContext(Context c) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        ITEMS = new ArrayList<ActivityItem>();
        ITEM_MAP = new HashMap<String, ActivityItem>();
        fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully retrieved activities");
                        int counter =0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getData().toString());
                            addItem(document.getId(), document.getData(), counter++);
                        }
                        Log.d(TAG, ITEMS.toString());
                    }
                }
            });
    }

    private static void addItem(String id, Map<String, Object> data, int pos) {
        ActivityItem item = new ActivityItem(id, (String)data.get("name"), makeDetails(pos));
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ActivityItem {
        public final String id;
        public final String name;
        public final String details;

        public ActivityItem(String id, String name, String details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}