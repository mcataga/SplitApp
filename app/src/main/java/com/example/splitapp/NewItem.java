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
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewItem extends AppCompatActivity {
    private static final String TAG = "NewItem";
    EditText itemName;
    EditText itemPrice;
    private String billId;
    private String activityId;
    private double price;
    private double amountOwed;
    private String name;
    private String itemId;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private CollectionReference profiles;
    private DocumentReference billRef;
    private ProfileViewAdapter adapter;
    private ArrayList<ProfileItem> selectedItems;
    private HashMap<ProfileItem, Boolean> selectedItemsMap;
    private FirestoreRecyclerOptions<ProfileItem> items ;
    private static final double DELTA = 1e-15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Intent intent = getIntent();
        billId = intent.getStringExtra("billId");
        activityId = intent.getStringExtra("activityId");
        price = intent.getDoubleExtra("price", DELTA);
        itemId = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        amountOwed = intent.getDoubleExtra("amountOwed", DELTA);
        itemName = (EditText)findViewById(R.id.editItemName);
        itemPrice = (EditText)findViewById(R.id.editItemPrice);
        if(itemId != null) {
            itemName.setText(name);
            itemPrice.setText(String.valueOf(price));
        }
        profiles = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("profiles");
        Log.d(TAG, "THIS IS THE PATH" + profiles.getPath());
        collectionReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId).collection("items");
        billRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId);
        billRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                amountOwed = documentSnapshot.getDouble("amountOwed");
            }
        });
        findViewById(R.id.saveItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }

        });
        selectedItems = new ArrayList<>();
        selectedItemsMap = new HashMap<>();
        setUpRecyclerView();
        adapter.setOnProfileClickListener(new ProfileViewAdapter.OnProfileClickListener() {
            @Override
            public void onItemClick(ProfileItem item ,DocumentSnapshot documentSnapshot) {
                selectedItemsMap.put(item,item.getIsVisible());
            }
        });
    }
    private void setUpRecyclerView() {
        Query query = profiles;
        items = new FirestoreRecyclerOptions.Builder<ProfileItem>()
                .setQuery(query, ProfileItem.class)
                .build();

        adapter = new ProfileViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.recyclerProfile);
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
    private void saveButton() {
        double remainingValue = 0.0;
        for(ProfileItem item: selectedItemsMap.keySet()) {
            if (selectedItemsMap.get(item)){
                selectedItems.add(item);
            }
        }
        HashMap<String, Object> item = new HashMap<>();
        String name = itemName.getText().toString().trim();
        String temp = itemPrice.getText().toString().trim();
        item.put("name", name);
        item.put("price", Double.parseDouble(temp));
        int divisor = selectedItems.size()+1;
        for(ProfileItem item1: selectedItems){
            profiles.document(item1.getId()).update("amountOwed", item1.getAmountOwed()+(Double.parseDouble(temp)/divisor));
            Log.d(TAG, "SPLIT AMOUNT! " + (Double.parseDouble(temp)/divisor));
            Log.d(TAG, "THE REAL NUMBER IS HERE" + divisor);
            remainingValue+=(Double.parseDouble(temp)/divisor);
            profiles.document(item1.getId()).update("totalSplit", item1.getTotalSplit()+1);
        }
        if (selectedItems.size() == 1) {
            billRef.update("amountOwed", (amountOwed + remainingValue));
        }
        else if(selectedItems.size() > 1)
            billRef.update("amountOwed", (amountOwed + remainingValue/(divisor-1)));

        if(itemId == null) {
            collectionReference.add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        item.put("id", task.getResult().getId());
                        task.getResult().set(item);
                        finish();
                        Toast.makeText(NewItem.this, "Successfully created New Item", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Creating new item failed");
                    }
                }
            });
        }
        else {
            collectionReference.document(itemId).set(item);
            finish();
        }
    }
}