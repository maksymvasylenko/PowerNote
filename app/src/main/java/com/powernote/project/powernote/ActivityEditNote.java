package com.powernote.project.powernote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.powernote.project.powernote.model.Note;

/**
 * Created by Maks on 11.09.2017.
 */

public class ActivityEditNote extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        final EditText title = (EditText) findViewById(R.id.et_note_edit_title);
        final EditText text = (EditText) findViewById(R.id.et_note_edit_text);
        Button saveButton = (Button) findViewById(R.id.bt_note_edit_save);

        //todo : fix : instead of updating its just ssves new note (either make different activities or set new label and onClick for the button)



        Intent intent = getIntent();
        Long value = intent.getLongExtra("noteDatabaseID", 0);
        Log.e("noteDatabaseID", "note" + value);

        Note note = PowerNotes.getInstance().getDB().getNote(value);
        title.setText(note.getName());
        text.setText(note.getText());

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //todo : fix TimeStamp and add this note to array of notes in PowerNotes
                Note newNote = new Note(text.getText().toString(),
                        "timeStamp",
                        title.getText().toString());


                PowerNotes.getInstance().getDB().createNote(newNote);
                Snackbar.make(v, "Note Created", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

}
