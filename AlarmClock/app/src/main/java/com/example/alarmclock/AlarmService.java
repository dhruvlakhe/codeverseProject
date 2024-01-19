package com.example.alarmclock;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmService extends IntentService {

    public static final String ACTION_PLAY_ALARM = "com.example.alarmclock.action.PLAY_ALARM";
    public static final String ACTION_STOP_ALARM = "com.example.alarmclock.action.STOP_ALARM";
    private static Ringtone ringtone;

    public AlarmService() {
        super("AlarmService");
    }

    public static void playAlarm(Context context) {
        if (ringtone == null) {
            // Create the ringtone only if it's not already created
            Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context, defaultRingtoneUri);
        }

        // Start playing the alarm sound
        if (!ringtone.isPlaying()) {
            ringtone.play();
        }
    }

    public static void stopAlarm(Context context) {
        // Check if the ringtone is playing and stop it
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PLAY_ALARM.equals(action)) {
                // Logic for playing the alarm sound
                playAlarm(this);
                // Show a notification or trigger UI (dialog) to inform the user
                showAlarmNotification();
            } else if (ACTION_STOP_ALARM.equals(action)) {
                // Logic for stopping the alarm sound
                stopAlarm(this);
            }
        }
    }

    private void showAlarmNotification() {
        // You can implement notification logic here
        // This can include creating a notification, launching an activity, or showing a dialog
        // For simplicity, let's show a notification using NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "alarm_channel_id")
                .setContentTitle("Alarm")
                .setContentText("Time to wake up!")
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(123, builder.build());
    }
}
