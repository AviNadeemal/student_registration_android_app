package com.example.studentregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_AGE + " INTEGER " +
                    ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public void addStudent(String name, String email, String location, String password, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_AGE, age);

        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                student.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                student.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                student.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                student.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                student.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_AGE)));

                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentList;
    }

    public void updateStudent(int id, String name, String email, String location, String password, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_AGE, age);

        // Updating row
        db.update(TABLE_STUDENTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean checkCredentials(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            // Query to check if email and password match
            String query = "SELECT * FROM " + TABLE_STUDENTS +
                    " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{email, password});

            // If a row with the provided email and password exists, return true
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }



    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(studentId)});
        db.close();
    }
}