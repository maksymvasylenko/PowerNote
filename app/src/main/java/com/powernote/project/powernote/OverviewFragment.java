package com.powernote.project.powernote;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

import java.util.List;


public class OverviewFragment extends Fragment {

    ListView list;
    ListAdapter listAdapter;
    PowerNotes pwn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pwn = PowerNotes.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // todo Check if callback listener is implemented in parent activity
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);


        listAdapter = new ListAdapter(getContext());

        List<Task> tasks = pwn.getDB().getAllTasks();
        if(tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                Log.e(DBOpenHelper.LOG, "task " + i + ":" + tasks.get(i).getName());
                listAdapter.addTaskItem(tasks.get(i));
            }
        }

        List<Note>notes = pwn.getDB().getAllNotes();
        if(notes != null) {
            for (int i = 1; i < notes.size(); i++) {
                Log.e(DBOpenHelper.LOG, "note " + i + ":" + notes.get(i).getName());
                listAdapter.addNoteItem(notes.get(i));
            }
        }



        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(listAdapter);

        pwn.closeDB();

        return view;

    }
}
