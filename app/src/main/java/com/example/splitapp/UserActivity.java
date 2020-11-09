package com.example.splitapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splitapp.db.AppDatabase;
import com.example.splitapp.db.User;

public class UserActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setContentView(R.layout.fragment_first);

        final EditText firstname = findViewById(R.id.firstName);
        final EditText lastname = findViewById(R.id.lastName);
        final EditText email = findViewById(R.id.emailAddress);
        final EditText password = findViewById(R.id.passwordField);

        Button save = findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(firstname.getText().toString(), lastname.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void saveUser(String first, String last, String email, String pass){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.firstName = first;
        user.lastName = last;
        user.email = email;
        user.password = pass;

        db.userDao().insertUser(user);

        finish();
    }
}
