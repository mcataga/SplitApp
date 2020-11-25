package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String TAG = "AddActivity";
    private EditText activityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        activityName = (EditText)findViewById(R.id.activityName);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectionReference = fStore.collection("users").document(userID).collection("activities");

        findViewById(R.id.btnAddActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = activityName.getText().toString().trim();
                Map<String, Object> activity = new HashMap<>();
                activity.put("name", name);
                collectionReference.add(activity).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully created new activity");
                            finish();
                        }
                        else {
                            Log.d(TAG, "Creating new activity failed");
                        }
                    }
                });
            }
        });
    }
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}