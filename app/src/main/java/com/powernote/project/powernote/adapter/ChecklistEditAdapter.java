package com.powernote.project.powernote.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.R;

import java.util.List;

public class ChecklistEditAdapter extends ArrayAdapter<ChecklistItem> {

    public ChecklistEditAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChecklistItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item_alt, null);
        }

        final ChecklistItem item = getItem(position);

        // Define views
        TextView text = (TextView) convertView.findViewById(R.id.cb_item);
        Button deleteItem = (Button) convertView.findViewById(R.id.bt_checklist_item_delete);

        text.setText(item.getText());

        // Set views
        deleteItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(item);
                notifyDataSetChanged();
                return true;
            }
        });


        return convertView;
    }
}