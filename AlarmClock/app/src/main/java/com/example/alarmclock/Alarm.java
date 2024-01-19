package com.example.alarmclock;

public class Alarm {
    private int id;
    private String time;
    private String label;
    private boolean isEnabled;

    // Constructor
    public Alarm(int id, String time, String label, boolean isEnabled) {
        this.id = id;
        this.time = time;
        this.label = label;
        this.isEnabled = isEnabled;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
