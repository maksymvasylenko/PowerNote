package com.powernote.project.powernote;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Tag;
import com.powernote.project.powernote.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private static final int EDITOR_REQUEST_CODE = 1001;
    private ListAdapter listAdapter;

    // Database Helper
    DBOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBOpenHelper(getApplicationContext());

        /*Note note1 = new Note("note text", getDateTime());
        Note note2 = new Note("note with name", getDateTime(), "note name");
        Note note3 = new Note("huuuuuuuuuge text ssss", getDateTime());
        Note note4 = new Note("note last", getDateTime());*/


        /*List<Note> notes = db.getAllNotes();
        for (int i = 0; i < notes.size(); i++) {
            Log.e(DBOpenHelper.LOG, "note " + i + ":" + notes.get(i).getName());
        }*/



        listAdapter = new ListAdapter(this);

        List<Task> tasks = db.getAllTasks();
        if(tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                Log.e(DBOpenHelper.LOG, "task " + i + ":" + tasks.get(i).getName());
                listAdapter.addTaskItem(tasks.get(i));
            }
        }

        List<Note> notes = db.getAllNotes();
        if(notes != null) {
            for (int i = 1; i < notes.size(); i++) {
                Log.e(DBOpenHelper.LOG, "note " + i + ":" + notes.get(i).getName());
                listAdapter.addNoteItem(notes.get(i));
            }
        }



        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(listAdapter);


        // Creating tasks
        /*Task task1 = new Task(5,"iPhone 5S", "10.04.2005", "10.03.2005", 0);*/


        db.closeDB();

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

    }*/

    /*private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted note " + noteUri.getLastPathSegment());
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*private void deleteAllNotes() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(
                                    NotesProvider.CONTENT_URI, null, null
                            );
                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }*/

    /*private void insertSampleData() {
        insertNote("Simple note");
        insertNote("Multi-line\nnote");
        insertNote("Very long note with a lot of text that exceeds the width of the screen");
        restartLoader();
    }*/

    /*private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }*/

    public void openEditorForNewNote(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            restartLoader();
        }
    }*/
}
