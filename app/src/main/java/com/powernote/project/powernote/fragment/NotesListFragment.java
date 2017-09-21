package com.powernote.project.powernote.fragment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.powernote.project.powernote.adapter.NoteCursorAdapter;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.activity.EditNoteActivity;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Maks on 21.09.2017.
 */

public class NotesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int NOTE_EDITOR_REQUEST_CODE = 1010;


    ListView list;
    CursorAdapter cursorAdapter;
    TaskAddedCallback addedCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addedCallback = (TaskAddedCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        cursorAdapter = new NoteCursorAdapter(getContext(), null, 1);



        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(getActivity(), EditNoteActivity.class);
                myIntent.putExtra(PowerNoteProvider.CONTENT_ITEM_TYPE, id);
                startActivityForResult(myIntent, NOTE_EDITOR_REQUEST_CODE);


            }
        });


        getActivity().getLoaderManager().initLoader(1, null, this);

        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.menu_note_list, menu );
    }
    
    private void insertNote() {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_NOTE_NAME, "title");
        values.put(DBOpenHelper.KEY_NOTE_TEXT, "description");
        values.put(DBOpenHelper.KEY_CREATED_AT, 1111);
        //values.put(DBOpenHelper.KEY_TASK_CHECKLIST, serializeChecklist(task.getCheckList()));

        Uri noteUri = getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_NOTES,
                values);
        Log.d("MainActivity", "Inserted note " + noteUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PowerNoteProvider.CONTENT_URI_NOTES,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getActivity().getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("result ", "notesListFragment");
        Log.e("result act", ""+resultCode);
        Log.e("result act", "request code"+requestCode);
        if (requestCode == NOTE_EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("result act", "reload");
            restartLoader();
        }else{
            Log.e("result act", "OK");
        }
    }
    
    
}