package com.powernote.project.powernote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Maks on 25.07.2017.
 */

public class NoteAdapter  extends ArrayAdapter<Note> {

    public NoteAdapter(Context context) {
        super(context, 0, NoteCollection.getInstance().getNotes());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_note, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView author = (TextView) convertView.findViewById(R.id.tv_author);
        ImageView image = (ImageView) convertView.findViewById(R.id.iv_icon);
        // NB the above could be optimized further by using a ViewHolder class

        Note book = getItem(position);
        title.setText(book.getName());
        author.setText(book.getText());

        return convertView;
    }
}
