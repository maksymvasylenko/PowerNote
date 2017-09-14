package com.powernote.project.powernote;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.powernote.project.powernote.model.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, null);
        }

        Task task = getItem(position);

        // Set view properties
        TextView title = (TextView) convertView.findViewById(R.id.tv_task_item_title);
        TextView duration = (TextView) convertView.findViewById(R.id.tv_task_item_duration);

        title.setText(task.getDescription());
        duration.setText(String.valueOf(task.getDuration()));

        return convertView;
    }
}
