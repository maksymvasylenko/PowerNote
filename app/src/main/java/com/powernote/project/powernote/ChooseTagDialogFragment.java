package com.powernote.project.powernote;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.powernote.project.powernote.activity.TaskActivity;
import com.powernote.project.powernote.adapter.TagCursorAdapter;
import com.powernote.project.powernote.adapter.TagWithCheckBoxCursorAdapter;
import com.powernote.project.powernote.adapter.TaskCursorAdapter;
import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.TaskAddedCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maks on 13.10.2017.
 */

public class ChooseTagDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private String[] listOfSelectedId = null;
    private ListView list;
    private CursorAdapter cursorAdapter;
    private String selection = null;
    private String [] selectionArgs = null;

    public static ChooseTagDialogFragment newInstance(String[] listOfSelectedId) {
        ChooseTagDialogFragment yourDialogFragment = new ChooseTagDialogFragment();

        //example of passing args
        Bundle args = new Bundle();
        args.putStringArray(PowerNoteProvider.CONTENT_SELECTION_ARGS, listOfSelectedId);
        yourDialogFragment.setArguments(args);

        return yourDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_tag_fragment_dialog, null);

        this.listOfSelectedId = getArguments().getStringArray(PowerNoteProvider.CONTENT_SELECTION_ARGS);


        final EditText searchBox = (EditText) view.findViewById(R.id.et_choose_tag_fragment_dialog_search);

        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String inputText = searchBox.getText().toString();

                        //inserting tag to DB
                        ContentValues values = new ContentValues();
                        values.put(DBOpenHelper.KEY_TAG_NAME, inputText);

                        getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values);
                        restartLoader();
                        searchBox.setText("");

                        Log.e("chooseTagDialogFragment", ": entered text:" + inputText);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    Log.e("chooseTagDialogFragment", ": event not null");
                    return false;
                }
            }
        };

        // Set keyEvent listener on editText
        searchBox.setOnEditorActionListener(listener);




        cursorAdapter = new TagWithCheckBoxCursorAdapter(getContext(), null, 3, this.listOfSelectedId, getActivity());

        list = (ListView) view.findViewById(R.id.lv_choose_tag_fragment_dialog_tags);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_tag_item_with_checkbox_assigned);
                checkBox.isChecked();
                // TODO: 13.10.2017 id might not be not correct!!!

            }
        });




        getActivity().getLoaderManager().initLoader(3, null, this);


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PowerNoteProvider.CONTENT_URI_TAGS,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e("taskListFragment"," count items:" + data.getCount() );
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getActivity().getLoaderManager().restartLoader(3, null, this);
    }


}