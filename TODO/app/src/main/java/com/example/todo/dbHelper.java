package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

public class dbHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "TASK.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "mytasks";
    private static final String TASK_ID = "_id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESCRIPTION = "task_description";
    private static final String TASK_DATE = "task_date";
    private static final String TASK_TIME = "task_time";
    private static final String STATUS = "status";

    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_TITLE + " TEXT, " +
                TASK_DESCRIPTION + " TEXT, " +
                TASK_DATE + " TEXT, " +
                TASK_TIME + " TEXT, " +
                STATUS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addTask(String title, String description, String date, String time, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, title);
        cv.put(TASK_DESCRIPTION, description);
        cv.put(TASK_DATE, date);
        cv.put(TASK_TIME, time);
        cv.put(STATUS, status);

        try {
            long result = db.insert(TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            if (db != null) {
                cursor = db.rawQuery(query, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    void updateTask(String taskId, String title, String description, String date, String time, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, title);
        cv.put(TASK_DESCRIPTION, description);

        cv.put(TASK_DATE, date);
        cv.put(TASK_TIME, time);
        cv.put(STATUS, status);


        try {
            int result = db.update(TABLE_NAME, cv, TASK_ID + "=?", new String[]{taskId});
            if (result > 0) {
                Toast.makeText(context, "UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "UPDATE FAILED", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    // Add this method to your dbHelper class
    void updateStatus(String taskId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STATUS, newStatus);

        try {
            int result = db.update(TABLE_NAME, cv, TASK_ID + "=?", new String[]{taskId});
            if (result > 0) {
                Toast.makeText(context, "STATUS UPDATED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "STATUS UPDATE FAILED", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    void deleteOneRecord(String taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, TASK_ID + "=?", new String[]{taskId});

        if (result == -1) {
            Toast.makeText(context, "FAILED TO DELETE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "DELETED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        }

        db.close(); // Make sure to close the database connection

    }

}

