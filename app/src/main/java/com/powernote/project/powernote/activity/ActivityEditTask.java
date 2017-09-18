package com.powernote.project.powernote.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.powernote.project.powernote.model.PowerNote;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.model.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maks on 11.09.2017.
 */

public class ActivityEditTask  extends AppCompatActivity{

    private boolean zoomOut =  false;
    private PowerNote pwn = PowerNote.getInstance();
    private Button date, time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView tvDate, tvTime;
    private Context ctx;
    private long value;
    private ImageView imageView;
    private Task currentTask;

    private long selectedTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);
        ctx = this;

        final EditText title = (EditText) findViewById(R.id.et_task_edit_title);
        final EditText description = (EditText) findViewById(R.id.et_task_edit_description);
        final SeekBar effort = (SeekBar) findViewById(R.id.sb_task_edit_effort);
        final SeekBar priority = (SeekBar) findViewById(R.id.sb_task_edit_priority);
        Button saveButton = (Button) findViewById(R.id.bt_task_edit_save);

        imageView = (ImageView) findViewById(R.id.image);

        date = (Button) findViewById(R.id.bt_task_edit_date);
        time = (Button) findViewById(R.id.bt_task_edit_time);

        tvDate = (TextView) findViewById(R.id.tv_task_edit_date);
        tvTime = (TextView) findViewById(R.id.tv_task_edit_time);


        Intent intent = getIntent();
        value = intent.getLongExtra("taskDatabaseID", -1);
        Log.e("taskDatabaseID", "task" + value);


        if(value != -1) {
            currentTask = pwn.getTask(value);
            title.setText(currentTask.getTitle());
            description.setText(currentTask.getDescription());

            effort.setProgress(currentTask.getEffort());
            priority.setProgress(currentTask.getRank());
            saveButton.setText("Update");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTask.getDeadline());

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            tvDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            tvTime.setText(hour + ":" + min);


            if(currentTask.getImagePath() != null) {
                imageView.setImageURI(Uri.parse(currentTask.getImagePath()));
            }
            imageView.setImageURI(Uri.parse("/storage/emulated/0/Android/data/com.powernote.project.powernote/files/Pictures/JPEG_20170913_225111_98265392.jpg"));

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentTask.setRank(priority.getProgress());
                    currentTask.setTitle(title.getText().toString());
                    currentTask.setDescription(description.getText().toString());
                    currentTask.setEffort(effort.getProgress());
                    currentTask.setDeadline(getTimeInMillisends());

                    pwn.addTask(currentTask);
                    Log.e("test 1", "" + pwn.getTask(value).getTitle() );
                    Snackbar.make(v, "Task updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finish();
                }
            });
        }else{
            imageView.setImageURI(Uri.parse("/storage/emulated/0/Android/data/com.powernote.project.powernote/files/Pictures/JPEG_20170913_225111_98265392.jpg"));


            currentTask = new Task();
            Calendar calendar = Calendar.getInstance();

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            tvDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            tvTime.setText(hour + ":" + min);



            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentTask.setRank(priority.getProgress());
                    currentTask.setTitle(title.getText().toString());
                    currentTask.setDescription(description.getText().toString());
                    currentTask.setEffort(effort.getProgress());
                    currentTask.setDeadline(getTimeInMillisends());

                    pwn.addTask(currentTask);

                    Snackbar.make(v, "Task Created", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    finish();
                }
            });
        }


        imageView .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zoomOut) {
                    Toast.makeText(getApplicationContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setAdjustViewBounds(true);
                    zoomOut =false;
                }else{
                    Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut = true;
                }
            }
        });



        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
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
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
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
    }

    private long getTimeInMillisends(){

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete:
                pwn.getTasks().remove(value);
                PowerNote.getInstance().getDB().deleteTask(value);
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

    static final int REQUEST_TAKE_PHOTO = 1;
    Uri photoURI;

    private void dispatchTakePictureIntent() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.powernote.project.powernote.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
