package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManageBill extends AppCompatActivity {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    EditText billName;
    EditText billPrice;
//    ImageView imageHolder;
    String activityId;
    public Uri imageUri;
    String imageId;
    private FirebaseStorage imgdb;
    private StorageReference ref;
    CollectionReference collectionReference;
    private static final String TAG = "ManageBill";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        imgdb = FirebaseStorage.getInstance();
        ref = imgdb.getReference();
        billName = (EditText)findViewById(R.id.editBillName);
        billPrice = (EditText)findViewById(R.id.editAmountPaid);
//        imageHolder = (ImageView)findViewById(R.id.uploadedBill);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        activityId = intent.getStringExtra("docId");
        collectionReference = fStore.collection("users").document(userID).collection("activities").document(activityId).collection("bills");


        findViewById(R.id.btnSaveNewBill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBill();
            }
        });
        findViewById(R.id.btnAddPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });
    }

    private void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData()!=null) {
            imageId = UUID.randomUUID().toString();
            imageUri = data.getData();
//            Picasso.get().load(imageUri).into(imageHolder);
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBill() {
        String name = billName.getText().toString().trim();
        String temp = billPrice.getText().toString().trim();
        if (name.isEmpty()) {
            billName.setError("Bill Name is required");
            billName.requestFocus();
            return;
        }
        if (temp.isEmpty()) {
            billPrice.setError("Amount paid is required");
            billPrice.requestFocus();
            return;
        }
        if (imageId == null) {
            imageId = "";
        }
        double totalPaid = Double.parseDouble(temp);
        Map<String, Object> bill = new HashMap<>();
        bill.put("name", name);
        bill.put("amountPaid", totalPaid);
        bill.put("imageId", imageId);
        if(!imageId.isEmpty()) uploadFile();
        collectionReference.add(bill).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    bill.put("id", task.getResult().getId());
                    task.getResult().set(bill);
                    finish();
                    Log.d(TAG, "Successfully created new bill");
                }
                else {
                    Log.d(TAG, "Creating new bill failed");
                }
            }
        });
    }
    private void uploadFile() {
        StorageReference reference = ref.child("images/" + imageId);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Image Upload Successful!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Image Upload Failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}