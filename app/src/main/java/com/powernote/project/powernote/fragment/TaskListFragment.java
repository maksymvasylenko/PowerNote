package com.powernote.project.powernote.fragment;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private String selection = null;
    private String [] selectionArgs = null;


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


        if(getArguments() != null) {
            selection = getArguments().getString(PowerNoteProvider.CONTENT_SELECTION);
            selectionArgs = getArguments().getStringArray(PowerNoteProvider.CONTENT_SELECTION_ARGS);
        }


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

            }
        });

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(!listOfSelectedId.contains(id)) {
                    listOfSelectedId.add(id);
                }else{
                    listOfSelectedId.remove(listOfSelectedId.indexOf(id));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_extra_functions, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    // Press delete
                    case R.id.action_delete:
                        actionDelete();
                        mode.finish();
                        return true;
                    case R.id.action_change_color:

                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.choose_color_dialog);
                        dialog.setTitle("Choose Color");

                        dialog.show();

                        Button btnGreen = (Button) dialog.findViewById(R.id.colorGreenButton);
                        Button btnRed = (Button) dialog.findViewById(R.id.colorRedrButton);
                        Button btnPurple = (Button) dialog.findViewById(R.id.colorPurpleButton);

                        Button btnBlue = (Button) dialog.findViewById(R.id.colorBlueButton);
                        Button btnDarkBlue = (Button) dialog.findViewById(R.id.colorDarkBlueButton);
                        Button btnOrange = (Button) dialog.findViewById(R.id.colorOrangeButton);

                        Button btnYellow = (Button) dialog.findViewById(R.id.colorYellowButton);
                        Button btnPink = (Button) dialog.findViewById(R.id.colorPinkButton);
                        Button btnWhite = (Button) dialog.findViewById(R.id.colorWhiteButton);


                        setColorButton(getResources().getColor(R.color.colorPurple), btnPurple, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorRed), btnRed, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorGreen), btnGreen, mode, dialog);

                        setColorButton(getResources().getColor(R.color.colorBlue), btnBlue, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorDarkBlue), btnDarkBlue, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorOrange), btnOrange, mode, dialog);

                        setColorButton(getResources().getColor(R.color.colorYellow), btnYellow, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorPink), btnPink, mode, dialog);
                        setColorButton(getResources().getColor(R.color.colorWhite), btnWhite, mode, dialog);

                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                listOfSelectedId = new ArrayList<>();
            }
        });


        
        getActivity().getLoaderManager().initLoader(0, null, this);


        return view;
    }

    private void setColorButton(final int color, Button btn, final ActionMode mode, final Dialog dialog){
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{color,color});
        gd.setCornerRadius(100f);

        btn.setBackgroundDrawable(gd);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionChangeColor(color);
                Log.e("color", "" + color);
                mode.finish();
                dialog.cancel();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PowerNoteProvider.CONTENT_URI_TASKS,
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

    private void actionDelete(){
        String [] stringList = new String[listOfSelectedId.size()];

        for (int i = 0; i < listOfSelectedId.size(); i++) {
            stringList[i] = listOfSelectedId.get(i).toString();
        }


        String noteFilter = DBOpenHelper.KEY_ID + " IN (" + new String(new char[stringList.length-1]).replace("\0", "?,") + "?)";

        getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_TASKS,
                noteFilter, stringList);

        listOfSelectedId = new ArrayList<>();
        restartLoader();
    }


    private void actionChangeColor(int color){

        String [] stringList = new String[listOfSelectedId.size()];

        for (int i = 0; i < listOfSelectedId.size(); i++) {
            stringList[i] = listOfSelectedId.get(i).toString();
        }
        Log.e("stringList", "" + (stringList.length-1) + ":" + stringList.length + ":" + listOfSelectedId.size());


        String noteFilter = DBOpenHelper.KEY_ID + " IN (" + new String(new char[stringList.length-1]).replace("\0", "?,") + "?)";

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_BACKGROUNDCOLOR, color);

        getActivity().getContentResolver().update(PowerNoteProvider.CONTENT_URI_TASKS, values,
                noteFilter, stringList);

        listOfSelectedId = new ArrayList<>();
        restartLoader();
    }
}
