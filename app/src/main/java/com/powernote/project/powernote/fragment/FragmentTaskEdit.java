package com.powernote.project.powernote.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.powernote.project.powernote.model.PowerNote;
import com.powernote.project.powernote.model.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.powernote.project.powernote.adapter.ChecklistEditAdapter;
import com.powernote.project.powernote.model.ListItem;
import com.powernote.project.powernote.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FragmentTaskEdit extends Fragment {

    private ListView lvChecklist;
    private ChecklistEditAdapter adapter;

    private Switch swDeadline;
    private Switch swChecklist;
    private Switch swEffort;

    private LinearLayout layoutChecklist;
    private LinearLayout layoutDeadline;
    private LinearLayout layoutEffort;
    private LinearLayout layoutImages;

    private ImageView imageView;
    private TextView tvTime;
    private TextView tvDate;

    private PowerNote pwn = PowerNote.getInstance();
    private Task currentTask;

    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                pwn.deleteTask(pwn.getCurrentSelectedItem());
                break;
            case R.id.action_take_photo:
                dispatchTakePictureIntent();
                break;
            case R.id.action_add_image:
                break;
            case R.id.action_record:
                break;
            case R.id.action_add_checklist:
                break;
            case R.id.action_add_deadline:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_task, container, false);

        lvChecklist = (ListView) view.findViewById(R.id.lv_checklist_edit);

        // Switches (for switching items on and off)
        swChecklist = (Switch) view.findViewById(R.id.sw_checklist);
        swEffort = (Switch) view.findViewById(R.id.sw_effort);
        swDeadline = (Switch) view.findViewById(R.id.sw_deadline);

        // Layouts (containers for items that can be switched on or off)
        layoutChecklist = (LinearLayout) view.findViewById(R.id.layout_checklist);
        layoutEffort = (LinearLayout) view.findViewById(R.id.layout_effort);
        layoutDeadline = (LinearLayout) view.findViewById(R.id.layout_deadline);

        // Switch listeners  et_task_edit_title
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
        lvChecklist.setAdapter(adapter);

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

        final EditText title = (EditText) view.findViewById(R.id.et_task_edit_title);
        final EditText description = (EditText) view.findViewById(R.id.et_task_edit_description);
        final SeekBar effort = (SeekBar) view.findViewById(R.id.sb_task_edit_effort);
        final SeekBar priority = (SeekBar) view.findViewById(R.id.sb_task_edit_priority);
        Button saveButton = (Button) view.findViewById(R.id.bt_task_edit_save);

        imageView = (ImageView) view.findViewById(R.id.image);
        layoutImages = (LinearLayout) view.findViewById(R.id.layout_images);

        Button date = (Button) view.findViewById(R.id.bt_edit_task_date);
        Button time = (Button) view.findViewById(R.id.bt_edit_task_time);
        tvDate = (TextView) view.findViewById(R.id.tv_edit_task_date);
        tvTime = (TextView) view.findViewById(R.id.tv_edit_task_time);

        if(pwn.getCurrentSelectedItem() == -1) {
            currentTask = new Task();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (swChecklist.isChecked()) {

                    }

                    if (swDeadline.isChecked()) {
                        currentTask.setDeadline(getDeadlineTimeInMillisends());
                    }else{
                        currentTask.setDeadline(-1);
                    }

                    if (swEffort.isChecked()) {
                        currentTask.setEffort(effort.getProgress());
                        currentTask.setRank(priority.getProgress());
                    }else{
                        currentTask.setEffort(-1);
                        currentTask.setRank(-1);
                    }

                    currentTask.setName(title.getText().toString());
                    currentTask.setDescription(description.getText().toString());


                    pwn.addTask(currentTask);
                    Snackbar.make(v, "Task created", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }else{
            currentTask = pwn.getTask(pwn.getCurrentSelectedItem());

            //obligatory
            title.setText(currentTask.getName());
            description.setText(currentTask.getDescription());
            saveButton.setText("Update");

            //effort
            if(currentTask.getEffort() != -1) {
                layoutEffort.setVisibility(View.VISIBLE);
                effort.setProgress(currentTask.getEffort());
                priority.setProgress(currentTask.getRank());
            }

            //deadline
            if(currentTask.getDeadline() != -1) {
                layoutDeadline.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTask.getDeadline());

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                // TODO: 9/18/17 use simple date format to format date
                tvDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                // TODO: 9/18/17 use simple date format to format time
                tvTime.setText(hour + ":" + min);
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swChecklist.isChecked()) {
                        // TODO: 9/18/17 save the checklist
                    }

                    if (swDeadline.isChecked()) {
                        currentTask.setDeadline(getDeadlineTimeInMillisends());
                    }

                    if (swEffort.isChecked()) {
                        currentTask.setEffort(effort.getProgress());
                        currentTask.setRank(priority.getProgress());
                    }

                    currentTask.setName(title.getText().toString());
                    currentTask.setDescription(description.getText().toString());

                    pwn.updateTask(currentTask);
                    Snackbar.make(v, "Task updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    getActivity().getSupportFragmentManager().popBackStack();

                }
            });
        }



        setCurrentDate();

        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tvTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });




        return view;
    }


    private void setCurrentDate() {
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

    private long getDeadlineTimeInMillisends(){

        String date = tvDate.getText().toString();
        String[] dt = date.split("-");


        String time = tvTime.getText().toString();
        String[] tm = time.split(":");

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dt[2]),Integer.parseInt(dt[1]),
                Integer.parseInt(dt[0]), Integer.parseInt(tm[0]),
                Integer.parseInt(tm[1]));
        return c.getTimeInMillis();
    }







    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;

    private void dispatchTakePictureIntent() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.powernote.project.powernote.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
            layoutImages.setVisibility(View.VISIBLE);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentTask.setImagePath(image.getAbsolutePath());
        Log.e("test 22:", image.getAbsolutePath());
        return image;
    }

}
