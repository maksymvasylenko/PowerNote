package com.powernote.project.powernote;

import android.content.Context;
import android.util.Log;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Tag;
import com.powernote.project.powernote.model.Task;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Maks on 09.09.2017.
 */

public class PowerNotes {

    private DBOpenHelper db;
    private HashMap<Long, Tag> tags;
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Note> notes;
    private long currentSelectedItem = -1;

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

        Log.e("id while adding to DB", "" + id);


        tasks.put(id,task);
    }

    public void addNote(Note note){
        db.createNote(note);
        notes.put(note.getId(),note);
    }

    public void addTag(Tag tag){
        db.createTag(tag);
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
