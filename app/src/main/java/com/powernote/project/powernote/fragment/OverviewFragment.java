package com.powernote.project.powernote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

import java.util.HashMap;
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


        if(pwn.getTasks() != null) {

            for (HashMap.Entry<Long, Task> entry : pwn.getTasks().entrySet())
            {
                listAdapter.addTaskItem(entry.getValue());
            }
        }


        if(pwn.getNotes() != null) {

            for (HashMap.Entry<Long, Note> entry : pwn.getNotes().entrySet())
            {
                listAdapter.addNoteItem(entry.getValue());
            }
        }

        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listAdapter.getItemViewType(position) == 1){
                    Intent myIntent = new Intent(getActivity(), ActivityEditNote.class);
                    Note note = (Note) listAdapter.getItem(position);
                    myIntent.putExtra("noteDatabaseID", note.getId());
                    startActivity(myIntent);

                }else{
                    Intent myIntent = new Intent(getActivity(), ActivityEditTask.class);
                    Task task = (Task) listAdapter.getItem(position);
                    myIntent.putExtra("taskDatabaseID", task.getId());
                    startActivity(myIntent);
                }

            }
        });

        return view;

    }
}
