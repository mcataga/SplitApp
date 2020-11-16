package com.example.splitapp;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment {
    EditText registerFirstName, registerLastName, registerEmail, registerPassword;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String TAG = "REGISTER";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_activity, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        registerFirstName = (EditText)view.findViewById(R.id.registerFirstName);
        registerLastName = (EditText)view.findViewById(R.id.registerLastName);
        registerEmail = (EditText)view.findViewById(R.id.registerEmailAddress);
        registerPassword = (EditText)view.findViewById(R.id.registerPassword);

        view.findViewById(R.id.btnLoginActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Register.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }
    private void registerUser() {
         String email = registerEmail.getText().toString().trim();
         String password = registerPassword.getText().toString().trim();
         String firstName = registerFirstName.getText().toString().trim();
         String lastName = registerLastName.getText().toString().trim();

         if (email.isEmpty()) {
             registerEmail.setError("Email is required");
             registerEmail.requestFocus();
             return;
         }
         if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
             registerEmail.setError("Please enter a valid email");
             registerEmail.requestFocus();
             return;
         }
         if (password.isEmpty()) {
             registerPassword.setError("Password is required");
             registerPassword.requestFocus();
             return;
         }
         if (password.length() < 6) {
             registerPassword.setError("Password must be over 6 characters");
             registerPassword.requestFocus();
             return;
         }
         fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     Toast.makeText(getActivity().getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                     String userID = fAuth.getCurrentUser().getUid();
                     Log.d(TAG, "User has been created in Firebase authentication " + userID);
                     DocumentReference documentReference = fStore.collection("users").document(userID);
                     Map<String, Object> user = new HashMap<>();
                     user.put("firstName", firstName);
                     user.put("firstName", lastName);
                     user.put("email", email);
                     user.put("userID", userID);
                     user.put("profiles", " ");
                     user.put("activities", " ");
                     documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Log.d(TAG, "User has successfully been stored in Firestore");
                         }
                     });

                 }
             }
         });
    }
}