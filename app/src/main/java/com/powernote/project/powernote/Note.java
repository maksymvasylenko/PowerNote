package com.powernote.project.powernote;

/**
 * Created by Cage on 9/4/17.
 */

public class Note {
    private int id;
    private String noteText, noteName, createdAt;

    public Note(int id, String noteText, String createdAt, String noteName){
        this.id = id;
        this.noteText = noteText;
        this.createdAt = createdAt;
        this.noteName = noteName;
    }

    public Note(int id, String noteText, String createdAt){
        this.id = id;
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNoteName() {
        return noteName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
