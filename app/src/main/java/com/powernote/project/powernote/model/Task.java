package com.powernote.project.powernote.model;

import java.util.List;

public class Task extends Note{

    private int rank, effort;
    private long deadline;
    private long duration;
    private boolean complete;
    private int priority;
    private List<Task> dependencies;

    public Task(long id, int rank, String title, String description, long deadline,
                long createdAt, long duration, int effort, String imagePath, List<ChecklistItem> checklist){
        super(id, description, createdAt, title, checklist);
        this.rank = rank;
        this.deadline = deadline;
        this.duration = duration;
        this.effort = effort;
    }

    public Task(){}

    public double getDuration() {
        return duration;
    }

    public int getRank() {
        return rank;
    }

    public long getDeadline() {
        return deadline;
    }

    public int getEffort() {
        return effort;
    }

    public void setDeadline(long taskDeadline) {
        this.deadline = taskDeadline;
    }

    public void setDuration(long taskDuration) {
        this.duration = taskDuration;
    }

    public void setRank(int taskRank) {
        this.rank = taskRank;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }
}


