package com.powernote.project.powernote.model;

import java.util.List;

public class Task extends Note{

    private int rank, effort;
    private long id, deadline;
    private String name, description, createdAt, imagePath = null;
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

    public Task(long id, int rank, String name, String description, long deadline, String createdAt, double duration, int effort, String imagePath){
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

    public Task(){
        this.name = null;
        this.description = null;
        this.createdAt = null;
    }


    public Task(int rank, String name, String description, long deadline, String createdAt, double duration, int effort){
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.effort = effort;
    }

    public Task(int rank, String description, long deadline, String createdAt, double duration, int effort){
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}


