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
	private static final String BASE_PATH_TAGS = "tags";
	private static final String BASE_PATH_TASKS_TAGS = "tasks_tags";
	private static final String BASE_PATH_NOTES_TAGS = "notes_tags";
	public static final Uri CONTENT_URI_TASKS = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH_TASKS );
	public static final Uri CONTENT_URI_NOTES = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH_NOTES );
	public static final Uri CONTENT_URI_TAGS = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH_TAGS );
	public static final Uri CONTENT_URI_TASKS_TAGS = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH_TASKS_TAGS );
	public static final Uri CONTENT_URI_NOTES_TAGS = Uri.parse( "content://" + AUTHORITY + "/" + BASE_PATH_NOTES_TAGS );
	
	// Constant to identify the requested operation
	private static final int TASK = 1;
	private static final int TASKS_ID = 2;
	private static final int NOTE = 3;
	private static final int NOTES_ID = 4;

	private static final int TAG = 5;
	private static final int TAGS_ID = 6;
	private static final int NOTES_TAGS = 7;
	private static final int NOTES_TAGS_ID = 8;
	private static final int TASKS_TAGS = 9;
	private static final int TASKS_TAGS_ID = 10;
	private static final int NOTES_TAGS_TAG_ID = 11;
	private static final int NOTES_TAGS_NOTE_ID = 12;
	private static final int TASKS_TAGS_TAG_ID = 13;
	private static final int TASKS_TAGS_TASK_ID = 14;


	private static final UriMatcher uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
	
	public static final String CONTENT_ITEM_TYPE = "Task";

	public static final String CONTENT_SELECTION = "selection";
	public static final String CONTENT_SELECTION_ARGS = "selectionArgs";
	
	static {
		uriMatcher.addURI( AUTHORITY, BASE_PATH_TASKS, TASK );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_TASKS + "/#", TASKS_ID );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_NOTES, NOTE );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_NOTES + "/#", NOTES_ID );

		uriMatcher.addURI( AUTHORITY, BASE_PATH_TAGS, TAG );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_TAGS + "/#", TAGS_ID );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_NOTES_TAGS, NOTES_TAGS );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_NOTES_TAGS + "/#", NOTES_TAGS_ID );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_TASKS_TAGS, TASKS_TAGS );
		uriMatcher.addURI( AUTHORITY, BASE_PATH_TASKS_TAGS + "/#", TASKS_TAGS_ID );
	}

	
	private SQLiteDatabase database;
	
	@Override
	public boolean onCreate() {
		
		DBOpenHelper helper = new DBOpenHelper( getContext() );
		database = helper.getWritableDatabase();
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		switch (uriMatcher.match( uri )) {
			case TASKS_ID:
				Log.e( "query", "tasks_id" );
				selection = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
			case TASK:
				Log.e( "query", "tasks" );
				return database.query( DBOpenHelper.TABLE_TASKS, DBOpenHelper.TASK_ALL_COLUMNS, selection, selectionArgs, null, null, null );
			case NOTES_ID:
				Log.e( "query", "notes_id" );
				selection = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
			case NOTE:
				Log.e( "query", "notes" );
				return database.query( DBOpenHelper.TABLE_NOTES, DBOpenHelper.NOTE_ALL_COLUMNS, selection, null, null, null, null );

			case TAGS_ID:
				Log.e( "query", "tags_id" );
				selection = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
			case TAG:
				Log.e( "query", "tags" );
				return database.query( DBOpenHelper.TABLE_TAGS, DBOpenHelper.TAG_ALL_COLUMNS, selection, null, null, null, null );

			/*case NOTES_TAGS_TAG_ID:
				// TODO: 13.10.2017 fix this!!! it doesnt work
				selection = DBOpenHelper.KEY_NOTES_TAGS_TAG_ID + "=" + uri.getLastPathSegment();
			case NOTES_TAGS_NOTE_ID:
				// TODO: 13.10.2017 fix this!!! it doesnt work
				selection = DBOpenHelper.KEY_NOTES_TAGS_NOTE_ID + "=" + uri.getLastPathSegment();*/
			case NOTES_TAGS:
				return database.query( DBOpenHelper.TABLE_NOTES_TAGS, DBOpenHelper.NOTES_TAGS_ALL_COLUMNS, selection, null, null, null, null );
			/*case TASKS_TAGS_TAG_ID:
				// TODO: 13.10.2017 fix this!!! it doesnt work
				selection = DBOpenHelper.KEY_TASKS_TAGS_TAG_ID + "=" + uri.getLastPathSegment();
			case TASKS_TAGS_TASK_ID:
				// TODO: 13.10.2017 fix this!!! it doesnt work
				selection = DBOpenHelper.KEY_TASKS_TAGS_TASK_ID + "=" + uri.getLastPathSegment();*/
			case TASKS_TAGS:

				return database.query( DBOpenHelper.TABLE_TASKS_TAGS, DBOpenHelper.TASKS_TAGS_ALL_COLUMNS, selection, selectionArgs, null, null, null );
			default:
				throw new SQLException( "Failed to insert row into " + uri );
		}
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (uriMatcher.match( uri )) {
			case TASK:
				long taskId = database.insert( DBOpenHelper.TABLE_TASKS, null, values );
				return Uri.parse( BASE_PATH_TASKS + "/" + taskId );
			case NOTE:
				Log.e( "note insert", "insertion" );
				long noteId = database.insert( DBOpenHelper.TABLE_NOTES, null, values );
				return Uri.parse( BASE_PATH_NOTES + "/" + noteId );

			case TAG:
				long tagId = database.insert( DBOpenHelper.TABLE_TAGS, null, values );
				Log.e( "tag insert", "insertion id:" + tagId );
				return Uri.parse( BASE_PATH_NOTES + "/" + tagId );
			case NOTES_TAGS:
				Log.e( "note tag insert", "insertion" );
				long notesTagsId = database.insert( DBOpenHelper.TABLE_NOTES_TAGS, null, values );
				return Uri.parse( BASE_PATH_NOTES + "/" + notesTagsId );
			case TASKS_TAGS:
				Log.e( "task tag insert", "insertion" );
				long tasksTagsId = database.insert( DBOpenHelper.TABLE_TASKS_TAGS, null, values );
				return Uri.parse( BASE_PATH_NOTES + "/" + tasksTagsId );

			default:
				throw new SQLException( "Failed to insert row into " + uri );
		}
	}
	
	
	//can be redone with selection inside the method(like query method)
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		switch (uriMatcher.match( uri )) {
			case TASK:
				return database.delete( DBOpenHelper.TABLE_TASKS, selection, selectionArgs );
			case NOTE:
				return database.delete( DBOpenHelper.TABLE_NOTES, selection, selectionArgs );

			case TAG:
				return database.delete( DBOpenHelper.TABLE_TAGS, selection, selectionArgs );
			case NOTES_TAGS:
				return database.delete( DBOpenHelper.TABLE_NOTES_TAGS, selection, selectionArgs );
			case TASKS_TAGS:
				return database.delete( DBOpenHelper.TABLE_TASKS_TAGS, selection, selectionArgs );

			default:
				throw new SQLException( "Failed to delete row " + uri );
		}

        /*database.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(taskId) });*/
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		switch (uriMatcher.match( uri )) {
			case TASK:
				Log.e( "powerNoteProvider", "task update" );
				return database.update( DBOpenHelper.TABLE_TASKS, values, selection, selectionArgs );
			case NOTE:
				Log.e( "powerNoteProvider", "note update" );
				return database.update( DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs );

			case TAG:
				Log.e( "powerNoteProvider", "tag update" );
				return database.update( DBOpenHelper.TABLE_TAGS, values, selection, selectionArgs );
			case NOTES_TAGS:
				Log.e( "powerNoteProvider", "note_tag update" );
				return database.update( DBOpenHelper.TABLE_NOTES_TAGS, values, selection, selectionArgs );
			case TASKS_TAGS:
				Log.e( "powerNoteProvider", "task_tag update" );
				return database.update( DBOpenHelper.TABLE_TASKS_TAGS, values, selection, selectionArgs );

			default:
				throw new SQLException( "Failed to update row " + uri );
		}

        /*return database.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(updatedTask.getId()) });*/
	}
}
