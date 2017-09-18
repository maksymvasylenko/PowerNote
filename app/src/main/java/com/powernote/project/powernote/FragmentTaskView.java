package com.powernote.project.powernote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.powernote.project.powernote.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
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





        final TextView title = (TextView) view.findViewById(R.id.tv_task_view_title);
        final TextView description = (TextView) view.findViewById(R.id.tv_task_view_description);
        final ProgressBar effort = (ProgressBar) view.findViewById(R.id.pb_effort);
        final ProgressBar priority = (ProgressBar) view.findViewById(R.id.pb_priority);

        tvDate = (TextView) view.findViewById(R.id.tv_task_view_deadline_date);
        tvTime = (TextView) view.findViewById(R.id.tv_task_view_deadline_time);

        layoutEffort = (LinearLayout) view.findViewById(R.id.ll_task_view_effort_priority);
        layoutDeadline = (LinearLayout) view.findViewById(R.id.ll_task_view_deadline);

        currentTask = pwn.getTask(pwn.getCurrentSelectedItem());

        if(currentTask != null) {

            //obligatory
            if (currentTask.getName() != null) {
                title.setText(currentTask.getName());
            }
            if (currentTask.getDescription() != null) {
                description.setText(currentTask.getDescription());
            }

            //effort
            if (currentTask.getEffort() != -1) {
                layoutEffort.setVisibility(View.VISIBLE);
                effort.setProgress(currentTask.getEffort());
                priority.setProgress(currentTask.getRank());
            }

            //deadline
            if (currentTask.getDeadline() != -1) {
                layoutDeadline.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTask.getDeadline());

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                tvDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                tvTime.setText(hour + ":" + min);
            }

        }

        return view;
    }

}
