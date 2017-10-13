package com.powernote.project.powernote;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Note;
import com.powernote.project.powernote.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maks on 21.09.2017.
 */

public class Methods {

    private static String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";
    private static String ARRAY_DIVIDER_SECOND = "#d2isdi9dcvra2r2ra5y";

    //might need these methods



    //callback for pre sort in search activity and TagListFragment/ColorListFragment
    public interface OnPresortSelectedListener {
        void onColorSelected(int color);
        void onTagSelected(long tagId);
    }


    public static void setPic(String imagePath, ImageView imageView, Activity activity) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;



        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/width, photoH/height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


    static public ContentValues getTaskValues(Task task){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_NAME, task.getTitle());
        values.put(DBOpenHelper.KEY_DESCRIPTION, task.getDescription());
        values.put(DBOpenHelper.KEY_TASK_DEADLINE, task.getDeadline());
        values.put(DBOpenHelper.KEY_TASK_RANK, task.getRank());
        values.put(DBOpenHelper.KEY_TASK_DURATION, task.getDuration());
        values.put(DBOpenHelper.KEY_CREATED_AT, task.getCreatedAt().getTimeInMillis());// maybe getter should already return in millis
        values.put(DBOpenHelper.KEY_TASK_EFFORT, task.getEffort());
        values.put(DBOpenHelper.KEY_IMAGE_PATH, task.getImagePath());
        values.put(DBOpenHelper.KEY_CHECKLIST, serializeChecklist(task.getCheckList()));
        values.put(DBOpenHelper.KEY_BACKGROUNDCOLOR, task.getBackgroundColor());
        values.put(DBOpenHelper.KEY_TASK_SPEND, task.getSpend());

        return values;
    }

    static public ContentValues getNoteValues(Note note){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.KEY_NAME, note.getTitle());
        values.put(DBOpenHelper.KEY_DESCRIPTION, note.getDescription());
        values.put(DBOpenHelper.KEY_CREATED_AT, note.getCreatedAt().getTimeInMillis());
        values.put(DBOpenHelper.KEY_CHECKLIST, serializeChecklist(note.getCheckList()));
        values.put(DBOpenHelper.KEY_IMAGE_PATH, note.getImagePath());
        values.put(DBOpenHelper.KEY_BACKGROUNDCOLOR, note.getBackgroundColor());
        return values;
    }

    static public Task getNewTask(Cursor c){
        Task task = new Task(c.getInt(c.getColumnIndex(DBOpenHelper.KEY_ID)),
                c.getInt(c.getColumnIndex(DBOpenHelper.KEY_TASK_RANK)),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_NAME)),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_DESCRIPTION)),
                c.getLong(c.getColumnIndex(DBOpenHelper.KEY_TASK_DEADLINE)),
                c.getLong(c.getColumnIndex(DBOpenHelper.KEY_CREATED_AT)),
                c.getLong(c.getColumnIndex(DBOpenHelper.KEY_TASK_DURATION)),
                c.getInt(c.getColumnIndex(DBOpenHelper.KEY_TASK_EFFORT)),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_IMAGE_PATH)),
                deserializeChecklist(c.getString(c.getColumnIndex(DBOpenHelper.KEY_CHECKLIST))),
                c.getInt(c.getColumnIndex(DBOpenHelper.KEY_BACKGROUNDCOLOR)),
                c.getLong(c.getColumnIndex(DBOpenHelper.KEY_TASK_SPEND)));
        return task;
    }

    static public Note getNewNote(Cursor c){
        Note note = new Note(c.getInt(c.getColumnIndex(DBOpenHelper.KEY_ID)),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_DESCRIPTION)),
                c.getLong(c.getColumnIndex(DBOpenHelper.KEY_CREATED_AT)),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_NAME)),
                deserializeChecklist(c.getString(c.getColumnIndex(DBOpenHelper.KEY_CHECKLIST))),
                c.getString(c.getColumnIndex(DBOpenHelper.KEY_IMAGE_PATH)),
                c.getInt(c.getColumnIndex(DBOpenHelper.KEY_BACKGROUNDCOLOR)));
        return note;
    }


    static private long getDateTime() {
        return System.currentTimeMillis();
    }

    /**
     * Serialize a checklist to allow for storage in the database
     * @param items list of checkItems
     * @return Serialized string of checklist items
     */
    private static String serializeChecklist(List<ChecklistItem> items){
        if(items != null) {
            List<String> content = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                String a = items.get(i).getText() + ARRAY_DIVIDER_SECOND + String.valueOf(items.get(i).isChecked());
                content.add(a);
            }
            return TextUtils.join(ARRAY_DIVIDER, content);
        }
        return "";
    }

    /**
     * Deserialize a checklist for retrieval from the database
     * @param content Serialized string of a checkItem list
     * @return List of deserialized checkItems
     */
    static private List<ChecklistItem> deserializeChecklist(String content){
        if (content != null && !content.isEmpty()) {
            String[] c = content.split(ARRAY_DIVIDER);
            List<ChecklistItem> items = new ArrayList<>();
            for (String aC : c) {
                String[] stringItem = aC.split(ARRAY_DIVIDER_SECOND);
                items.add(new ChecklistItem(stringItem[0], Boolean.valueOf(stringItem[1])));
            }
            return items;
        }
        return null;
    }

}
