package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateTaskActivity extends AppCompatActivity {

    EditText title_input, description_input, status_input;
    DatePicker date_input;
    TimePicker time_input;

    String id, title, description, date, time, signal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        title_input = findViewById(R.id.updateTaskTitle);
        description_input = findViewById(R.id.updateTaskDescription);
        date_input = findViewById(R.id.updateTaskDate);
        time_input = findViewById(R.id.updateTaskTime);
        status_input = findViewById(R.id.taskStatus);

        // Call the method to get and set intent data
        getAndSetIntentData();
    }

    public void update_task(View view) {
        dbHelper db = new dbHelper(UpdateTaskActivity.this);

        // Get the status from the user input
        String status = status_input.getText().toString().trim();

        // Validate if the status is not empty
        if (!status.isEmpty()) {
            // Update the task with the provided details and status
            db.updateTask(id, title_input.getText().toString().trim(),
                    description_input.getText().toString().trim(),
                    date, time, status);

            db.close();  // Close the dbHelper instance to avoid potential memory leaks

            // Optionally, you can show a toast or perform other actions to notify the user
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Notify the user that the status cannot be empty
            Toast.makeText(this, "Please enter a valid status", Toast.LENGTH_SHORT).show();
        }
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id")
                && getIntent().hasExtra("title")
                && getIntent().hasExtra("description")
                && getIntent().hasExtra("date")
                && getIntent().hasExtra("time")) {

            // getting intent data
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");

            // setting intent data
            title_input.setText(title);
            description_input.setText(description);

            // Split the date string into year, month, and day
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
            int day = Integer.parseInt(dateParts[2]);
            date_input.updateDate(year, month, day);

            // Split the time string into hour and minute
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            time_input.setHour(hour);
            time_input.setMinute(minute);

        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

}
