package com.powernote.project.powernote.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Cage on 9/4/17.
 */

public class Note {
    private long id;
    private String description, title;
    private Calendar createdAt  = Calendar.getInstance();
    private List<ChecklistItem> checkList = new ArrayList<>();
    private String imagePath = null;
    private int backgroundColor = -1;

    public Note(long id, String description, long createdAt, String title,List<ChecklistItem> checklist, String imagePath, int backgroundColor){
        this.id = id;
        this.description = description;
        this.createdAt.setTimeInMillis(createdAt);
        this.title = title;
        this.checkList = checklist;
        this.imagePath = imagePath;
        this.backgroundColor = backgroundColor;
    }

    public Note(){
        this.id = -1;
        this.description = null;
        this.title = null;
        this.backgroundColor = -263685;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public List<ChecklistItem> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<ChecklistItem> checkList) {
        this.checkList = checkList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt.setTimeInMillis(createdAt);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
