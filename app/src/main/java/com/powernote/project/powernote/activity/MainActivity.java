package com.powernote.project.powernote.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;
import android.view.View;

import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.fragment.NoteFragment;
import com.powernote.project.powernote.fragment.TaskListFragment;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

public class MainActivity extends AppCompatActivity implements TaskAddedCallback{
    
    private static final int EDITOR_REQUEST_CODE = 1001;
    private static final int NOTE_EDITOR_REQUEST_CODE = 1010;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Fragment taskListFragment, notesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init PagerAdapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        Cursor cursor = getContentResolver().query(PowerNoteProvider.CONTENT_URI_TASKS_TAGS,
                DBOpenHelper.TASKS_TAGS_ALL_COLUMNS, null, null, null);
        Log.e("task_tag table", ": ");
        if(cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
            long tagId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TAG_ID));
            long taskId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TASK_ID));
            Log.e("task_tag table", ":0: id:" + id + "; tagId:" + tagId + "; taskId:" + taskId);

            int i = 1;
            while(cursor.moveToNext()){
                id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
                tagId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TAG_ID));
                taskId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_TASKS_TAGS_TASK_ID));
                Log.e("task_tag table", ":" + i + ": id:" + id + "; tagId:" + tagId + "; taskId:" + taskId);
                i++;
            }
        }


        cursor = getContentResolver().query(PowerNoteProvider.CONTENT_URI_TAGS,
                DBOpenHelper.TAG_ALL_COLUMNS, null, null, null);
        Log.d("tag table", ": ");
        if(cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
            String tagName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TAG_NAME));
            Log.d("tag table", ":0: id:" + id + "; tagName:" + tagName);

            int i = 1;
            while(cursor.moveToNext()){
                id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
                tagName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TAG_NAME));
                Log.d("tag table", ":" + i + ": id:" + id + "; tagName:" + tagName);
                i++;
            }
        }


        cursor = getContentResolver().query(PowerNoteProvider.CONTENT_URI_TASKS,
                DBOpenHelper.TASK_ALL_COLUMNS, null, null, null);
        Log.i("task table", ": ");
        if(cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
            String taskName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_NAME));
            Log.i("task table", ":0: id:" + id + "; taskName:" + taskName);

            int i = 1;
            while(cursor.moveToNext()){
                id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
                taskName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_NAME));
                Log.i("task table", ":" + i + ": id:" + id + "; taskName:" + taskName);
                i++;
            }
        }





        /*ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_TAG_NAME, "test1");
        getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values);
        ContentValues values2 = new ContentValues();
        values2.put(DBOpenHelper.KEY_TAG_NAME, "test2");
        getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values2);*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add){
            int pageNumber = mViewPager.getCurrentItem();
            if (pageNumber == 0){
                Intent myIntent = new Intent(MainActivity.this, TaskActivity.class);
                taskListFragment.startActivityForResult(myIntent, EDITOR_REQUEST_CODE);
            } else if (pageNumber == 1){
                Intent myIntent = new Intent(MainActivity.this, NoteActivity.class);
                notesFragment.startActivityForResult(myIntent, NOTE_EDITOR_REQUEST_CODE);
            }
        } else if(item.getItemId() == R.id.action_search){
            Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(myIntent, EDITOR_REQUEST_CODE);
        }
        return super.onOptionsItemSelected( item );
    }
    
    @Override
    public void taskAdded() {
        Log.d("asdf", "asdf");
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tasks";
                case 1:
                    return "Notes";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    taskListFragment = new TaskListFragment();
                    return taskListFragment;
                case 1:
                    notesFragment = new NoteFragment();
                    return notesFragment;
                default:
                    return null;
            }
        }
    }
}
