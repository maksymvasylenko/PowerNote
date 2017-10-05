package com.powernote.project.powernote.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.powernote.project.powernote.adapter.ChecklistEditAdapter;
import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.TaskAddedCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class FragmentTaskEdit extends Fragment {

    private static final int DATE_TIME = 9912, DATE = 1231;
    private ListView lvChecklist;
    private ChecklistEditAdapter adapter;
    private List items;

    private Switch swDeadline, swChecklist, swEffort;

    private LinearLayout layoutChecklist, layoutDeadline, layoutEffort, layoutImages;

    private ImageView imageView;
    private TextView tvTime, tvDate;

    private EditText title, description;
    private SeekBar effort, priority;

    private Button saveButton;

    private TaskAddedCallback addedCallback;

    private Task currentTask;

    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.US);
    private SimpleDateFormat stf = new SimpleDateFormat("HH:mm", Locale.US);

    //variables for taking photo
    static final int REQUEST_TAKE_PHOTO = 12364, REQUEST_ADD_PHOTO = 26513;

    private String imagePath = null;



    final Calendar calendar = Calendar.getInstance();

    private String noteFilter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_task_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_NOTES,
                        noteFilter, null);

                getActivity().setResult(RESULT_OK);
                getActivity().finish();
                break;
            case R.id.action_take_photo:
                dispatchTakePictureIntent();
                break;
            case R.id.action_add_image:
                addImageFromGallery();
                break;
            case R.id.action_record:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.task_edit, container, false);


        lvChecklist = (ListView) view.findViewById(R.id.lv_checklist_edit);

        //initializing xml elements
        title = (EditText) view.findViewById(R.id.et_task_edit_title);
        description = (EditText) view.findViewById(R.id.et_task_edit_description);
        effort = (SeekBar) view.findViewById(R.id.sb_effort);
        priority = (SeekBar) view.findViewById(R.id.sb_priority);
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


        updateDeadlineTimeText(calendar);
        updateDeadlineDateText(calendar);

        // Switch listeners  et_task_edit_title
        swChecklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layoutChecklist.setVisibility(View.VISIBLE);
                } else {
                    layoutChecklist.setVisibility(View.GONE);
                }
            }
        });

        swEffort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layoutEffort.setVisibility(View.VISIBLE);
                } else {
                    layoutEffort.setVisibility(View.GONE);
                }
            }
        });

        swDeadline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    layoutDeadline.setVisibility(View.VISIBLE);
                    chooseDeadline();
                } else {
                    layoutDeadline.setVisibility(View.GONE);
                }
            }
        });

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
                        if (inputText.isEmpty()) {
                            inputText = "Empty";
                        }
                        items.add(new ChecklistItem(inputText, false));
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


        if (getArguments() != null) {


            long id = getArguments().getLong(PowerNoteProvider.CONTENT_ITEM_TYPE);
            Uri uri = Uri.parse(PowerNoteProvider.CONTENT_URI_TASKS + "/" + id);


            noteFilter = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getActivity().getContentResolver().query(uri,
                    DBOpenHelper.TASK_ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();


            currentTask = Methods.getNewTask(cursor);

            view.setBackgroundColor(currentTask.getBackgroundColor());

            // Default items
            title.setText(currentTask.getTitle());
            description.setText(currentTask.getDescription());
            saveButton.setText("Update");

            // Effort
            if (currentTask.getEffort() != -1) {
                layoutEffort.setVisibility(View.VISIBLE);
                effort.setProgress(currentTask.getEffort());
                priority.setProgress(currentTask.getRank());
            }

            // Deadline
            if (currentTask.getDeadline() != -1) {
                layoutDeadline.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTask.getDeadline());

                updateDeadlineDateText(calendar);
                updateDeadlineTimeText(calendar);
            }

            if(currentTask.getImagePath() != null && !currentTask.getImagePath().isEmpty()){

                layoutImages.setVisibility(View.VISIBLE);
                Uri imageUri = Uri.parse(currentTask.getImagePath());
                imageView.setImageURI(imageUri);
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO fix updating
                    getActivity().getContentResolver().update(PowerNoteProvider.CONTENT_URI_TASKS,
                            Methods.getTaskValues(getTheCurrentSelectedData(currentTask)),noteFilter, null);


                    Snackbar.make(v, "Task updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    getActivity().setResult(RESULT_OK);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

        } else {
            currentTask = new Task();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getContentResolver().insert(PowerNoteProvider.CONTENT_URI_TASKS,
                            Methods.getTaskValues(getTheCurrentSelectedData(currentTask)));

                    Snackbar.make(v, "Task created", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    Log.e("before finish act", "fin in taskEditFrag");
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                }
            });
        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeadlineDate(DATE);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDeadlineTime();
            }
        });


        return view;
    }

    private void chooseDeadline(){
        chooseDeadlineDate(DATE_TIME);
    }

    private void chooseDeadlineDate(final int datePickerType){
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDeadlineDateText(calendar);
                        if(datePickerType == DATE_TIME) {
                            chooseDeadlineTime();
                        }
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void chooseDeadlineTime() {

        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateDeadlineTimeText(calendar);
                        Log.e("calendar", ":" + calendar.getTimeInMillis());
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void updateDeadlineDateText(Calendar calendar) {
        String dateText = sdf.format(calendar.getTime().getTime());
        tvDate.setText(dateText);
    }

    private void updateDeadlineTimeText(Calendar calendar) {
        String timeText = stf.format(calendar.getTime().getTime());
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
                Log.e("dispatch photo ", "" + photoFile.getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile(getContext(),
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
            Methods.setPic(imagePath, imageView, getActivity());



            Log.e("setted picture", "");
            layoutImages.setVisibility(View.VISIBLE);
        }else if(requestCode == REQUEST_ADD_PHOTO && resultCode == RESULT_OK){
            Log.e("add gallery picture", "");


            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Methods.setPic(picturePath, imageView, getActivity());
            imagePath = picturePath;
            layoutImages.setVisibility(View.VISIBLE);
        }
        Log.e("fragment task edit ", "code:" + requestCode);

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: imagePath for use with ACTION_VIEW intents
        currentTask.setImagePath(image.getAbsolutePath());

        imagePath = image.getAbsolutePath();


        Log.e("test 22:", image.getAbsolutePath());
        return image;
    }

    private void addImageFromGallery(){
        startActivityForResult(
                new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ), REQUEST_ADD_PHOTO);
    }



    private Task getTheCurrentSelectedData(Task task) {

        if (swChecklist.isChecked()) {
            task.setCheckList(items);
        }

        if (swDeadline.isChecked()) {
            task.setDeadline(calendar.getTimeInMillis());
        } else {
            task.setDeadline(-1);
        }

        if (swEffort.isChecked()) {
            task.setEffort(effort.getProgress());
            task.setRank(priority.getProgress());


            Log.e("edit Task", " effort:" + task.getEffort());
            Log.e("edit Task", " progress:" + task.getRank());

        } else {
            task.setEffort(-1);
            task.setRank(-1);
        }

        task.setCreatedAt(System.currentTimeMillis());
        task.setTitle(title.getText().toString());
        task.setDescription(description.getText().toString());

        if(imagePath != null){
            task.setImagePath(imagePath);
        }




        // TODO: 21.09.2017 implement duration

        return task;
    }
}

