package com.powernote.project.powernote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.powernote.project.powernote.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maks on 13.09.2017.
 */

public class FragmentNoteslist extends Fragment {

    private PowerNotes pwn = PowerNotes.getInstance();


    public FragmentNoteslist() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noteslist, container, false);


        if(pwn.getNotes() != null) {
            ArrayList<Note> notes = new ArrayList<Note>(pwn.getNotes().values());

            Log.e("test 2", "" + notes.get(0).getName());
            ListAdapterNotes listAdapterNotes = new ListAdapterNotes(getContext(), R.layout.list_note_item, notes);

            ListView list = (ListView) view.findViewById(R.id.lv_notes);
            list.setAdapter(listAdapterNotes);

        }

        return view;
    }
}