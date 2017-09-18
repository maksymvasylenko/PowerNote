package com.powernote.project.powernote.model;

import java.util.Calendar;
import java.util.List;

public class Task {

    private int rank, effort;
    private long id;
    private long deadline;
    private String name;
    private String description;
    private long createdAt;
    private String imagePath = null;
    private long duration;
    private boolean complete;
    private int priority;
    private List<ListItem> checkList;
    private List<Task> dependencies;

    public Task(long id, int rank, String name, String description, long deadline, long createdAt, long duration, int effort, String imagePath){
        this.id = id;
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.effort = effort;
        this.imagePath = imagePath;
    }

    public Task(){}

    public long getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public double getDuration() {
        return duration;
    }

    public int getRank() {
        return rank;
    }

    public long getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getEffort() {
        return effort;
    }

    public void setDeadline(long taskDeadline) {
        this.deadline = taskDeadline;
    }

    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public void setDuration(long taskDuration) {
        this.duration = taskDuration;
    }

    public void setName(String taskName) {
        this.name = taskName;
    }

    public void setRank(int taskRank) {
        this.rank = taskRank;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}


