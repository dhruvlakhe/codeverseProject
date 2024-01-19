// add_alarm.java
package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class add_alarm extends AppCompatActivity {
    TimePicker tp;
    dbHelper db;
CustomAdapter CustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        tp = findViewById(R.id.tp);
        db = new dbHelper(this);
    }

    public void addAlarm(View view) {
        int hour = tp.getHour();
        int minute = tp.getMinute();
        String formattedTime = String.format("%02d:%02d", hour, minute);
        String label = "Your Alarm Label";

        dbHelper db = new dbHelper(this);
        long alarmId = db.addTask(formattedTime, label, true);
        scheduleAlarm(alarmId, hour, minute);

        // Update the data list in the adapter and refresh the RecyclerView
        CustomAdapter.setAlarms(db.getAllAlarms());
        CustomAdapter.notifyDataSetChanged();
    }


    private void scheduleAlarm(long alarmId, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent("com.example.alarmclock.ALARM_TRIGGEREDz");
        intent.putExtra("ALARM_ID", alarmId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }
}
