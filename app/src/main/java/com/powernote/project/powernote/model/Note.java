package com.powernote.project.powernote.model;

/**
 * Created by Cage on 9/4/17.
 */

public class Note {
    private int id;
    private String text, name, createdAt;

    public Note(int id, String text, String createdAt, String name){
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.name = name;
    }

    public Note(int id, String noteText, String createdAt){
        this.id = id;
        this.text = noteText;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }
}
