package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class add_task extends AppCompatActivity {

    EditText addTaskTitle, addTaskDescription, taskStatus;
    DatePicker taskDate;
    TimePicker taskTime;
    String formatedDate, formatedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTaskTitle = findViewById(R.id.addTaskTitle);
        addTaskDescription = findViewById(R.id.addTaskDescription);
        taskDate = findViewById(R.id.taskDate);
        taskTime = findViewById(R.id.taskTime);
        taskStatus = findViewById(R.id.taskStatus);
    }

    public void add_task(View view) {
        String title = addTaskTitle.getText().toString().trim();
        String description = addTaskDescription.getText().toString().trim();
        String status = taskStatus.getText().toString().trim();

        if (!title.isEmpty()) {
            // Perform the date and time formatting as you are currently doing
            int year = taskDate.getYear();
            int month = taskDate.getMonth();
            int day = taskDate.getDayOfMonth();
            int hour = taskTime.getHour();
            int minute = taskTime.getMinute();
            formatedDate = String.format("%04d-%02d-%02d", year, month + 1, day); // Adjust month (+1) as it is zero-based.
            formatedTime = String.format("%02d:%02d", hour, minute);

            // Add the task only if the title is not empty
            dbHelper db = new dbHelper(add_task.this);
            db.addTask(title, description, formatedDate, formatedTime, status);

            // Optionally, you can finish the activity or clear input fields after adding the task
            // finish();
            // clearInputFields();
        } else {
            // Display a message or toast indicating that the title is required
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
        }
    }

    // Optionally, add a method to clear input fields
    private void clearInputFields() {
        addTaskTitle.setText("");
        addTaskDescription.setText("");
        taskStatus.setText("");
    }
}
