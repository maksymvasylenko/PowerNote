package com.powernote.project.powernote;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Maks on 25.07.2017.
 */

/*class ListAdapter extends android.widget.CursorAdapter {
    public ListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String noteText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        int pos = noteText.indexOf(10);
        if (pos != -1) {
            noteText = noteText.substring(0, pos) + " ...";
        }

        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);

    }
}*/
class ListAdapter extends BaseAdapter {

    private static final int TYPE_TASK = 0;
    private static final int TYPE_NOTE = 1;
    private static final int TYPE_MAX_COUNT = TYPE_NOTE + 1;
    private Context context;

    private List<Object> mData = new ArrayList();
    private LayoutInflater mInflater;

    private TreeSet mNotesSet = new TreeSet();

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void addTaskItem(Task item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addNoteItem(Note item) {
        mData.add(item);
        // save separator position
        mNotesSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mNotesSet.contains(position) ? TYPE_NOTE : TYPE_TASK;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        System.out.println("getView " + position + " " + convertView + " type = " + type);
        if (convertView == null) {
            switch (type) {
                case TYPE_TASK:
                    convertView = LayoutInflater.from(this.context).inflate(R.layout.list_item, null);
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageDocIcon);
                    imageView.setImageResource(R.drawable.ic_document_red);

                    TextView text = (TextView) convertView.findViewById(R.id.listItemText);
                    Task currentTaskItem = (Task) getItem(position);

                    if(currentTaskItem.getName() != null) {
                        text.setText(currentTaskItem.getName());
                    }else{
                        text.setText(currentTaskItem.getDescription());
                    }

                    break;
                case TYPE_NOTE:
                    convertView = LayoutInflater.from(this.context).inflate(R.layout.list_item, null);
                    TextView text2 = (TextView) convertView.findViewById(R.id.listItemText);
                    Note currentNoteItem = (Note) getItem(position);

                    if(currentNoteItem.getName() != null) {
                        text2.setText(currentNoteItem.getName());
                    }else{
                        text2.setText(currentNoteItem.getText());
                    }
                    break;
            }
        }
        return convertView;
    }

}
