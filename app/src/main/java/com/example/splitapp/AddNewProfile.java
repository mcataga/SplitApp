package com.example.splitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AddNewProfile extends AppCompatActivity {
    private static final String TAG = "AddNewProfile";
    private String activityId;
    private String profileName;
    private String profileNumber;
    private String profileEmail;
    private String profileAddress;
    private String profileNotes;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);
        Intent intent = getIntent();
        activityId = intent.getStringExtra("docId");
        EditText editProfileName = (EditText)findViewById(R.id.editProfileName);
        EditText editProfileNumber = (EditText)findViewById(R.id.editTextPhone);
        EditText editProfileEmail = (EditText)findViewById(R.id.editEmail);
        EditText editProfileAddress = (EditText)findViewById(R.id.editAddress);
        EditText editProfileNotes = (EditText)findViewById(R.id.editMisc);
        String userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectionReference = fStore.collection("users").document(userID).collection("activities").document(activityId).collection("profiles");
        findViewById(R.id.btnSaveProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName = editProfileName.getText().toString();
                profileNumber = editProfileNumber.getText().toString();
                profileEmail = editProfileEmail.getText().toString();
                profileAddress = editProfileAddress.getText().toString();
                profileNotes = editProfileNotes.getText().toString();
                Map<String, Object> profile = new HashMap<>();
                profile.put("name", profileName);
                profile.put("phoneNumber", profileNumber);
                profile.put("email", profileEmail);
                profile.put("address", profileAddress);
                profile.put("notes", profileNotes);
                    collectionReference.add(profile).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully created new profile");
                            finish();
                        }
                        else {
                            Log.d(TAG, "Creating new profile failed");
                        }
                    }
                });
            }
        });
    }
}