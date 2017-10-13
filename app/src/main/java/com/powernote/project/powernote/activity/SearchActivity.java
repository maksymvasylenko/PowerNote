package com.powernote.project.powernote.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.fragment.ColorListFragment;
import com.powernote.project.powernote.fragment.TagListFragment;
import com.powernote.project.powernote.fragment.TaskListFragment;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

/**
 * Created by Maks on 13.10.2017.
 */

public class SearchActivity extends AppCompatActivity implements Methods.OnPresortSelectedListener, TaskAddedCallback {

    private ScrollView presortScrollView;
    private FrameLayout taskListFragmentContainer;
    private FragmentManager fragmentManager;
    private Fragment fragmentTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_search_actvity_action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //View actionBarView = getSupportActionBar().getCustomView();


        taskListFragmentContainer = (FrameLayout) findViewById(R.id.fl_activity_search_tasks_fragment_container);
        presortScrollView = (ScrollView) findViewById(R.id.sv_activity_search_presort);
        presortScrollView.setVisibility(View.VISIBLE);


        fragmentManager = getSupportFragmentManager();

        Fragment fragmentTagList = new TagListFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_activity_search_tags_fragment_container, fragmentTagList).commit();

        Fragment fragmentColorList = new ColorListFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_activity_search_colors_fragment_container, fragmentColorList).commit();


        fragmentTasks = new TaskListFragment();

    }

    @Override
    public void onColorSelected(int color) {

        String noteFilter = DBOpenHelper.KEY_BACKGROUNDCOLOR + "=" + color;//change 25 to String[].length-1

        showTasks(null, noteFilter);
    }

    @Override
    public void onTagSelected(long tagId) {

        //get all tasks with this tag (tagid in column task_tag)
        //convert tasks id into String[] - this will be selectionArgs => stringList

        String[] stringListOfIds = null;
        String noteFilter = null;

        String selection = DBOpenHelper.KEY_TASKS_TAGS_TAG_ID + "=" + tagId;

        Cursor cursor = getContentResolver().query(PowerNoteProvider.CONTENT_URI_TASKS_TAGS,
                DBOpenHelper.TASKS_TAGS_ALL_COLUMNS, selection, null, null);

        if(cursor.moveToFirst()){

            stringListOfIds = new String[cursor.getCount()];

            stringListOfIds[0] = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TASK_ID)));
            Log.e("test0", "" + stringListOfIds[0]);
            for (int i = 1;cursor.moveToNext(); i++) {
                Log.e("test" + i, "" + DBOpenHelper.KEY_TASKS_TAGS_TASK_ID);
                stringListOfIds[i] = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TASK_ID)));
            }

            noteFilter = DBOpenHelper.KEY_ID + " IN (" + new String(new char[stringListOfIds.length-1]).replace("\0", "?,") + "?)";
        }

        if(stringListOfIds == null){
            Log.e("test", "stringListOfIds == null");
        }
        if(noteFilter == null){
            Log.e("test", "noteFilter == null");
        }


        // TODO: 13.10.2017 fix this: if both null all tasks are shown
        showTasks(stringListOfIds, noteFilter);
    }


    private void showTasks(@Nullable String[] stringListOfIds, String noteFilter){
        presortScrollView.setVisibility(View.GONE);
        taskListFragmentContainer.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString(PowerNoteProvider.CONTENT_SELECTION, noteFilter );
        if(stringListOfIds != null){
            Log.e("stringListOfIds", "stringListOfIds != null");
            bundle.putStringArray(PowerNoteProvider.CONTENT_SELECTION_ARGS, stringListOfIds );
        }

        fragmentTasks.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fl_activity_search_tasks_fragment_container, fragmentTasks).commit();
    }
    
    @Override
    public void taskAdded() {

    }
}
