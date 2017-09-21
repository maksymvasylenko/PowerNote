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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.powernote.project.powernote.ActivityDetailsTask;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.TaskCursorAdapter;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.TaskAddedCallback;

import static android.app.Activity.RESULT_OK;


public class OverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1001;


    ListView list;
    CursorAdapter cursorAdapter;
    TaskAddedCallback addedCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        cursorAdapter = new TaskCursorAdapter(getContext(), null, 0);


        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(getActivity(), ActivityDetailsTask.class);
                myIntent.putExtra(PowerNoteProvider.CONTENT_ITEM_TYPE, id);
                startActivityForResult(myIntent, EDITOR_REQUEST_CODE);


                /*if(cursorAdapter.getItemViewType(position) == 1){
                    Intent myIntent = new Intent(getActivity(), ActivityEditNote.class);
                    Note note = (Note) cursorAdapter.getItem(position);
                    myIntent.putExtra("noteDatabaseID", note.getId());
                    startActivity(myIntent);

                }else{
                    Intent myIntent = new Intent(getActivity(), ActivityDetailsTask.class);
                    Task task = (Task) cursorAdapter.getItem(position);
                    myIntent.putExtra("taskID", task.getId());
                    startActivity(myIntent);
                }*/
            }
        });


        getActivity().getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PowerNoteProvider.CONTENT_URI_TASKS,
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
        getActivity().getLoaderManager().restartLoader(0, null, this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("result ", "overviewFragment");
        Log.e("result act", ""+resultCode);
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("result act", "reload");
            restartLoader();
        }else{
            Log.e("result act", "not correct request code");
        }

    }
}
