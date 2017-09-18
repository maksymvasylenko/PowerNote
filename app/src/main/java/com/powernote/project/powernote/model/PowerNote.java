package com.powernote.project.powernote.model;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

public class PowerNote {

    private DBOpenHelper db;
    private HashMap<Long, Tag> tags;
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Note> notes;
    private long currentSelectedItem = -1;

    private static final PowerNote ourInstance = new PowerNote();

    public static PowerNote getInstance() {
        return ourInstance;
    }

    private PowerNote() {
        tags = null;
        tasks = null;
        notes = null;
    }

    public void initializeDB(Context applicationContext){
        db = new DBOpenHelper(applicationContext);
        notes = db.getAllNotes();
        tasks = db.getAllTasks();
        tags = db.getAllTags();

        if(notes == null){
            notes = new HashMap<>();
        }
        if(tasks == null){
            tasks = new HashMap<>();
        }
        if(tags == null){
            tags = new HashMap<>();
        }
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
        long id = db.createTask(task);
        tasks.put(id,task);
    }

    public void addNote(Note note){
        long id = db.createNote(note);
        notes.put(id,note);
    }

    public void addTag(Tag tag){
        long id = db.createTag(tag);
        tags.put(id,tag);
    }

    public void updateTask(Task task){
        db.updateTask(task);
        tasks.put(task.getId(),task);
    }

    public void updateNote(Note note){
        db.updateNote(note);
        notes.put(note.getId(),note);
    }

    public void updateTag(Tag tag){
        db.updateTag(tag);
        tags.put(tag.getId(),tag);
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

    public long getCurrentSelectedItem() {
        return currentSelectedItem;
    }

    public void setCurrentSelectedItem(long currentSelectedItem) {
        this.currentSelectedItem = currentSelectedItem;
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
