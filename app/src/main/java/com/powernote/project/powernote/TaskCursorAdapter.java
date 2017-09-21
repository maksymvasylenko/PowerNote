package com.powernote.project.powernote;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Task;

/**
 * Created by Maks on 20.09.2017.
 */

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



        TextView text = (TextView) view.findViewById(R.id.tv_task_item_title);

        String title = cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TASK_NAME));
        if(title != null && !title.isEmpty()){
            text.setText(title);
        }else{
            text.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TASK_DESCRIPTION)));
        }

    }
}