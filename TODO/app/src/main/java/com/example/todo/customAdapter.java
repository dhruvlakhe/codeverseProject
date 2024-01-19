package com.example.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class customAdapter extends RecyclerView.Adapter<customAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> task_id, task_title, task_description, task_date, task_time, task_status;

    private dbHelper db; // Add this line

    public customAdapter(Context context) {
        this.context = context;
        this.task_id = new ArrayList<>();
        this.task_title = new ArrayList<>();
        this.task_description = new ArrayList<>();
        this.task_date = new ArrayList<>();
        this.task_time = new ArrayList<>();
        this.task_status = new ArrayList<>();

        db = new dbHelper(context);
    }

    // Add a method to update data
    public void updateData(ArrayList<String> task_id,
                           ArrayList<String> task_title,
                           ArrayList<String> task_description,
                           ArrayList<String> task_date,
                           ArrayList<String> task_time,
                           ArrayList<String> task_status) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_description = task_description;
        this.task_date = task_date;
        this.task_time = task_time;
        this.task_status = task_status;
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.options_menu, popupMenu.getMenu());

        // Set OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        // Implement delete functionality
                        showDeleteConfirmationDialog(position);
                        break;
                    case R.id.menu_update:
                        // Implement update functionality
                        Intent intent = new Intent(context, UpdateTaskActivity.class);
                        intent.putExtra("id", task_id.get(position)); // Pass the task ID to identify the specific task
                        intent.putExtra("title", task_title.get(position));
                        intent.putExtra("description", task_description.get(position));
                        intent.putExtra("date", task_date.get(position));
                        intent.putExtra("time", task_time.get(position));
                        intent.putExtra("status", task_status.get(position));
                        intent.putExtra("UPDATE_MODE", true); // Set a flag to indicate update mode
                        context.startActivity(intent);
                        break;
                    case R.id.menu_completed:
                        // Build and show a confirmation dialog
                        showCompletionConfirmationDialog(position);
                        break;

                }
                return true;
            }
        });

        // Show the PopupMenu
        popupMenu.show();
    }
    private void showCompletionConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Task Completed");
        builder.setMessage("Are you sure you want to mark this task as completed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update status in the database
                db.updateStatus(task_id.get(position), "Completed");

                // Optionally, you may want to update the UI immediately
                task_status.set(position, "Completed");
                notifyDataSetChanged(); // Notify the adapter of the data change
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the completion
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    // Confirm dialog
    // Confirm dialog
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed deletion, proceed to delete the task
                db.deleteOneRecord(task_id.get(position));

                // Remove the deleted item from all lists
                task_id.remove(position);
                task_title.remove(position);
                task_description.remove(position);
                task_date.remove(position);
                task_time.remove(position);
                task_status.remove(position);

                // Notify the adapter that the data has changed
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the deletion
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    @NonNull
    @Override
    public customAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.MyViewHolder holder, int position) {
        Log.d("DataAdapter", "onBindViewHolder called for position: " + position);

        // Parsing date
        String dateString = task_date.get(position);
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Setting date views
        if (date != null) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            holder.day.setText(dayFormat.format(date));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            holder.date.setText(dateFormat.format(date));

            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            holder.month.setText(monthFormat.format(date));
        }

        // Parsing time and setting time view
        String timeString = task_time.get(position);
        Date timeDate = null;
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeDate = timeFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Setting time view
        if (timeDate != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            holder.time.setText(timeFormat.format(timeDate));
        }

        // Setting other views
        holder.title.setText(String.valueOf(task_title.get(position)));
        holder.description.setText(String.valueOf(task_description.get(position)));
        holder.status.setText(String.valueOf(task_status.get(position)));

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return task_id != null ? task_id.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, day, date, month, time, status;
        ImageButton options;  // Add this line

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            day = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            options = itemView.findViewById(R.id.options);  // Add this line
        }
    }

    // Method to show the congratulations popup
    private void showCongratulationsPopup(View anchorView) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_congrats, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set up the popup window
        Button btnThanks = popupView.findViewById(R.id.btnThanks);
        btnThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // Show the popup window using the anchorView parameter
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }
}
