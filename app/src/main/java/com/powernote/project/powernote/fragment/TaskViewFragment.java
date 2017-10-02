package com.powernote.project.powernote.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Task;

import com.powernote.project.powernote.adapter.ChecklistViewAdapter;
import com.powernote.project.powernote.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TaskViewFragment extends Fragment {
	private ChecklistViewAdapter adapter;
	private ListView lvCheckist;
	
	private LinearLayout layoutChecklist;
	private LinearLayout layoutDeadline;
	private LinearLayout layoutEffort;
    private LinearLayout layoutImage;
	
	private TextView tvTime;
	private TextView tvDate;

	private Task task;

	private String noteFilter;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu( menu, inflater );
		// Inflate the options menu to show the available options
		inflater.inflate( R.menu.menu_task_view, menu );
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Press delete
			case R.id.action_delete:

				getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_TASKS,
						noteFilter, null);

				getActivity().setResult(RESULT_OK);
				getActivity().finish();
				break;
			// Press edit
			case R.id.action_edit:
				// Define a new TaskEditFragment and pass in the bundle from the parent activity
				Fragment fragmentTaskEdit = new FragmentTaskEdit();
				fragmentTaskEdit.setArguments(getArguments());
				
				// Replace this fragment with an EditTaskFragment
				getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fl_activity_task_details_fragment_container, fragmentTaskEdit )
						// Add the fragment to the backStack to be popped later by the parent activity
						.addToBackStack( null ).commit();
				break;
			default:
				return super.onOptionsItemSelected( item );
		}
		return super.onOptionsItemSelected( item );
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.task_view, container, false );
		
		lvCheckist = (ListView) view.findViewById( R.id.lv_checklist_view );
		
		final TextView title = (TextView) view.findViewById( R.id.tv_task_view_title );
		final TextView description = (TextView) view.findViewById( R.id.tv_task_view_description );
		final ProgressBar effort = (ProgressBar) view.findViewById( R.id.pb_effort );
		final ProgressBar priority = (ProgressBar) view.findViewById( R.id.pb_priority );
		
		tvDate = (TextView) view.findViewById( R.id.tv_task_view_deadline_date );
		tvTime = (TextView) view.findViewById( R.id.tv_task_view_deadline_time );
		
		layoutEffort = (LinearLayout) view.findViewById( R.id.ll_task_view_effort_priority );
		layoutDeadline = (LinearLayout) view.findViewById( R.id.ll_task_view_deadline );
		layoutChecklist = (LinearLayout) view.findViewById( R.id.layout_checklist );
        layoutImage = (LinearLayout) view.findViewById( R.id.layout_images );


        ImageView imageView = (ImageView) view.findViewById( R.id.image );
		
		if(getArguments() != null) {
			// Get the taskId that was passed in via the bundle and set the current task


			long id = getArguments().getLong(PowerNoteProvider.CONTENT_ITEM_TYPE);
			Uri uri = Uri.parse(PowerNoteProvider.CONTENT_URI_TASKS + "/" + id);



			noteFilter = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();

			Cursor cursor = getActivity().getContentResolver().query(uri,
					DBOpenHelper.TASK_ALL_COLUMNS, noteFilter, null, null);
			cursor.moveToFirst();

			task = Methods.getNewTask(cursor);

			// If the task contains a checklist
			if(task.getCheckList() != null) {
				layoutChecklist.setVisibility( View.VISIBLE );
				final List items = task.getCheckList();

				adapter = new ChecklistViewAdapter( getContext(), R.layout.checklist_item_alt, items );
				lvCheckist.setAdapter( adapter );
			}

			// Fill views with their respective data from the task
			if(task.getTitle() != null) {
				title.setText( task.getTitle() );
			}
			if(task.getDescription() != null) {
				description.setText( task.getDescription() );
			}

			if(task.getEffort() != -1) {
				layoutEffort.setVisibility( View.VISIBLE );
				Log.e("View Task", " effort:" + task.getEffort());
				Log.e("View Task", " progress:" + task.getRank());
				effort.setProgress( task.getEffort() );
				priority.setProgress( task.getRank());
			}

			if(task.getDeadline() != -1) {
				layoutDeadline.setVisibility( View.VISIBLE );

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis( task.getDeadline() );

				int mYear = calendar.get( Calendar.YEAR );
				int mMonth = calendar.get( Calendar.MONTH );
				int mDay = calendar.get( Calendar.DAY_OF_MONTH );
				tvDate.setText( mDay + "-" + (mMonth + 1) + "-" + mYear );

				int hour = calendar.get( Calendar.HOUR_OF_DAY );
				int min = calendar.get( Calendar.MINUTE );
				tvTime.setText( hour + ":" + min );
			}

			if(task.getImagePath() != null && !task.getImagePath().isEmpty() ){
                layoutImage.setVisibility( View.VISIBLE );


				Uri imageUri = Uri.parse(task.getImagePath());
				imageView.setImageURI(imageUri);

				//imageView.setImageURI(Uri.parse("com.powernote.project.powernote.fileprovider" + task.getImagePath()));
            }

		}

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().finish();
	}
}
