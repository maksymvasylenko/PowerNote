package com.powernote.project.powernote.model;

/**
 * Created by Cage on 9/4/17.
 */

public class Task {

    private int rank;
    private long id;
    private String name, description, deadline, createdAt;
    private double duration;

    public Task(long id, int rank, String name, String description, String deadline, String createdAt, double duration){
        this.id = id;
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
    }

    public Task(int rank, String name, String description, String deadline, String createdAt, double duration){
        this.rank = rank;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
    }

    public Task(int rank, String description, String deadline, String createdAt, double duration){
        this.rank = rank;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.duration = duration;
        this.name = null;
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
}


