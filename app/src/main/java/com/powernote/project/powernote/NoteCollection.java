package com.powernote.project.powernote;

import java.util.ArrayList;

/**
 * Created by Maks on 25.07.2017.
 */

public class NoteCollection {
    private static NoteCollection instance;
    private ArrayList<Note> notes;

    private NoteCollection() {
        notes = new ArrayList<>();
        addDummyData();
    }

    private void addDummyData() {
        addBook("The Hitchhiker's Guide to the Galaxy");
        addBook("Android App Development for Dummies");
        addBook("Deception Point");
        addBook("I always get my sin");
        addBook("The Color of Magic");
    }

    public static NoteCollection getInstance() {
        if (instance == null) {
            instance = new NoteCollection();
        }
        return instance;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void addBook(Note book) {
        notes.add(book);
    }

    public void addBook(String text) {
        Note book = new Note(text);
        addBook(book);
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

}
