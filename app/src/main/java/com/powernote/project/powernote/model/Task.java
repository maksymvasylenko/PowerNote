package com.powernote.project.powernote.model;

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

    public double getDuration() {
        return taskDuration;
    }

    public int getRank() {
        return taskRank;
    }

    public String getDeadline() {
        return taskDeadline;
    }

    public String getDescription() {
        return taskDescription;
    }

    public String getName() {
        return taskName;
    }

    public void setDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setDuration(double taskDuration) {
        this.taskDuration = taskDuration;
    }

    public void setName(String taskName) {
        this.taskName = taskName;
    }

    public void setRank(int taskRank) {
        this.taskRank = taskRank;
    }

}


