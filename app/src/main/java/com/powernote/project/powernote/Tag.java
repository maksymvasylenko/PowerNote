package com.powernote.project.powernote;

/**
 * Created by Cage on 9/4/17.
 */

public class Tag {

    private int id;
    private String tagName, createdAt;

    public Tag(int id, String tagName, String createdAt){
        this.id = id;
        this.createdAt = createdAt;
        this.tagName = tagName;

    }

    public int getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


}
