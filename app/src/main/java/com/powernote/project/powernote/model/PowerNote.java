package com.powernote.project.powernote.model;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Maks on 09.09.2017.
 */

public class PowerNotes {

    private DBOpenHelper db;
    private HashMap<Long, Tag> tags;
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Note> notes;

    private static final PowerNotes ourInstance = new PowerNotes();

    public static PowerNotes getInstance() {
        return ourInstance;
    }

    private PowerNotes() {
        tags = null;
        tasks = null;
        notes = null;
    }

    public void initializeDB(Context applicationContext){
        db = new DBOpenHelper(applicationContext);
        notes = db.getAllNotes();
        tasks = db.getAllTasks();
        tags = db.getAllTags();
    }

    public DBOpenHelper getDB(){
        return db;
    }

    public void closeDB(){
        db.closeDB();
    }

    public HashMap<Long, Note> getNotes() {
        return notes;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, Tag> getTag() {
        return tags;
    }

    public void addTask(Task task){
        tasks.put(task.getId(),task);
        db.createTask(task);
    }

    public void addNote(Note note){
        notes.put(note.getId(),note);
        db.createNote(note);
    }

    public void addTag(Tag tag){
        tags.put(tag.getId(),tag);
        db.createTag(tag);
    }

    public void deleteNote(long key){
        notes.remove(key);
        db.deleteNote(key);
    }

    public void deleteTask(long key){
        tasks.remove(key);
        db.deleteTask(key);
    }

    public void deleteTag(long key){
        tags.remove(key);
        db.deleteTag(key);
    }

    public Task getTask(long key){
        return this.tasks.get(key);
    }

    public Note getNote(long key){
        return this.notes.get(key);
    }

    public Tag getTag(long key) {
        return this.tags.get(key);
    }

    public void setNotes(HashMap<Long, Note> notes) {
        this.notes = notes;
    }

    public void setTags(HashMap<Long, Tag> tags) {
        this.tags = tags;
    }

    public void setTasks(HashMap<Long, Task> tasks) {
        this.tasks = tasks;
    }

}
