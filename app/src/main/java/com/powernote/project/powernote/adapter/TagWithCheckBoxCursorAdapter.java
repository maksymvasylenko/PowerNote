package com.powernote.project.powernote.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.DBOpenHelper;

/**
 * Created by Maks on 13.10.2017.
 */

public class TagWithCheckBoxCursorAdapter extends CursorAdapter {

    private String[] listOfSelectedId = null;
    private Activity activity;

    public TagWithCheckBoxCursorAdapter(Context context, Cursor c, int flags, String[] listOfSelectedId, Activity activity) {
        super(context, c, flags);
        this.listOfSelectedId = listOfSelectedId;
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_tag_item_with_checkbox, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        final long id = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.KEY_ID));
        Log.e("tagID", ":" + id);

        TextView text = (TextView) view.findViewById(R.id.tv_tag_item_with_checkbox_title);
        text.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.KEY_TAG_NAME)));

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_tag_item_with_checkbox_assigned);
        checkBox.setTag(id);

        //using tag id check task_tag table in sqlite for row containing tag_id and one of the task_id (one from listOfSelectedId)
        //if there is at least one row than checkBox should be checked and the other way around
        String noteFilter = DBOpenHelper.KEY_TASKS_TAGS_TAG_ID + "=" + id + " AND " +
                DBOpenHelper.KEY_TASKS_TAGS_TASK_ID + " IN (" + new String(new char[listOfSelectedId.length-1]).replace("\0", "?,") + "?)";
        Cursor taskTagCursor = activity.getContentResolver().query(PowerNoteProvider.CONTENT_URI_TASKS_TAGS,
                DBOpenHelper.TASKS_TAGS_ALL_COLUMNS, noteFilter, listOfSelectedId, null);


        if(taskTagCursor.moveToFirst()) {
            Log.e("checkbox set up","" + true);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(true);
        }else{
            Log.e("checkbox set up","" + false);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(false);
        }

        taskTagCursor.close();//not sure if this is correct


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.e("checked change","" );


                    long tempId = (long) buttonView.getTag();
                    if(isChecked){
                        ContentValues values;
                        Log.e("checkedchanged", "isChecked:" + listOfSelectedId.length + ":tagId:" + tempId);
                        for (String selectedTaskId : listOfSelectedId) {
                            values = new ContentValues();
                            values.put(DBOpenHelper.KEY_TASKS_TAGS_TAG_ID, tempId);
                            Log.e("checkedchanged", "taskId:" + selectedTaskId);
                            values.put(DBOpenHelper.KEY_TASKS_TAGS_TASK_ID, Long.valueOf(selectedTaskId));

                            activity.getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TASKS_TAGS, values);
                        }
                    }else{
                        Log.e("checkedchanged", "isNotChecked");
                        String noteFilter = DBOpenHelper.KEY_TASKS_TAGS_TAG_ID + "=" + tempId + " AND " +
                                DBOpenHelper.KEY_TASKS_TAGS_TASK_ID + " IN (" + new String(new char[listOfSelectedId.length-1])
                                .replace("\0", "?,") + "?)";
                        activity.getContentResolver().delete(PowerNoteProvider.CONTENT_URI_TASKS_TAGS, noteFilter, listOfSelectedId);

                    }

                }
            }
        );

    }

}