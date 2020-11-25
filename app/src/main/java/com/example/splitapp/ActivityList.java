package com.example.splitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ActivityList extends AppCompatActivity {
    private static final String TAG = "ActivityList";
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference activities= fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities");

    private ActivityViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setUpRecyclerView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addActivityItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new ActivityViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(ActivityList.this, ActivityDetails.class);
                intent.putExtra("docId", documentSnapshot.getId());
                intent.putExtra("name", documentSnapshot.getString("name"));
                Log.d(TAG, "GOING TO INTENT YEEHAW");
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = activities;
        FirestoreRecyclerOptions<ActivityItem> items = new FirestoreRecyclerOptions.Builder<ActivityItem>()
                .setQuery(query, ActivityItem.class)
                .build();

        adapter = new ActivityViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.recyclerActivityItem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}