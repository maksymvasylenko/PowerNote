package com.powernote.project.powernote.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.NonScrollListView;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.adapter.TagCursorAdapter;
import com.powernote.project.powernote.model.DBOpenHelper;

/**
 * Created by Maks on 13.10.2017.
 */

public class TagListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private CursorAdapter cursorAdapter;
    private NonScrollListView list;
    private Methods.OnPresortSelectedListener mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nonscrolllistview, container, false);


        /*ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_TAG_NAME, "test1");
        getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values);
        ContentValues values2 = new ContentValues();
        values2.put(DBOpenHelper.KEY_TAG_NAME, "test2");
        getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values2);*/

        /*ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_TAG_NAME, "ccc");
        getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values);

        ContentValues values2 = new ContentValues();
        values.put(DBOpenHelper.KEY_TAG_NAME, "aaa");
        getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TAGS, values2);*/
/*

        Cursor cursor = getActivity().getContentResolver().query(PowerNoteProvider.CONTENT_URI_TAGS, null, null,null,null);
        cursor.moveToFirst();

        Log.e("tag name",":" + cursor.getCount());*/


        cursorAdapter = new TagCursorAdapter(getContext(), null, 2);

        // TODO: 13.10.2017 important: on xiaomi redmi note 3 list doesnt show all items!!!
        /*
        * was tested on 3 emulators. Works on each of them !!!
        */


        list = (NonScrollListView) view.findViewById(R.id.listOverview);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 13.10.2017 id might not be not correct!!!

                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                long tagId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));

                mCallback.onTagSelected(tagId);
            }
        });

        getActivity().getLoaderManager().initLoader(2, null, this);

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
        getActivity().getLoaderManager().restartLoader(2, null, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Methods.OnPresortSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
