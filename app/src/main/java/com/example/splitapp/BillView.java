package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class BillView extends AppCompatActivity {
    private static final String TAG = "BillView";
    private String billName;
    private String billId;
    private String activityId;
    private String price;
    private String imageId;
    private TextView totalPaid;
    private double amountOwed;
    private EditText nameBill;
    private TextView totalPrice;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private DocumentReference billRef;
    private CollectionReference items1;
    private ItemViewAdapter adapter;
    public boolean onCreate;
    Locale locale = new Locale("en", "US");
    NumberFormat format = NumberFormat.getCurrencyInstance(locale);
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        Intent intent = getIntent();
        onCreate = true;
        nameBill = (EditText)findViewById(R.id.editNameBill);
        totalPaid = (TextView)findViewById(R.id.editTotalPaid);
        totalPrice = (TextView)findViewById(R.id.totalPrice);
        billName = intent.getStringExtra("name");
        billId = intent.getStringExtra("billId");
        activityId = intent.getStringExtra("docId");
        items1 = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId).collection("items");
        billRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId);
        queryBills();
        findViewById(R.id.viewReceipt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BillImage.class);
                intent.putExtra("imageId", imageId);
                startActivity(intent);
            }
        });
        findViewById(R.id.btnSaveExistingBill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton();
            }
        });
        findViewById(R.id.btnNewItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItemButton();
            }
        });
        findViewById(R.id.viewProfiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProfileButton();
            }
        });
        billRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                amountOwed = documentSnapshot.getDouble("amountOwed");
                totalPaid.setText(format.format(amountOwed));
                totalPrice.setText(format.format((documentSnapshot.getDouble("totalPrice"))));
            }
        });
        setUpRecyclerView();
        adapter.setOnItemClickListener(new ItemViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(BillView.this, NewItem.class);
                intent.putExtra("billId", billId);
                intent.putExtra("activityId", activityId);
                intent.putExtra("name", documentSnapshot.getString("name"));
                intent.putExtra("price", documentSnapshot.getDouble("price"));
                intent.putExtra("id", documentSnapshot.getId());
                startActivity(intent);
            }
        });
    }

    private void viewProfileButton() {
        Intent intent = new Intent(BillView.this, AddProfileToBill.class);
        intent.putExtra("billId", billId);
        intent.putExtra("activityId", activityId);
        startActivity(intent);
    }

    private void newItemButton() {
        Intent intent = new Intent(BillView.this, NewItem.class);
        intent.putExtra("billId", billId);
        intent.putExtra("amountOwed", amountOwed);
        intent.putExtra("activityId", activityId);
        startActivity(intent);
    }

    private void queryBills() {
        billRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult();
                    imageId= (String)task.getResult().getData().get("imageId");
                    nameBill.setText(billName);
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void saveButton() {
        HashMap<String, Object> billDetails = new HashMap<>();
        billDetails.put("name", nameBill.getText().toString().trim());
        billDetails.put("imageId", imageId);
        billRef.set(billDetails, SetOptions.merge());
        Toast.makeText(this, "Bill has successfully been updated", Toast.LENGTH_SHORT).show();
    }

    private void setUpRecyclerView() {
        Query query = items1;
        FirestoreRecyclerOptions<ItemItem> items = new FirestoreRecyclerOptions.Builder<ItemItem>()
                .setQuery(query, ItemItem.class)
                .build();

        adapter = new ItemViewAdapter(items);

        RecyclerView recyclerView = findViewById(R.id.recyclerItems2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!onCreate)
        resetView();
        onCreate = false;
        adapter.startListening();

    }

    private void resetView() {
        items1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    double total = 0.0;
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                        total = total + documentSnapshot.getDouble("price");
                        Log.d(TAG, "this is total" + total);
                    }
                    billRef.update("totalPrice", total).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Bill Total Updated");

                        }
                    });
                    totalPrice.setText("Total Price: "+format.format(total));
                    billRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            totalPaid.setText(format.format(documentSnapshot.getDouble("amountOwed")));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.deleteItem(viewHolder.getAdapterPosition());
            Toast.makeText(BillView.this, "Item has been deleted", Toast.LENGTH_SHORT).show();
            resetView();
        }
    };
}