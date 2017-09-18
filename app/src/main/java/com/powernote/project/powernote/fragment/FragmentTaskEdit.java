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
    private List items;

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

    private EditText title;
    private EditText description;
    private SeekBar effort;
    private SeekBar priority;
    Button saveButton;

    private PowerNote pwn = PowerNote.getInstance();
    private Task currentTask;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);

    //variables for taking photo
    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;
    

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


        //initializing xml elements
        title = (EditText) view.findViewById(R.id.et_task_edit_title);
        description = (EditText) view.findViewById(R.id.et_task_edit_description);
        effort = (SeekBar) view.findViewById(R.id.sb_task_edit_effort);
        priority = (SeekBar) view.findViewById(R.id.sb_task_edit_priority);
        saveButton = (Button) view.findViewById(R.id.bt_task_edit_save);

        imageView = (ImageView) view.findViewById(R.id.image);
        layoutImages = (LinearLayout) view.findViewById(R.id.layout_images);

        Button date = (Button) view.findViewById(R.id.bt_edit_task_date);
        Button time = (Button) view.findViewById(R.id.bt_edit_task_time);
        tvDate = (TextView) view.findViewById(R.id.tv_edit_task_date);
        tvTime = (TextView) view.findViewById(R.id.tv_edit_task_time);
        
        
        
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
        items = new ArrayList();

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
                        return true;
                    }
                } else {
                    return false;
                }
                return true;
            }
        };

        // Set keyEvent listener on editText
        etCheckListInput.setOnEditorActionListener(listener);
        

        //// TODO: 18.09.2017 needs to be done through bundle not through model 
        if(pwn.getCurrentSelectedItem() == -1) {
            currentTask = new Task();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwn.addTask(getTheCurrentSelectedData(currentTask));
                    Snackbar.make(v, "Task created", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        }else{
            currentTask = pwn.getTask(pwn.getCurrentSelectedItem());

            // Default items
            title.setText(currentTask.getTitle());
            description.setText(currentTask.getDescription());
            saveButton.setText("Update");

            // Effort
            if(currentTask.getEffort() != -1) {
                layoutEffort.setVisibility(View.VISIBLE);
                effort.setProgress(currentTask.getEffort());
                priority.setProgress(currentTask.getRank());
            }

            // Deadline
            if(currentTask.getDeadline() != -1) {
                layoutDeadline.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTask.getDeadline());

                updateDeadlineDateText(calendar);
                updateDeadlineTimeText(calendar);
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pwn.updateTask(getTheCurrentSelectedData(currentTask));
                    Snackbar.make(v, "Task updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }


        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                updateDeadlineDateText(calendar);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                // TODO: 9/18/17 update new deadline as variable
            }
        });

        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                updateDeadlineTimeText(c);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        return view;
    }

    private void updateDeadlineDateText(Calendar calendar){
        String dateText = sdf.format(calendar.getTime().getTime());
        tvDate.setText(dateText);
    }

    private void updateDeadlineTimeText(Calendar calendar){
        String timeText = sdf.format(calendar.getTime().getTime());
        tvTime.setText(timeText);
    }

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


    private Task getTheCurrentSelectedData(Task task){

        if (swChecklist.isChecked()) {
            // TODO: 9/18/17 save checklist
        }

        if (swDeadline.isChecked()) {
            // TODO: 9/18/17 set deadline as current selected deadline
//                        currentTask.setDeadline(getDeadlineTimeInMillisends());
        }else{
            task.setDeadline(-1);
        }

        if (swEffort.isChecked()) {
            task.setEffort(effort.getProgress());
            task.setRank(priority.getProgress());
        }else{
            task.setEffort(-1);
            task.setRank(-1);
        }

        task.setCreatedAt(System.currentTimeMillis());
        task.setTitle(title.getText().toString());
        task.setDescription(description.getText().toString());

        task.setCheckList(items);

        return task;
    }
}
