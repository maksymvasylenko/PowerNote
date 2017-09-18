package com.powernote.project.powernote;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

public class ChecklistViewAdapter extends ArrayAdapter<ListItem> {

    public ChecklistViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ListItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, null);
        }

        final ListItem item = getItem(position);

        // Define views
        CheckBox checkItem = (CheckBox) convertView.findViewById(R.id.cb_item);

        // Set views
        checkItem.setText(item.getText());
        checkItem.setChecked(item.isChecked());

        checkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setChecked(true);
                notifyDataSetChanged();
            }
        });

        checkItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                item.setChecked(false);
                notifyDataSetChanged();
                return true;
            }
        });

        return convertView;
    }
}
