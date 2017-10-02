package com.powernote.project.powernote.fragment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.powernote.project.powernote.adapter.ChecklistEditAdapter;
import com.powernote.project.powernote.adapter.NoteCursorAdapter;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.activity.NoteActivity;
import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int NOTE_EDITOR_REQUEST_CODE = 1010;
    
    private ListView list;
    private CursorAdapter cursorAdapter;

    private List<Long> listOfSelectedId;
    private boolean boolMultiSelection = false;//boolean if multi selection started


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        listOfSelectedId = new ArrayList<>();
        
	    cursorAdapter = new NoteCursorAdapter(getContext(), null, 1);
        list = (ListView) view.findViewById(R.id.listOverview);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(boolMultiSelection){
                    selectionOfItem(id, view);
                }else {
                    Intent myIntent = new Intent(getActivity(), NoteActivity.class);
                    myIntent.putExtra(PowerNoteProvider.CONTENT_ITEM_TYPE, id);
                    startActivityForResult(myIntent, NOTE_EDITOR_REQUEST_CODE);
                }
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                selectionOfItem(id, view);

                return true;
            }
        });
        
        getActivity().getLoaderManager().initLoader(1, null, this);

        return view;
    }

    //method for adding and deleting from list Of selected Items
    //changes background color to grey, activates menu with extra functionality(delete, etc)
    public void selectionOfItem(long itemId, View clickedView){
        Log.e("selectedItems", ":start");
        if(!listOfSelectedId.contains(itemId)) {
            listOfSelectedId.add(itemId);
            for (int i = 0; i < listOfSelectedId.size(); i++) {
                Log.e("selectedItems", ":" + listOfSelectedId.get(i));
            }
            clickedView.setBackgroundColor(Color.LTGRAY);
        }else{
            listOfSelectedId.remove(listOfSelectedId.indexOf(itemId));
            clickedView.setBackgroundColor(Color.WHITE);
        }

        if(listOfSelectedId.size() == 1) {
            boolMultiSelection = true;
            ActivityCompat.invalidateOptionsMenu(getActivity());
        }else if(listOfSelectedId.size() == 0){
            finishSelectionOfItems();
        }
        Log.e("selectedItems", ":" + boolMultiSelection);
    }

    public void finishSelectionOfItems(){
        listOfSelectedId = new ArrayList<>();
        boolMultiSelection = false;
        ActivityCompat.invalidateOptionsMenu(getActivity());
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
        if (requestCode == NOTE_EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("result act", "reload");
            restartLoader();
        }else{
            Log.e("result act", "reload failed");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.menu_extra_functions, menu );
        MenuItem delete = menu.findItem(R.id.action_delete);
        delete.setVisible(boolMultiSelection);
        Log.e("create menu", ":" + boolMultiSelection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Press delete
            case R.id.action_delete:
                actionDelete();
                break;
            default:
                return super.onOptionsItemSelected( item );
        }
        return super.onOptionsItemSelected( item );
    }

    public void actionDelete(){
        String [] stringList = new String[listOfSelectedId.size()];

        for (int i = 0; i < listOfSelectedId.size(); i++) {
            stringList[i] = listOfSelectedId.get(i).toString();
        }

        String noteFilter = DBOpenHelper.KEY_ID + " IN (" + new String(new char[stringList.length-1]).replace("\0", "?,") + "?)";

        getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_NOTES,
                noteFilter, stringList);

        finishSelectionOfItems();

        restartLoader();
    }
}