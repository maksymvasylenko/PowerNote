package com.powernote.project.powernote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Task;

public class TaskCursorAdapter extends CursorAdapter {

    public TaskCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_task_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String noteText = cursor.getString( cursor.getColumnIndex(DBOpenHelper.KEY_TASK_NAME));
        TextView text = (TextView) view.findViewById(R.id.tv_task_item_title);
        text.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TASK_NAME)));
    }
}