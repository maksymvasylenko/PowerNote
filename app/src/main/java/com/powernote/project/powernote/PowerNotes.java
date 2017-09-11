package com.powernote.project.powernote;

import android.content.Context;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Tag;
import com.powernote.project.powernote.model.Task;

import java.util.List;

/**
 * Created by Maks on 09.09.2017.
 */

public class PowerNotes {

    private DBOpenHelper db;
    private List<Note> notes;
    private List<Task> tasks;
    private List<Tag> tags;

    private static final PowerNotes ourInstance = new PowerNotes();

    public static PowerNotes getInstance() {
        return ourInstance;
    }

    private PowerNotes() {

    }

    public void initializeDB(Context applicationContext){
        db = new DBOpenHelper(applicationContext);
    }

    public DBOpenHelper getDB(){
        return db;
    }

    public void closeDB(){
        db.closeDB();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Tag> getTag() {
        return tags;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void addNote(Note note){
        notes.add(note);
    }

}
