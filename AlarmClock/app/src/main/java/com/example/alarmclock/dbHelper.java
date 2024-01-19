package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "alarms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_IS_ENABLED = "is_enabled";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_LABEL + " TEXT, " +
                COLUMN_IS_ENABLED + " INTEGER DEFAULT 1);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    long addTask(String time, String label, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_LABEL, label);
        cv.put(COLUMN_IS_ENABLED, isEnabled ? 1 : 0);

        long result = -1; // Initialize the result variable

        try {
            result = db.insert(TABLE_NAME, null, cv);
            if (result == -1) {
                // Handle insertion failure
            } else {
                // Handle insertion success
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result; // Return the ID of the newly inserted row
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {COLUMN_ID, COLUMN_TIME, COLUMN_LABEL, COLUMN_IS_ENABLED};
            cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                    String label = cursor.getString(cursor.getColumnIndex(COLUMN_LABEL));
                    int isEnabled = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ENABLED));

                    Alarm alarm = new Alarm(id, time, label, isEnabled == 1);
                    alarms.add(alarm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return alarms;
    }

    void updateAlarmStatus(int alarmId, boolean newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IS_ENABLED, newStatus ? 1 : 0);

        try {
            db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(alarmId)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    void updateAlarmTime(int alarmId, String newTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TIME, newTime);

        try {
            db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(alarmId)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // New method to delete an alarm by its ID
    void deleteAlarm(int alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(alarmId)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Updated method to receive an Alarm object
    void addAlarmWithNotification(Alarm alarm) {
        // Convert time to milliseconds
        long alarmTimeMillis = convertTimeToMillis(alarm.getTime());

        // Check if the alarm is enabled
        if (alarm.isEnabled()) {
            // Create an Intent for the AlarmReceiver
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.setAction(AlarmReceiver.ALARM_TRIGGERED_ACTION);

            // Create a PendingIntent to be triggered when the alarm fires
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            // Set the alarm using AlarmManager
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }

        // Insert the alarm into the database
        addTask(alarm.getTime(), "", alarm.isEnabled()); // Assuming label is an optional field in your database
    }

    private long convertTimeToMillis(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        return (hours * 60 + minutes) * 60 * 1000;
    }
}
