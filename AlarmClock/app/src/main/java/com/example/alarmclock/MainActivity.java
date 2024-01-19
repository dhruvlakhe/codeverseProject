// MainActivity.java
package com.example.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
     CustomAdapter customAdapter;
    dbHelper db;
    private static final int ADD_ALARM_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        db = new dbHelper(this);

        customAdapter = new CustomAdapter(this, db.getAllAlarms(), db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
    }

    public void add_alarm(View view) {
        Intent i = new Intent(MainActivity.this, add_alarm.class);
        startActivityForResult(i, ADD_ALARM_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ALARM_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle any actions after the add_alarm activity finishes (if needed)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.setAlarms(db.getAllAlarms());
        customAdapter.notifyDataSetChanged();
    }
}
