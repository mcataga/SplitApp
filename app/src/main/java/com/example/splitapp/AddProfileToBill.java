package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class AddProfileToBill extends AppCompatActivity {
    private static final String TAG = "AddProfileToBill";
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private String billId;
    private String activityId;
    private CollectionReference profiles;
    private DocumentReference bills;
    private CollectionReference items;
    private ProfileViewAdapter adapter;
    int totalItems = 0;
    double totalPrice = 0.0;
    int profileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_to_bill);
        Intent intent = getIntent();
        billId = intent.getStringExtra("billId");
        activityId = intent.getStringExtra("activityId");
        profiles = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("profiles");
        bills = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId);
        items = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId).collection("items");
        items.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 totalItems = queryDocumentSnapshots.size();
             }
         });
        bills.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalPrice = documentSnapshot.getDouble("totalPrice");
            }
        });
        profiles.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    profileSize = task.getResult().size();
                    DocumentReference profile;
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                        profile = documentSnapshot.getReference();
                        profile.update("totalSplit", totalItems);
                        profile.update("amountOwed", totalPrice/(profileSize+1));
                    }
                }
            }
        });
        setUpRecyclerView();

    }
    private void setUpRecyclerView() {
        Query query = profiles;
        FirestoreRecyclerOptions<ProfileItem> items = new FirestoreRecyclerOptions.Builder<ProfileItem>()
                .setQuery(query, ProfileItem.class)
                .build();

        adapter = new ProfileViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.recyclerProfile2);
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

}