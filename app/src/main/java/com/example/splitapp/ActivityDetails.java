package com.example.splitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ActivityDetails extends AppCompatActivity {
    private static final String TAG = "ActivityDetails";
    private String activityName;
    private String activityId;
    private TextView activityDetailName;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private CollectionReference bills;
    private BillViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        activityDetailName = (TextView) findViewById(R.id.activityDetailsName);
        Intent intent = getIntent();
        activityName = intent.getStringExtra("name");
        Log.d(TAG, intent.getStringExtra("name"));
        activityId = intent.getStringExtra("docId");
        Log.d(TAG, activityId);
        activityDetailName.setText(activityName);
        bills = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills");
        findViewById(R.id.activityDetailBtnProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewProfile.class);
                intent.putExtra("docId", activityId);
                startActivity(intent);
            }
        });
        findViewById(R.id.activityDetailBtnBill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageBill.class);
                intent.putExtra("docId", activityId);
                startActivity(intent);
            }
        });
        setUpRecyclerView();
        adapter.setOnBillClickListener(new BillViewAdapter.OnBillClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(ActivityDetails.this, BillView.class);
                intent.putExtra("billId", documentSnapshot.getId());
                Log.d(TAG, "docID" + activityId);
                intent.putExtra("docId", activityId);
                intent.putExtra("amountOwed", documentSnapshot.getDouble("totalPrice"));
                intent.putExtra("name", documentSnapshot.getString("name"));
                Log.d(TAG, "GOING TO BILLS YEEHAW");
                startActivity(intent);
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = bills;
        FirestoreRecyclerOptions<BillItem> items = new FirestoreRecyclerOptions.Builder<BillItem>()
                .setQuery(query, BillItem.class)
                .build();

        adapter = new BillViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.billRecyclerView);
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