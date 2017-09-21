package com.powernote.project.powernote.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static final String TABLE_NOTES_TAGS = "notes_tas";

    //Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_CREATED_AT = "created_at";

    //tasks table column names
    public static final String KEY_TASK_NAME = "task_name";
    public static final String KEY_TASK_DESCRIPTION = "task_description";
    public static final String KEY_TASK_DEADLINE = "task_deadline";
    public static final String KEY_TASK_RANK = "task_rank";
    public static final String KEY_TASK_DURATION = "task_duration";
    public static final String KEY_TASK_EFFORT = "task_effort";
    public static final String KEY_TASK_IMAGE_PATH = "task_image_path";
    public static final String KEY_TASK_CHECKLIST = "task_checklist";

    public static final String[] TASK_ALL_COLUMNS = {KEY_ID,KEY_CREATED_AT,
            KEY_TASK_NAME,KEY_TASK_DESCRIPTION,KEY_TASK_DEADLINE,KEY_TASK_RANK,KEY_TASK_DURATION,
            KEY_TASK_EFFORT,KEY_TASK_IMAGE_PATH,KEY_TASK_CHECKLIST};


    //notes table column names
    public static final String KEY_NOTE_TEXT = "note_text";
    public static final String KEY_NOTE_NAME = "note_name";
    public static final String KEY_NOTE_CHECKLIST = "note_checklist";

    public static final String[] NOTE_ALL_COLUMNS = {KEY_ID,KEY_CREATED_AT,
            KEY_NOTE_TEXT,KEY_NOTE_NAME,KEY_NOTE_CHECKLIST};


    //tags table column names
    public static final String KEY_TAG_NAME = "tag_name";

    //notes_tag table column names
    public static final String KEY_NOTESTAGS_NOTE_ID = "note_id";
    public static final String KEY_NOTESTAGS_TAG_ID = "tag_id";

    //tasks_tag table column names
    public static final String KEY_TASKSTAGS_TASK_ID = "task_id";
    public static final String KEY_TASKSTAGS_TAG_ID = "tag_id";

    //
    public static String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";
    public static String ARRAY_DIVIDER_SECOND = "#d2isdi9dcvra2r2ra5y";

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
}