package com.powernote.project.powernote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.design.widget.TabLayout;
import android.widget.Toast;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int EDITOR_REQUEST_CODE = 1001;
    private ListAdapter listAdapter;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private PowerNotes pwn = PowerNotes.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pwn.initializeDB(getApplicationContext());

        //test for data. already done this.
        /*Task task = new Task(10, "task Name", "task descriptiooooon", "02.05.08", "01.04.07", 10.4);
        Task task2 = new Task(10, "task descriptiooooon2", "04.07.10", "03.06.09", 10.4);
        Note note = new Note("body of the note", "10.04.1996", "note Name");
        Note note2 = new Note("body of the note2", "10.01.1885");

        pwn.getDB().createTask(task);
        pwn.getDB().createTask(task2);
        pwn.getDB().createNote(note);
        pwn.getDB().createNote(note2);*/

        /*for (int i = 0; i < 10; i++) {
            Task task = new Task(10, "task Name" + i, "task descriptiooooon" + i, "02.05.08", "01.04.07", 10.4);
            pwn.getDB().createTask(task);
        }

        for (int i = 0; i < 10; i++) {
            Note note = new Note("body of the note" + i, "10.04.1996_" + i, "note Name" + i);
            pwn.getDB().createNote(note);
        }*/

        /*Note note = pwn.getDB().getNote(8);
        Log.e("db", "note " + 112 + " id: " + note.getId());
        Log.e("db", "note " + 112 + " name: " + note.getName());
        Log.e("db", "note " + 112 + " text: " + note.getText());
        Log.e("db", "note " + 112 + " createdAt: " + note.getCreatedAt());


        List<Note> notes = pwn.getDB().getAllNotes();
        Log.e("db", "notes size:" + notes.size());
        for (int i = 0; i < notes.size(); i++) {
            Log.e("db", "note " + i + " id: " + notes.get(i).getId());
            Log.e("db", "note " + i + " name: " + notes.get(i).getName());
            Log.e("db", "note " + i + " text: " + notes.get(i).getText());
            Log.e("db", "note " + i + " createdAt: " + notes.get(i).getCreatedAt());
        }*/

        /*Task task = new Task(10,"task 1010", "task descriptiooooon2", "04.07.10", "03.06.09", 10.4);
        Log.e("db", "task " + 1010 + " id: " + task.getId());
        Log.e("db", "task " + 1010 + " name: " + task.getName());
        Log.e("db", "task " + 1010 + " description: " + task.getDescription());
        Log.e("db", "task " + 1010 + " createdAt: " + task.getCreatedAt());
        Log.e("db", "task " + 1010 + " deadline: " + task.getDeadline());
        Log.e("db", "task " + 1010 + " duration: " + task.getDuration());
        Log.e("db", "task " + 1010 + " rank: " + task.getRank());

        pwn.getDB().createTask(task);

        Log.e("db", "task " + 1010 + " id: " + task.getId());
        Log.e("db", "task " + 1010 + " name: " + task.getName());
        Log.e("db", "task " + 1010 + " description: " + task.getDescription());
        Log.e("db", "task " + 1010 + " createdAt: " + task.getCreatedAt());
        Log.e("db", "task " + 1010 + " deadline: " + task.getDeadline());
        Log.e("db", "task " + 1010 + " duration: " + task.getDuration());
        Log.e("db", "task " + 1010 + " rank: " + task.getRank());*/



        List<Task> tasks = pwn.getDB().getAllTasks();
        Log.e("db", "tasks:" + pwn.getDB().getAllTasks().size());
        for (int i = 0; i < tasks.size(); i++) {
            Log.e("db", "task " + i + " id: " + tasks.get(i).getId());
            Log.e("db", "task " + i + " name: " + tasks.get(i).getName());
            Log.e("db", "task " + i + " description: " + tasks.get(i).getDescription());
            Log.e("db", "task " + i + " createdAt: " + tasks.get(i).getCreatedAt());
            Log.e("db", "task " + i + " deadline: " + tasks.get(i).getDeadline());
            Log.e("db", "task " + i + " duration: " + tasks.get(i).getDuration());
            Log.e("db", "task " + i + " rank: " + tasks.get(i).getRank());
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle("Create");
                builder.setMessage("What do you want to create?");
                builder.setNegativeButton("Note",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent myIntent = new Intent(MainActivity.this, ActivityEditNote.class);
                                startActivity(myIntent);
                            }
                        });
                builder.setPositiveButton("Task",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent myIntent = new Intent(MainActivity.this, ActivityEditTask.class);
                                startActivity(myIntent);
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


                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 1:
                    return new OverviewFragment();
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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


    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBOpenHelper(getApplicationContext());

        listAdapter = new ListAdapter(this);

        List<Task> tasks = db.getAllTasks();
        if(tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                Log.e(DBOpenHelper.LOG, "task " + i + ":" + tasks.get(i).getName());
                listAdapter.addTaskItem(tasks.get(i));
            }
        }

        List<Note> notes = db.getAllNotes();
        if(notes != null) {
            for (int i = 1; i < notes.size(); i++) {
                Log.e(DBOpenHelper.LOG, "note " + i + ":" + notes.get(i).getName());
                listAdapter.addNoteItem(notes.get(i));
            }
        }



        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(listAdapter);

        db.closeDB();

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }*/
}
