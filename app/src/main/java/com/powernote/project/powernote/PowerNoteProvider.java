package com.powernote.project.powernote;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.powernote.project.powernote.model.DBOpenHelper;


/**
 * Created by Maks on 20.09.2017.
 */

public class PowerNoteProvider extends ContentProvider {

    private static final String AUTHORITY = "com.powernote.project.powernote.powernoteprovider";
    private static final String BASE_PATH_TASKS = "tasks";
    private static final String BASE_PATH_NOTES = "notes";
    public static final Uri CONTENT_URI_TASKS =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TASKS );
    public static final Uri CONTENT_URI_NOTES =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_NOTES );

    // Constant to identify the requested operation
    private static final int TASK = 1;
    private static final int TASKS_ID = 2;
    private static final int NOTE = 3;
    private static final int NOTES_ID = 4;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Task";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TASKS, TASK);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_TASKS +  "/#", TASKS_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_NOTES, NOTE);
        uriMatcher.addURI(AUTHORITY, BASE_PATH_NOTES +  "/#", NOTES_ID);
    }


    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch(uriMatcher.match(uri)){
            case TASKS_ID:
                Log.e("tasks_id", "tasks_id");
                selection = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
            case TASK:
                Log.e("tasks_id", "tasks_id");
                return database.query(DBOpenHelper.TABLE_TASKS, DBOpenHelper.TASK_ALL_COLUMNS,
                        selection, null, null, null, null);
            case NOTES_ID:
                Log.e("notes_id", "notes_id");
                selection = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
            case NOTE:
                Log.e("notes", "notes");
                return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.NOTE_ALL_COLUMNS,
                        selection, null, null, null, null);
            default: throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch(uriMatcher.match(uri)){
            case TASK:
                long taskId = database.insert(DBOpenHelper.TABLE_TASKS, null, values);
                return Uri.parse(BASE_PATH_TASKS + "/" + taskId);
            case NOTE:
                Log.e("note insert", "insertion");
                long noteId = database.insert(DBOpenHelper.TABLE_NOTES, null, values);
                return Uri.parse(BASE_PATH_NOTES + "/" + noteId);
            default: throw new SQLException("Failed to insert row into " + uri);
        }
    }


//can be redone with selection inside the method(like query method)
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch(uriMatcher.match(uri)){
            case TASK:
                return database.delete(DBOpenHelper.TABLE_TASKS, selection, selectionArgs);
            case NOTE:
                return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
            default: throw new SQLException("Failed to delete row " + uri);
        }

        /*database.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(taskId) });*/
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch(uriMatcher.match(uri)){
            case TASK:
                Log.e("powerNoteProvider", "task update");
                return database.update(DBOpenHelper.TABLE_TASKS,
                        values, selection, selectionArgs);
            case NOTE:
                Log.e("powerNoteProvider", "note update");
                return database.update(DBOpenHelper.TABLE_NOTES,
                        values, selection, selectionArgs);
            default: throw new SQLException("Failed to update row " + uri);
        }

        /*return database.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(updatedTask.getId()) });*/
    }
}
