package com.powernote.project.powernote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.fragment.FragmentTaskEdit;
import com.powernote.project.powernote.fragment.FragmentTaskView;

/**
 * TaskActivity allows for the viewing and editing of tasks
 */
public class TaskActivity extends AppCompatActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_task_details );
		
		// Define task edit and view fragments
		Fragment fragmentTaskEdit = new FragmentTaskEdit();
		Fragment fragmentTaskView = new FragmentTaskView();
		
		// Get the fragment manager to allow for switching fragments
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		
		// Get the intent from the parent activity
		Intent intent = getIntent();
		
		// Get the type of item being passed in from the intent
		long id = intent.getLongExtra( PowerNoteProvider.CONTENT_ITEM_TYPE, -1 );
		
		// If the type being passes in is not of type Task
		if(id == -1) {
			fragmentManager.beginTransaction().replace( R.id.fl_activity_task_details_fragment_container, fragmentTaskEdit ).commit();
		} else {
			
			// Create a bundle and pass in the taskId
			Bundle bundle = new Bundle();
			bundle.putLong( PowerNoteProvider.CONTENT_ITEM_TYPE, id );
			
			// Pass the bundle into the fragment
			fragmentTaskView.setArguments( bundle );
			
			// Display the fragment in the fragment container
			fragmentManager.beginTransaction().replace( R.id.fl_activity_task_details_fragment_container, fragmentTaskView ).commit();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
