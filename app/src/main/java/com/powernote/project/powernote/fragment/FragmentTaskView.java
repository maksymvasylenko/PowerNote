package com.powernote.project.powernote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.powernote.project.powernote.model.Task;

import com.powernote.project.powernote.adapter.ChecklistViewAdapter;
import com.powernote.project.powernote.model.ListItem;
import com.powernote.project.powernote.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentTaskView extends Fragment {
    private ChecklistViewAdapter adapter;
    private ListView lvCheckist;



    Switch swDeadline;
    Switch swChecklist;
    Switch swEffort;
    LinearLayout layoutChecklist;
    LinearLayout layoutDeadline;
    LinearLayout layoutEffort;



    LinearLayout layoutImages;
    ImageView imageView;
    TextView tvTime;
    TextView tvDate;

    private PowerNotes pwn = PowerNotes.getInstance();
    private Task currentTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                pwn.deleteTask(pwn.getCurrentSelectedItem());
                break;
            case R.id.action_edit:

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

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
