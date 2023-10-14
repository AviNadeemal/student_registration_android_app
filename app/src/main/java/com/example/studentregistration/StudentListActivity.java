package com.example.studentregistration;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        dbHelper = new DBHelper(this);
        displayStudentCards();

        Button buttonAddStudent = findViewById(R.id.buttnAddStudent);
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentDialog();
            }
        });
    }

    private void displayStudentCards() {
        LinearLayout container = findViewById(R.id.container);
        List<Student> studentList = dbHelper.getAllStudents();

        for (Student student : studentList) {
            View studentCard = getLayoutInflater().inflate(R.layout.student_card, null);
            TextView textViewName = studentCard.findViewById(R.id.textViewName);
            TextView textViewEmail = studentCard.findViewById(R.id.textViewEmail);
            TextView textViewLocation = studentCard.findViewById(R.id.textViewLocation);
            TextView textViewPassword = studentCard.findViewById(R.id.textViewPassword);
            TextView textViewAge = studentCard.findViewById(R.id.textViewAge);

            textViewName.setText("Name: " + student.getName());
            textViewEmail.setText("Email: " + student.getEmail());
            textViewLocation.setText("Location: " + student.getLocation());
            textViewPassword.setText("Password: " + student.getPassword());
            textViewAge.setText("Age: " + student.getAge());

            studentCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showEditDialog(student);
                    return true;
                }
            });

            container.addView(studentCard);
        }
    }

    private void showEditDialog(Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_student_popup, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);

        editTextName.setText(student.getName());
        editTextEmail.setText(student.getEmail());
        editTextLocation.setText(student.getLocation());
        editTextPassword.setText(student.getPassword());
        editTextAge.setText(String.valueOf(student.getAge()));

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteStudent(student.getId());
                recreate();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedName = editTextName.getText().toString();
                String updatedEmail = editTextEmail.getText().toString();
                String updatedLocation = editTextLocation.getText().toString();
                String updatedPassword = editTextPassword.getText().toString();
                int updatedAge = Integer.parseInt(editTextAge.getText().toString());

                dbHelper.updateStudent(student.getId(), updatedName, updatedEmail, updatedLocation, updatedPassword, updatedAge);
                recreate();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_student_popup, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                int age = Integer.parseInt(editTextAge.getText().toString().trim());

                dbHelper.addStudent(name, email, location, password, age);
                recreate();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}