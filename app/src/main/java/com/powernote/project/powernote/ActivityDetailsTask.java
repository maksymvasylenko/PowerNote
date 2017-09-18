package com.powernote.project.powernote;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Maks on 14.09.2017.
 */

public class ActivityDetailsTask extends AppCompatActivity {

    private PowerNotes pwn = PowerNotes.getInstance();
    private int value;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_details);



        Fragment fragmentTaskEdit = new FragmentTaskEdit();
        Fragment fragmentTaskView = new FragmentTaskView();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        value = intent.getIntExtra("action", -1);
        Log.e("taskDatabaseID", "task" + value);



        if(value != -1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_activity_task_details_fragment_container, fragmentTaskView)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_activity_task_details_fragment_container, fragmentTaskEdit)
                    .commit();
        }

    }
}
