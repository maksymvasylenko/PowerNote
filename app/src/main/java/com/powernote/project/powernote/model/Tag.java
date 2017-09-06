package com.powernote.project.powernote.model;

/**
 * Created by Cage on 9/4/17.
 */

public class Tag {

    private int id;
    private String name, createdAt;

    public Tag(int id, String name, String createdAt){
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;

    }

    public int getId() {
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


}
