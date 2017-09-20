package com.powernote.project.powernote.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.R;

import java.util.List;

public class ChecklistViewAdapter extends ArrayAdapter<ChecklistItem> {

    public ChecklistViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChecklistItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checklist_item, null);
        }

        final ChecklistItem item = getItem(position);

        // Define views
        CheckBox checkItem = (CheckBox) convertView.findViewById(R.id.cb_item);

        // Set views
        checkItem.setText(item.getText());
        checkItem.setChecked(item.isChecked());

        checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                item.setChecked(!item.isChecked());
            }
        });

        return convertView;
    }
}
