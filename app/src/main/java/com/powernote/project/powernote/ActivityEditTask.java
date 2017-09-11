package com.powernote.project.powernote;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

/**
 * Created by Maks on 11.09.2017.
 */

public class ActivityEditTask  extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);


        final EditText title = (EditText) findViewById(R.id.et_task_edit_title);
        final EditText text = (EditText) findViewById(R.id.et_task_edit_description);
        final SeekBar effort = (SeekBar) findViewById(R.id.sb_task_edit_effort);
        final SeekBar priority = (SeekBar) findViewById(R.id.sb_task_edit_priority);
        Button date = (Button) findViewById(R.id.bt_task_edit_date);
        Button time = (Button) findViewById(R.id.bt_task_edit_time);
        Button saveButton = (Button) findViewById(R.id.bt_task_edit_save);


        /*Intent intent = getIntent();
        Long value = intent.getLongExtra("noteDatabaseID", 0);
        Log.e("noteDatabaseID", "note" + value);

        Note note = PowerNotes.getInstance().getDB().getNote(value);
        title.setText(note.getName());
        text.setText(note.getText());*/

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Task newTask = new Task(priority.getProgress(),
                        title.getText().toString(),
                        text.getText().toString(),
                        "deadline",
                        "timeStamp",
                        8.0
                );

                //effort.getProgress();
                PowerNotes.getInstance().getDB().createTask(newTask);
                Snackbar.make(v, "Task Created", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




    }
}
