package com.powernote.project.powernote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.powernote.project.powernote.fragment.FragmentTaskEdit;
import com.powernote.project.powernote.fragment.FragmentTaskView;

public class ActivityDetailsTask extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Define task edit and view fragments
        Fragment fragmentTaskEdit = new FragmentTaskEdit();
        Fragment fragmentTaskView = new FragmentTaskView();

        // Get the fragment manager to allow for switching fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        // Get the intent from the parent activity
        Intent intent = getIntent();



        long id = intent.getLongExtra(PowerNoteProvider.CONTENT_ITEM_TYPE, -1);

        if (id == -1) {

            fragmentManager.beginTransaction()
                    .replace(R.id.fl_activity_task_details_fragment_container, fragmentTaskEdit)
                    .commit();
        } else {

            // Create a bundle and pass in the taskId
            Bundle bundle = new Bundle();
            bundle.putLong(PowerNoteProvider.CONTENT_ITEM_TYPE, id);

            // Pass the bundle into the fragment
            fragmentTaskView.setArguments(bundle);

            // (hack) set the current selected task in the singleton model
            /*
             What we should do is pass the current selected item back and fourth between activities/fragments
             */


            // Display the fragment in the fragment container
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_activity_task_details_fragment_container, fragmentTaskView)
                    .commit();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
