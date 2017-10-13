package com.powernote.project.powernote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;

/**
 * Created by Maks on 13.10.2017.
 */

public class TagCursorAdapter extends CursorAdapter {

    public TagCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_tag_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView text = (TextView) view.findViewById(R.id.tv_task_item_title);
        String name = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TAG_NAME));
        text.setText(name);

    }


}
