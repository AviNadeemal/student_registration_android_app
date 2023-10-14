package com.example.studentregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private DBHelper dbHelper;  // Declare dbHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize dbHelper
        dbHelper = new DBHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonAddStudent = findViewById(R.id.buttonAddStudent);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check the database for the given username and password
                boolean isValidCredentials = dbHelper.checkCredentials(email, password);

                if (isValidCredentials) {
                    // Successful login, navigate to StudentListActivity
                    Intent intent = new Intent(LoginActivity.this, StudentListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a new activity to add a student
                Intent intent = new Intent(LoginActivity.this, AddStudent.class);
                startActivity(intent);
            }
        });

    }
}