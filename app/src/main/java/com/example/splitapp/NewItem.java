package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class NewItem extends AppCompatActivity {
    private static final String TAG = "NewItem";
    EditText itemName;
    EditText itemPrice;
    private String billId;
    private String activityId;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private CollectionReference profiles;
    private ProfileViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Intent intent = getIntent();
        billId = intent.getStringExtra("billId");
        activityId = intent.getStringExtra("activityId");
        itemName = (EditText)findViewById(R.id.editItemName);
        itemPrice = (EditText)findViewById(R.id.editItemPrice);
        profiles = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("profiles");
        Log.d(TAG, "THIS IS THE PATH" + profiles.getPath());
        collectionReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId).collection("items");
        findViewById(R.id.saveItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }

        });
        setUpRecyclerView();
        adapter.setOnProfileClickListener(new ProfileViewAdapter.OnProfileClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                FloatingActionButton indicator = (FloatingActionButton)findViewById(R.id.checkMark);
                DocumentReference docRef = documentSnapshot.getReference();
                ProfileItem item = documentSnapshot.toObject(ProfileItem.class);
                Map<String, Object> updateVisibility = new HashMap<>();
                if (indicator.getVisibility() == View.VISIBLE) {
                    updateVisibility.put("isVisible", false);
                    docRef.set(updateVisibility, SetOptions.merge());
                    Log.d(TAG, "WHAT ISI T? " + item.getIsVisible());
                    Log.d(TAG, "IT IS NOW GONE");
                }
                else {
                    if (indicator.getVisibility() == View.GONE) {
                        updateVisibility.put("isVisible", false);
                        docRef.set(updateVisibility, SetOptions.merge());
                        Log.d(TAG, "WHAT ISI T? " + item.getIsVisible());
                        Log.d(TAG, "IT IS NOW GONE");
                    }
                }
                Log.d(TAG, "Item clicked");
                DocumentReference doc = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("profiles").document(documentSnapshot.getId());
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = profiles;
        FirestoreRecyclerOptions<ProfileItem> items = new FirestoreRecyclerOptions.Builder<ProfileItem>()
                .setQuery(query, ProfileItem.class)
                .build();

        adapter = new ProfileViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.recyclerProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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
    private void saveButton() {
        HashMap<String, Object> item = new HashMap<>();
        String name = itemName.getText().toString().trim();
        String temp = itemPrice.getText().toString().trim();
        item.put("name", name);
        item.put("price", Double.parseDouble(temp));
        collectionReference.add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    item.put("id", task.getResult().getId());
                    task.getResult().set(item);
                    finish();
                    Toast.makeText(NewItem.this, "Successfully created New Item", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "Creating new bill failed");
                }
            }
        });
    }
}