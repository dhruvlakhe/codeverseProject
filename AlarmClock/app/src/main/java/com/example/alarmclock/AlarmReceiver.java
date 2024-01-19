// AlarmReceiver.java
package com.example.alarmclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_TRIGGERED_ACTION = "com.example.alarmclock.ALARM_TRIGGERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm received");

        if (intent.getAction() != null && intent.getAction().equals(ALARM_TRIGGERED_ACTION)) {
            // Show a dialog to stop the alarm
            AlarmService.playAlarm(context);
            // Display a dialog or perform UI actions here
            showStopDialog(context);
        }
    }
    private void showStopDialog(final Context context) {
        if (!(context instanceof Activity)) {
            // Ensure that the context is an instance of Activity before showing the dialog
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Stop Alarm?")
                .setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmService.stopAlarm(context);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Implement snooze functionality if needed
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();

        // Use the application context to prevent BadTokenException
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        // Check if the activity is not finishing before showing the dialog
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
    }


}
