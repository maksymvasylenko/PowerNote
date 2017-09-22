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
import android.view.View;

import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.powernote.project.powernote.fragment.FragmentTaskEdit;
import com.powernote.project.powernote.fragment.NotesListFragment;
import com.powernote.project.powernote.fragment.OverviewFragment;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.TaskAddedCallback;

public class MainActivity extends AppCompatActivity implements TaskAddedCallback{


    private static final int EDITOR_REQUEST_CODE = 1001;
    private static final int NOTE_EDITOR_REQUEST_CODE = 1010;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Fragment overFragment, notesFragment;

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

        // Init Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Create");
                builder.setMessage("What do you want to create?");
                builder.setNegativeButton("Note",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent myIntent = new Intent(MainActivity.this, EditNoteActivity.class);
                                //myIntent.putExtra("action", -1);
                                notesFragment.startActivityForResult(myIntent, NOTE_EDITOR_REQUEST_CODE);
                            }
                        });
                builder.setPositiveButton("Task",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent myIntent = new Intent(MainActivity.this, TaskActivity.class);
                                //myIntent.putExtra("action", -1);
                                overFragment.startActivityForResult(myIntent, EDITOR_REQUEST_CODE);
                            }
                        });
                builder.setNeutralButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Toast.makeText(getApplicationContext(),"Cancel is clicked",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();
            }
        });
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tasks";
                case 1:
                    return "Overview";
                case 2:
                    return "Notes";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new FragmentTaskEdit();
                case 1:
                    overFragment = new OverviewFragment();
                    return overFragment;
                case 2:
                    notesFragment = new NotesListFragment();
                    return notesFragment;
                default:
                    return null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
