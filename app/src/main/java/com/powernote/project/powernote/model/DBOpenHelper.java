package com.powernote.project.powernote.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    //logcat tag
    public static final String LOG = "DatabaseHelper";

    //Constants for db name and version
    private static final String DATABASE_NAME = "powerNote.db";
    private static final int DATABASE_VERSION = 1;

    //Table names
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_TAGS = "tags";
    public static final String TABLE_TASKS_TAGS = "tasks_tags";
    public static final String TABLE_NOTES_TAGS = "notes_tags";

    //Common column names
    public static final String KEY_ID = "_id";
    public static final String KEY_CREATED_AT = "created_at";



    //Commomn column names for tasks and notes column
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CHECKLIST = "checklist";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_BACKGROUNDCOLOR = "backgroundcolor";

    //tasks table column names
    public static final String KEY_TASK_DEADLINE = "task_deadline";
    public static final String KEY_TASK_RANK = "task_rank";
    public static final String KEY_TASK_DURATION = "task_duration";
    public static final String KEY_TASK_SPEND = "task_spend";
    public static final String KEY_TASK_EFFORT = "task_effort";

    public static final String[] TASK_ALL_COLUMNS = {KEY_ID,KEY_CREATED_AT,
            KEY_NAME,KEY_DESCRIPTION,KEY_TASK_DEADLINE,KEY_TASK_RANK,KEY_TASK_DURATION,
            KEY_TASK_EFFORT,KEY_IMAGE_PATH,KEY_BACKGROUNDCOLOR,KEY_CHECKLIST,KEY_TASK_SPEND};


    //notes table column names
    //...

    public static final String[] NOTE_ALL_COLUMNS = {KEY_ID,KEY_CREATED_AT,
            KEY_DESCRIPTION,KEY_NAME,KEY_CHECKLIST,KEY_IMAGE_PATH,KEY_BACKGROUNDCOLOR};


    //tags table column names
    public static final String KEY_TAG_NAME = "tag_name";

    public static final String[] TAG_ALL_COLUMNS = {KEY_ID,KEY_CREATED_AT,KEY_TAG_NAME};


    //notes_tag table column names
    public static final String KEY_NOTES_TAGS_NOTE_ID = "note_id";
    public static final String KEY_NOTES_TAGS_TAG_ID = "tag_id";

    public static final String[] NOTES_TAGS_ALL_COLUMNS = {KEY_ID,KEY_NOTES_TAGS_NOTE_ID,KEY_NOTES_TAGS_TAG_ID};


    //tasks_tag table column names
    public static final String KEY_TASKS_TAGS_TASK_ID = "task_id";
    public static final String KEY_TASKS_TAGS_TAG_ID = "tag_id";

    public static final String[] TASKS_TAGS_ALL_COLUMNS = {KEY_ID, KEY_TASKS_TAGS_TASK_ID, KEY_TASKS_TAGS_TAG_ID};


    //create table statements
    //Tasks table
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_TASK_DEADLINE + " INTEGER," + KEY_TASK_RANK + " INTEGER,"
            + KEY_TASK_DURATION + " INTEGER," + KEY_CREATED_AT
            + " INTEGER," + KEY_TASK_EFFORT + " INTEGER," + KEY_IMAGE_PATH + " TEXT,"
            + KEY_CHECKLIST + " TEXT," + KEY_BACKGROUNDCOLOR + " INTEGER," + KEY_TASK_SPEND + " INTEGER" + ")";

    //Notes table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE "
            + TABLE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_CREATED_AT + " INTEGER," + KEY_CHECKLIST + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT," + KEY_BACKGROUNDCOLOR + " INTEGER" + ")";

    //Tags table
    private static final String CREATE_TABLE_TAGS = "CREATE TABLE "
            + TABLE_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TAG_NAME + " TEXT," + KEY_CREATED_AT
            + " INTEGER" + ")";

    //Tasks_tags table
    private static final String CREATE_TABLE_TASKS_TAGS = "CREATE TABLE "
            + TABLE_TASKS_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TASKS_TAGS_TASK_ID + " INTEGER," + KEY_TASKS_TAGS_TAG_ID + " INTEGER"
            + ")";

    //Notes_tags table
    private static final String CREATE_TABLE_NOTES_TAGS = "CREATE TABLE "
            + TABLE_NOTES_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NOTES_TAGS_NOTE_ID + " INTEGER," + KEY_NOTES_TAGS_TAG_ID + " INTEGER"
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
}