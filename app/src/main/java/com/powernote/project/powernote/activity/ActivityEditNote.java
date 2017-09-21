package com.powernote.project.powernote.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Note;

public class ActivityEditNote extends AppCompatActivity{

    private String noteFilter;
    private Note note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        final EditText title = (EditText) findViewById(R.id.et_note_edit_title);
        final EditText text = (EditText) findViewById(R.id.et_note_edit_text);
        Button saveButton = (Button) findViewById(R.id.bt_note_edit_save);

        //todo : fix : instead of updating its just ssves new note (either make different activities or set new label and onClick for the button)

        Intent intent = getIntent();
        long id = intent.getLongExtra(PowerNoteProvider.CONTENT_ITEM_TYPE, -1);

        //todo : fix TimeStamp and add this note to array of notes in PowerNotes
        if(id == -1) {

            note = new Note();
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: 18.09.2017  fix checklist
                /*Note newNote = new Note(0,text.getText().toString(),
                        1,
                        title.getText().toString());

                pwn.addNote(newNote);
                Snackbar.make(v, "Note Created", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                    note.setTitle(title.getText().toString());
                    note.setDescription(text.getText().toString());

                    Log.e("finish activity", "insert");
                    getContentResolver().insert(PowerNoteProvider.CONTENT_URI_NOTES,
                            Methods.getNoteValues(note));

                    setResult(RESULT_OK);
                    finish();
                }
            });

        }else{

            Uri uri = Uri.parse(PowerNoteProvider.CONTENT_URI_NOTES + "/" + id);


            Log.e("activityeditnote", "id:"+id);
            noteFilter = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.NOTE_ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();

            note = Methods.getNewNote(cursor);


            title.setText(note.getTitle());
            text.setText(note.getDescription());
            saveButton.setText("Update");
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note.setTitle(title.getText().toString());
                    note.setDescription(text.getText().toString());
                    //update note

                    getContentResolver().update(PowerNoteProvider.CONTENT_URI_NOTES,
                            Methods.getNoteValues(note),noteFilter, null);

                    Snackbar.make(v, "Note Updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                getContentResolver().delete(PowerNoteProvider.CONTENT_URI_NOTES,
                        noteFilter, null);

                setResult(RESULT_OK);
                finish();
                break;
            case R.id.action_take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
                break;
            case R.id.action_add_image:
                break;
            case R.id.action_record:
                break;
            case R.id.action_add_checklist:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
