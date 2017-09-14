package com.powernote.project.powernote.model;

import com.powernote.project.powernote.ListItem;

import java.util.List;

public class Task {

    private int rank, effort;
    private long id;
    private String name, description, deadline, createdAt;
    private double duration;
    private boolean complete;
    private int priority;
    private List<ListItem> checkList;
    private List<Task> dependencies;

    //    private float rank;
    //    private Calendar deadline;
    //    private Repetition repetition;
    //    private List<Reminder> reminders;
    //    private Effort effort;
    //    private Duration duration;

    public Task(long id, int rank, String name, String description, String deadline, String createdAt, double duration, int effort){
        this.id = id;
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.effort = effort;
    }

    public Task(int rank, String name, String description, String deadline, String createdAt, double duration, int effort){
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.effort = effort;
    }

    public Task(int rank, String description, String deadline, String createdAt, double duration, int effort){
        this.rank = rank;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.name = null;
        this.effort = effort;
    }

    public String getCreatedAt() {
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

    public String getDeadline() {
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

    public void setDeadline(String taskDeadline) {
        this.deadline = taskDeadline;
    }

    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public void setDuration(double taskDuration) {
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
}


