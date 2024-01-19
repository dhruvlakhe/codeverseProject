package com.example.todo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    dbHelper db;
    ArrayList<String> task_id, task_title, task_description, task_date, task_time, task_signal,task_status;

    customAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new dbHelper(MainActivity.this);

        recyclerView = findViewById(R.id.recyclerView);



        // Create the adapter without passing empty lists
        customAdapter = new customAdapter(MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(customAdapter);

        // Call storeDataInArrays after initializing the adapter
        storeDataInArrays();





    }
    @Override
    protected void onResume() {
        super.onResume();
        storeDataInArrays();
    }

    public void add_task(View view) {
        Intent i = new Intent(MainActivity.this, add_task.class);
        startActivity(i);
    }

    void storeDataInArrays() {
        task_id = new ArrayList<>();
        task_title = new ArrayList<>();
        task_description = new ArrayList<>();
        task_date = new ArrayList<>();
        task_time = new ArrayList<>();
        task_status = new ArrayList<>();

        Cursor cursor = db.readAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                task_id.add(cursor.getString(0));
                task_title.add(cursor.getString(1));
                task_description.add(cursor.getString(2));
                task_date.add(cursor.getString(3));
                task_time.add(cursor.getString(4));
                task_status.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }

        // Log the data to check if it's being fetched correctly
        for (int i = 0; i < task_id.size(); i++) {
            Log.d("Data", "ID: " + task_id.get(i) +
                    ", Title: " + task_title.get(i) +
                    ", Description: " + task_description.get(i) +
                    ", Date: " + task_date.get(i) +
                    ", Time: " + task_time.get(i) +
                    ", Status: " + task_status.get(i));
        }

        // Use the new method to update adapter data
        customAdapter.updateData(task_id, task_title, task_description, task_date, task_time, task_status);
    }


}
