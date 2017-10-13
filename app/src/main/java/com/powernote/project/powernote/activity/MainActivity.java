package com.powernote.project.powernote.activity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.powernote.project.powernote.fragment.NoteFragment;
import com.powernote.project.powernote.fragment.TaskListFragment;
import com.powernote.project.powernote.R;
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
