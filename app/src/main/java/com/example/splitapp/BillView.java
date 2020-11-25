package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BillView extends AppCompatActivity {
    private static final String TAG = "BillView";
    private String billName;
    private String billId;
    private String activityId;
    private EditText totalPrice;
    private StorageReference imgref;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseStorage imgdb = FirebaseStorage.getInstance();
    private DocumentReference billRef;
    private DocumentSnapshot billItem;
    private ImageView imageHolder;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        Intent intent = getIntent();
        imageHolder = (ImageView)findViewById(R.id.billPicture);
        totalPrice = (EditText)findViewById(R.id.editAmountOwed);
        billName = intent.getStringExtra("name");
        Log.d(TAG, intent.getStringExtra("name"));
        billId = intent.getStringExtra("billId");
        activityId = intent.getStringExtra("activityId");
        billRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(activityId).collection("bills").document(billId);
        billRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    billItem = task.getResult();
                    if (billItem.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + billItem.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        totalPrice.setText(billItem.getString("totalPrice"));
        imgref = imgdb.getReference().child("images/" + billItem.getString("ImageId"));
        imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(imageHolder);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}