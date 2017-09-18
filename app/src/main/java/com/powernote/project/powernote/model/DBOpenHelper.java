package com.powernote.project.powernote.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DBOpenHelper extends SQLiteOpenHelper {

    //logcat tag
    public static final String LOG = "DatabaseHelper";

    //Constants for db name and version
    private static final String DATABASE_NAME = "powerNote.db";
    private static final int DATABASE_VERSION = 1;

    //Table names
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_TAGS = "tags";
    private static final String TABLE_TASKS_TAGS = "tasks_tags";
    private static final String TABLE_NOTES_TAGS = "notes_tas";

    //Column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //tasks table column names
    private static final String KEY_TASK_NAME = "task_name";
    private static final String KEY_TASK_DESCRIPTION = "task_description";
    private static final String KEY_TASK_DEADLINE = "task_deadline";
    private static final String KEY_TASK_RANK = "task_rank";
    private static final String KEY_TASK_DURATION = "task_duration";
    private static final String KEY_TASK_EFFORT = "task_effort";
    private static final String KEY_TASK_IMAGE_PATH = "task_image_path";
    private static final String KEY_TASK_CHECKLIST = "task_checklist";

    //notes table column names
    private static final String KEY_NOTE_TEXT = "note_text";
    private static final String KEY_NOTE_NAME = "note_name";
    private static final String KEY_NOTE_CHECKLIST = "note_checklist";

    //tags table column names
    private static final String KEY_TAG_NAME = "tag_name";

    //notes_tag table column names
    private static final String KEY_NOTESTAGS_NOTE_ID = "note_id";
    private static final String KEY_NOTESTAGS_TAG_ID = "tag_id";

    //tasks_tag table column names
    private static final String KEY_TASKSTAGS_TASK_ID = "task_id";
    private static final String KEY_TASKSTAGS_TAG_ID = "tag_id";

    //create table statements
    //Tasks table
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TASK_NAME + " TEXT," + KEY_TASK_DESCRIPTION + " TEXT,"
            + KEY_TASK_DEADLINE + " INTEGER," + KEY_TASK_RANK + " INTEGER,"
            + KEY_TASK_DURATION + " INTEGER," + KEY_CREATED_AT
            + " INTEGER," + KEY_TASK_EFFORT + " INTEGER," + KEY_TASK_IMAGE_PATH + " TEXT,"
            + KEY_TASK_CHECKLIST + " TEXT" + ")";

    //Notes table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NOTE_NAME + " TEXT," + KEY_NOTE_TEXT + " TEXT,"
            + KEY_CREATED_AT + " INTEGER," + KEY_NOTE_CHECKLIST + " TEXT" + ")";

    //Tags table
    private static final String CREATE_TABLE_TAGS = "CREATE TABLE "
            + TABLE_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TAG_NAME + " TEXT," + KEY_CREATED_AT
            + " INTEGER" + ")";

    //Tasks_tags table
    private static final String CREATE_TABLE_TASKS_TAGS = "CREATE TABLE "
            + TABLE_TASKS_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TASKSTAGS_TASK_ID + " INTEGER," + KEY_TASKSTAGS_TAG_ID + " INTEGER"
            + ")";

    //Notes_tags table
    private static final String CREATE_TABLE_NOTES_TAGS = "CREATE TABLE "
            + TABLE_NOTES_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NOTESTAGS_NOTE_ID + " INTEGER," + KEY_NOTESTAGS_TAG_ID + " INTEGER"
            + ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_TAGS);
        db.execSQL(CREATE_TABLE_TASKS_TAGS);
        db.execSQL(CREATE_TABLE_NOTES_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_TAGS);

        onCreate(db);
    }

    //CRUD operations
    //task crud
    public long createTask(Task newTask){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, newTask.getTitle());
        values.put(KEY_TASK_DESCRIPTION, newTask.getDescription());
        values.put(KEY_TASK_DEADLINE, newTask.getDeadline());
        values.put(KEY_TASK_RANK, newTask.getRank());
        values.put(KEY_TASK_DURATION, newTask.getDuration());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_TASK_EFFORT, newTask.getEffort());
        values.put(KEY_TASK_IMAGE_PATH, newTask.getImagePath());
        values.put(KEY_TASK_CHECKLIST, serialize(newTask.getCheckList()));

        //fix adding tag(s)

        long taskId = db.insert(TABLE_TASKS, null, values);//add id to the task object

        newTask.setId(taskId);
        return taskId;
    }

    public Task getTask(long taskId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
                + KEY_ID + " = " + taskId;

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c != null){
            return getNewTask(c);
        }else{
            return null;
        }
    }

    public Task getNewTask(Cursor c){


        Task task = new Task(c.getInt(c.getColumnIndex(KEY_ID)),
                c.getInt(c.getColumnIndex(KEY_TASK_RANK)),
                c.getString(c.getColumnIndex(KEY_TASK_NAME)),
                c.getString(c.getColumnIndex(KEY_TASK_DESCRIPTION)),
                c.getLong(c.getColumnIndex(KEY_TASK_DEADLINE)),
                c.getLong(c.getColumnIndex(KEY_CREATED_AT)),
                c.getLong(c.getColumnIndex(KEY_TASK_DURATION)),
                c.getInt(c.getColumnIndex(KEY_TASK_EFFORT)),
                c.getString(c.getColumnIndex(KEY_TASK_IMAGE_PATH)),
                derialize(c.getString(c.getColumnIndex(KEY_TASK_CHECKLIST)))
        );
        return task;
    }

    public HashMap<Long, Task>  getAllTasks(){
        HashMap<Long, Task>  tasks = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = getNewTask(c);
                tasks.put(task.getId(), task);
            } while (c.moveToNext());

            return tasks;
        }else{
            return null;
        }
    }

    public HashMap<Long, Task> getAllTasksByTag(String tagName){
        HashMap<Long, Task> tasks = new HashMap<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " td, "
                + TABLE_TAGS + " tg, " + TABLE_TASKS_TAGS + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tagName + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TASKSTAGS_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TASKSTAGS_TASK_ID;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = getNewTask(c);
                tasks.put(task.getId(), task);
            } while (c.moveToNext());
            return tasks;
        }else{
            return null;
        }
    }

    public int updateTask(Task updatedTask){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TASK_NAME, updatedTask.getTitle());
        values.put(KEY_TASK_DESCRIPTION, updatedTask.getDescription());
        values.put(KEY_TASK_DEADLINE, updatedTask.getDeadline());
        values.put(KEY_TASK_RANK, updatedTask.getRank());
        values.put(KEY_TASK_DURATION, updatedTask.getDuration());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_TASK_EFFORT, updatedTask.getEffort());
        values.put(KEY_TASK_IMAGE_PATH, updatedTask.getImagePath());

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(updatedTask.getId()) });
    }

    public void deleteTask(long taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(taskId) });
    }

    //note crud
    public long createNote(Note newNote){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_NAME, newNote.getTitle());
        values.put(KEY_NOTE_TEXT, newNote.getDescription());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_NOTE_CHECKLIST, serialize(newNote.getCheckList()));

        //fix adding tag(s)

        long taskId = db.insert(TABLE_NOTES, null, values);//add id to the note object

        return taskId;
    }

    private Note getNewNote(Cursor c){
        Note note = new Note(c.getInt(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NOTE_TEXT)),
                c.getLong(c.getColumnIndex(KEY_CREATED_AT)),
                c.getString(c.getColumnIndex(KEY_NOTE_NAME)),
                derialize(c.getString(c.getColumnIndex(KEY_NOTE_CHECKLIST)))
        );
        return note;
    }

    public Note getNote(long noteId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " WHERE "
                + KEY_ID + " = " + noteId;

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        if(c != null){
            return getNewNote(c);
        }else{
            return null;
        }
    }

    public HashMap<Long, Note> getAllNotes(){
        HashMap<Long, Note> notes = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Note note = getNewNote(c);
                notes.put(note.getId(), note);
            } while (c.moveToNext());

            return notes;
        }else{
            return null;
        }
    }

    public HashMap<Long, Note> getAllNotesByTag(String tagName){
        HashMap<Long, Note> notes = new HashMap<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " td, "
                + TABLE_TAGS + " tg, " + TABLE_NOTES_TAGS + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tagName + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_NOTESTAGS_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_NOTESTAGS_NOTE_ID;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Note note = getNewNote(c);
                notes.put(note.getId(), note);
            } while (c.moveToNext());

            return notes;
        }else{
            return null;
        }
    }

    public int updateNote(Note updatedNote){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_NAME, updatedNote.getTitle());
        values.put(KEY_NOTE_TEXT, updatedNote.getDescription());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_NOTES, values, KEY_ID + " = " + String.valueOf(updatedNote.getId()),
                null);
    }

    public void deleteNote(long noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[] { String.valueOf(noteId) });
    }

    //tag crud
    public long createTag(Tag newTag){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, newTag.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        //fix adding tag(s)

        long taskId = db.insert(TABLE_TAGS, null, values);//add id to the note object

        return taskId;
    }

    public Tag getTag(long tagId){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TAGS + " WHERE "
                + KEY_ID + " = " + tagId;

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null){
            Tag tag = new Tag(c.getInt(c.getColumnIndex(KEY_ID)),
                    c.getString(c.getColumnIndex(KEY_TAG_NAME)),
                    c.getString(c.getColumnIndex(KEY_CREATED_AT))
            );

            return tag;
        }else{
            return null;
        }
    }

    public HashMap<Long, Tag> getAllTags(){
        HashMap<Long, Tag> tags = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAGS;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tag tag = new Tag(c.getInt(c.getColumnIndex(KEY_ID)),
                        c.getString(c.getColumnIndex(KEY_TAG_NAME)),
                        c.getString(c.getColumnIndex(KEY_CREATED_AT))
                );

                tags.put(tag.getId(), tag);
            } while (c.moveToNext());

            return tags;
        }else{
            return null;
        }
    }

    public int updateTag(Tag updatedTag){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOTE_NAME, updatedTag.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_TAGS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(updatedTag.getId()) });
    }

    public void deleteTag(long tagId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGS, KEY_ID + " = ?",
                new String[] { String.valueOf(tagId) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    private long getDateTime() {
        return System.currentTimeMillis();
    }






    String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";
    String ARRAY_DIVIDER_SECOND = "#d2isdi9dcvra2r2ra5y";

    public String serialize(List<ListItem> items){

        List<String> content = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            String a = items.get(i).getText() + ARRAY_DIVIDER_SECOND + String.valueOf(items.get(i).isChecked());
            content.add(a);
        }
        return TextUtils.join(ARRAY_DIVIDER, content);
    }

    public List<ListItem> derialize(String content){

        String[] c = content.split(ARRAY_DIVIDER);
        List<ListItem> items = new ArrayList<>();

        for (int i = 0; i < c.length ; i++) {
            String[] stringItem = c[i].split(ARRAY_DIVIDER_SECOND);
            items.add(new ListItem(stringItem[0], Boolean.valueOf(stringItem[1])));
        }



        return items;
    }



}
