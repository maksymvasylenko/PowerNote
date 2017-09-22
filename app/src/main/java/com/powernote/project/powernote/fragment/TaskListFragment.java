package com.powernote.project.powernote.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.powernote.project.powernote.activity.TaskActivity;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.adapter.TaskCursorAdapter;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TaskListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EDITOR_REQUEST_CODE = 1001;

    private List<Long> listOfSelectedId;

    private ListView list;
    private CursorAdapter cursorAdapter;
    private TaskAddedCallback addedCallback;

    private MenuItem delete;
    private boolean boolVisibility = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        
        listOfSelectedId = new ArrayList<>();
        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(getActivity(), TaskActivity.class);
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {



                Log.e("selectedItems", ":start");
                if(!listOfSelectedId.contains(id)) {
                    listOfSelectedId.add(id);
                    for (int i = 0; i < listOfSelectedId.size(); i++) {
                        Log.e("selectedItems", ":" + listOfSelectedId.get(i));
                    }
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    listOfSelectedId.remove(listOfSelectedId.indexOf(id));
                    view.setBackgroundColor(Color.WHITE);
                }



                if(listOfSelectedId.size() == 1) {
                    boolVisibility = true;
                    ActivityCompat.invalidateOptionsMenu(getActivity());
                }else if(listOfSelectedId.size() == 0){
                    boolVisibility = false;
                    ActivityCompat.invalidateOptionsMenu(getActivity());
                }

                return true;
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
        Log.e("result act", "" + resultCode);
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("result act", "reload");
            restartLoader();
        }else{
            Log.e("result act", "not correct request code");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.menu_overview, menu );
        delete = menu.findItem(R.id.action_delete);
        delete.setVisible(boolVisibility);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Press delete
            case R.id.action_delete:

                String [] stringList = new String[listOfSelectedId.size()];

                for (int i = 0; i < listOfSelectedId.size(); i++) {
                    stringList[i] = listOfSelectedId.get(i).toString();
                }

                String noteFilter = DBOpenHelper.KEY_ID + " IN (" + new String(new char[stringList.length-1]).replace("\0", "?,") + "?)";

                getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_TASKS,
                        noteFilter, stringList);

                listOfSelectedId = new ArrayList<>();

                boolVisibility = false;
                ActivityCompat.invalidateOptionsMenu(getActivity());

                restartLoader();
                break;
            default:
                return super.onOptionsItemSelected( item );
        }
        return super.onOptionsItemSelected( item );
    }
}
