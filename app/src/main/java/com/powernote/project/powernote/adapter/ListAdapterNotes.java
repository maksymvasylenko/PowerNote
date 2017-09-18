package com.powernote.project.powernote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.Note;

import java.util.ArrayList;

/**
 * Created by Maks on 13.09.2017.
 */

public class ListAdapterNotes extends ArrayAdapter<Note> {

    ArrayList<Note> notesList = new ArrayList<>();

    public ListAdapterNotes(Context context, int textViewResourceId, ArrayList<Note> objects) {
        super(context, textViewResourceId, objects);
        notesList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_note_item, null);


        TextView text2 = (TextView) v.findViewById(R.id.listItemText);
        Note currentNoteItem = (Note) getItem(position);

        if(currentNoteItem.getTitle() != null) {
            text2.setText(currentNoteItem.getTitle());
        }else{
            text2.setText(currentNoteItem.getTitle());
        }
        return v;

    }

}