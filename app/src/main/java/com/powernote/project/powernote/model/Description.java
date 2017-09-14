package com.powernote.project.powernote.model;

public class Description {
    private String title;
    private String details;

    public Description(String title) {
        this.title = title;
    }

    public Description(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
