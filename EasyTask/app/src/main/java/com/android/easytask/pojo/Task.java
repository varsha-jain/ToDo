package com.android.easytask.pojo;

/**
 * Created by varsha on 8/1/2017.
 */

public class Task {
    private int taskID;
    private String taskName;
    private String description;
    private String priority;
    private String date;
    private String status;

    public Task(){

    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TASK NAME: " + taskName + "\nDUE: " + date + "\nPRIORITY:  " + priority;
    }
}
