package com.example.alarmclock;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<Alarm> alarms;
    private LayoutInflater inflater;
    private dbHelper db;

    public CustomAdapter(Context context, List<Alarm> alarms, dbHelper db) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.alarms = alarms;
        this.db = db;
    }

    // Add this method to set the list of alarms
    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.timeTextView.setText(alarm.getTime());
        holder.labelToggleButton.setChecked(alarm.isEnabled());

        // Open a time picker dialog when the time TextView is clicked
        holder.timeTextView.setOnClickListener(v -> showTimePickerDialog(alarm));

        // Set the alarm status in the database when the ToggleButton is checked
        holder.labelToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setEnabled(isChecked);
            db.updateAlarmStatus(alarm.getId(), isChecked);

            // Use a Handler to post notifyDataSetChanged to the main thread's message queue
            new Handler(Looper.getMainLooper()).post(() -> {
                notifyDataSetChanged(); // Refresh the RecyclerView
            });
        });
    }

    // Method to show a time picker dialog for editing alarm time
    private void showTimePickerDialog(Alarm alarm) {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

            // Update the alarm time in the database
            db.updateAlarmTime(alarm.getId(), formattedTime);

            // Update the data list and refresh the RecyclerView
            setAlarms(db.getAllAlarms());
        };

        String[] timeParts = alarm.getTime().split(":");
        int initialHour = Integer.parseInt(timeParts[0]);
        int initialMinute = Integer.parseInt(timeParts[1]);

        // Show the time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeSetListener, initialHour, initialMinute, true);
        timePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        ToggleButton labelToggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            labelToggleButton = itemView.findViewById(R.id.labelToggleButton);
        }
    }
}
