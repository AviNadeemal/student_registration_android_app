package com.example.studentregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class AddStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = findViewById(R.id.editTextName);
                EditText editTextEmail = findViewById(R.id.editTextEmail);
                EditText editTextLocation = findViewById(R.id.editTextLocation);
                EditText editTextPassword = findViewById(R.id.editTextPassword);
                EditText editTextAge = findViewById(R.id.editTextAge);

                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String ageString = editTextAge.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || location.isEmpty() || password.isEmpty() || ageString.isEmpty()) {
                    Toast.makeText(AddStudent.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email using regex
                if (!isEmailValid(email)) {
                    Toast.makeText(AddStudent.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(ageString);
                    if (age < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AddStudent.this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHelper dbHelper = new DBHelper(AddStudent.this);

                // Try to add the student
                try {
                    dbHelper.addStudent(name, email, location, password, age);
                    Intent intent = new Intent(AddStudent.this, StudentListActivity.class);
                    startActivity(intent);

                    // Clear the input fields
                    editTextName.setText("");
                    editTextEmail.setText("");
                    editTextLocation.setText("");
                    editTextPassword.setText("");
                    editTextAge.setText("");
                } catch (Exception e) {
                    Toast.makeText(AddStudent.this, "Error adding student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Regex to validate email
    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}