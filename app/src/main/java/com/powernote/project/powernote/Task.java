package com.powernote.project.powernote;

/**
 * Created by Cage on 9/4/17.
 */

public class Task {

    private int id, taskRank;
    private String taskName, taskDescription, taskDeadline, createdAt;
    private double taskDuration;

    public Task(int id, int taskRank, String taskName, String taskDescription, String taskDeadline, String createdAt, double taskDuration){
        this.id = id;
        this.taskRank = taskRank;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.createdAt = createdAt;
        this.taskDuration = taskDuration;
    }

    public Task(int id, int taskRank, String taskDescription, String taskDeadline, String createdAt, double taskDuration){
        this.id = id;
        this.taskRank = taskRank;
        this.taskDescription = taskDescription;
        this.taskDeadline = taskDeadline;
        this.createdAt = createdAt;
        this.taskDuration = taskDuration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public double getTaskDuration() {
        return taskDuration;
    }

    public int getTaskRank() {
        return taskRank;
    }

    public String getTaskDeadline() {
        return taskDeadline;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskDuration(double taskDuration) {
        this.taskDuration = taskDuration;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskRank(int taskRank) {
        this.taskRank = taskRank;
    }

}


