package com.powernote.project.powernote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTaskEdit extends Fragment {
    // views
    ListView lvCheckist;
    ChecklistEditAdapter adapter;
    Switch swDeadline;
    Switch swChecklist;
    Switch swEffort;
    LinearLayout layoutChecklist;
    LinearLayout layoutDeadline;
    LinearLayout layoutEffort;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task, container, false);

        lvCheckist = (ListView) view.findViewById(R.id.lv_checklist_edit);

        // Switches (for switching items on and off)
        swChecklist = (Switch) view.findViewById(R.id.sw_checklist);
        swEffort = (Switch) view.findViewById(R.id.sw_effort);
        swDeadline = (Switch) view.findViewById(R.id.sw_deadline);

        // Layouts (containers for items that can be switched on or off)
        layoutChecklist = (LinearLayout) view.findViewById(R.id.layout_checklist);
        layoutEffort = (LinearLayout) view.findViewById(R.id.layout_effort);
        layoutDeadline = (LinearLayout) view.findViewById(R.id.layout_deadline);

        // Switch listeners
        swChecklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutChecklist.setVisibility(View.VISIBLE);
                } else {
                    layoutChecklist.setVisibility(View.GONE);
                }
            }
        });

        swEffort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutEffort.setVisibility(View.VISIBLE);
                } else {
                    layoutEffort.setVisibility(View.GONE);
                }
            }
        });

        swDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    layoutDeadline.setVisibility(View.VISIBLE);
                } else {
                    layoutDeadline.setVisibility(View.GONE);
                }
            }
        });

        // TODO: 9/12/17 get checklist from current task
        final List items = new ArrayList();

        adapter = new ChecklistEditAdapter(getContext(), R.layout.checklist_item_alt, items);
        lvCheckist.setAdapter(adapter);

        final EditText etCheckListInput = (EditText) view.findViewById(R.id.et_checklist_input);

        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (event == null) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String inputText = etCheckListInput.getText().toString();
                        if (inputText.isEmpty()){
                            inputText = "Empty";
                        }
                        items.add(new ListItem(inputText, false));
                        etCheckListInput.setText("");
                        adapter.notifyDataSetChanged();
                    } else {
                        return true;   // We consume the event when the key is released.
                    }
                } else {
                    return false;
                }
                return true;   // Consume the event
            }
        };

        // Set keyEvent listener on editText
        etCheckListInput.setOnEditorActionListener(listener);

        return view;
    }
}
