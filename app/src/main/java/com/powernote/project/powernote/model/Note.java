package com.powernote.project.powernote.model;

/**
 * Created by Cage on 9/4/17.
 */

public class Note {
    private long id;
    private String text, name, createdAt;

    public Note(long id, String text, String createdAt, String name){
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.name = name;
    }

    public Note(String text, String createdAt, String name){
        this.text = text;
        this.createdAt = createdAt;
        this.name = name;
    }

    public Note(String text, String createdAt){
        this.text = text;
        this.createdAt = createdAt;
        this.name = null;
    }

    public long getId() {
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

    public void setId(long id) {
        this.id = id;
    }
}
