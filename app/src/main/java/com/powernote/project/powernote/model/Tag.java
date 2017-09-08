package com.powernote.project.powernote.model;

/**
 * Created by Cage on 9/4/17.
 */

public class Tag {

    private long id;
    private String name, createdAt;

    public Tag(long id, String name, String createdAt){
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
    }

    public Tag(String name, String createdAt){
        this.createdAt = createdAt;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setName(String tagName) {
        this.name = tagName;
    }

    public void setId(long id) {
        this.id = id;
    }
}
