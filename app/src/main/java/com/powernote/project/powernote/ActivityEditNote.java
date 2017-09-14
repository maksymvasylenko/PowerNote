package com.powernote.project.powernote;

import android.content.Intent;
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

import com.powernote.project.powernote.model.Note;

/**
 * Created by Maks on 11.09.2017.
 */

public class ActivityEditNote extends AppCompatActivity{

    private PowerNotes pwn = PowerNotes.getInstance();
    private long value;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        final EditText title = (EditText) findViewById(R.id.et_note_edit_title);
        final EditText text = (EditText) findViewById(R.id.et_note_edit_text);
        Button saveButton = (Button) findViewById(R.id.bt_note_edit_save);

        //todo : fix : instead of updating its just ssves new note (either make different activities or set new label and onClick for the button)



        Intent intent = getIntent();
        value = intent.getLongExtra("noteDatabaseID", -1);
        Log.e("noteDatabaseID", "note" + value);

        //todo : fix TimeStamp and add this note to array of notes in PowerNotes
        if(value != -1) {
            //onUpdate
            final Note note = pwn.getNote(value);
            title.setText(note.getName());
            text.setText(note.getText());
            saveButton.setText("Update");

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                note.setName(title.getText().toString());
                note.setText(text.getText().toString());


                pwn.addNote(note);
                Snackbar.make(v, "Note Updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                finish();
                }
            });
        }else{
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Note newNote = new Note(text.getText().toString(),
                        "timeStamp",
                        title.getText().toString());

                pwn.addNote(newNote);
                Snackbar.make(v, "Note Created", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                pwn.getNotes().remove(value);
                PowerNotes.getInstance().getDB().deleteNote(value);
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
