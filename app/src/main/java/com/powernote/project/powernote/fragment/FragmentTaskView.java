package com.powernote.project.powernote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FragmentTaskView extends Fragment {
    private ChecklistViewAdapter adapter;
    private ListView lvCheckist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_view, container, false);

        lvCheckist = (ListView) view.findViewById(R.id.lv_checklist_view);

        final List items = new ArrayList();
        items.add(new ListItem("item1", true));

        adapter = new ChecklistViewAdapter(getContext(), R.layout.checklist_item_alt, items);
        lvCheckist.setAdapter(adapter);

        return view;
    }
}
