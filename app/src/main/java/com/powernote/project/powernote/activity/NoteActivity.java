package com.powernote.project.powernote.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.adapter.ChecklistEditAdapter;
import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {
	
	// For getting the right notes from the database
	private String noteFilter;
	
	// Storing the note object received from the database
	private Note note;
	private ListView lvChecklist;
	private LinearLayout layoutChecklist, layoutImages;
	private List<ChecklistItem> items;
	private Button buttonAddChecklistItem;
	private long contentType;
	private EditText title;
	private EditText text;
	private ImageView imageView;
	private View rootLayout;


	//variables for taking photo
	static final int REQUEST_TAKE_PHOTO = 12364, REQUEST_ADD_PHOTO = 26513;

	private String imagePath = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView(R.layout.note);
		
		title = (EditText) findViewById(R.id.et_note_edit_title);
		text = (EditText) findViewById(R.id.et_note_edit_text);
		rootLayout = text.getRootView();

		imageView = (ImageView) findViewById(R.id.image);
		layoutImages = (LinearLayout) findViewById(R.id.layout_images);



		// Get the intent from the parent class, and get the content type
		Intent intent = getIntent();
		contentType = intent.getLongExtra( PowerNoteProvider.CONTENT_ITEM_TYPE, -1 );
		
		if (contentType == -1){
			note = new Note();
		} else {
			Uri uri = Uri.parse( PowerNoteProvider.CONTENT_URI_NOTES + "/" + contentType );
			noteFilter = DBOpenHelper.KEY_ID + "=" + uri.getLastPathSegment();
			Cursor cursor = getContentResolver().query( uri, DBOpenHelper.NOTE_ALL_COLUMNS, noteFilter, null, null );
			cursor.moveToFirst();
			note = Methods.getNewNote( cursor );

			if(note.getImagePath() != null && !note.getImagePath().isEmpty() ){
				Methods.setPic(note.getImagePath(), imageView, this);
				layoutImages.setVisibility( View.VISIBLE );
			}

			rootLayout.setBackgroundColor(note.getBackgroundColor());
			title.setText( note.getTitle() );
			text.setText( note.getDescription() );

		}

		
		initializeChecklist();
	}
	
	private void saveExistingNote() {
		Log.d("note", note.getTitle());
		note.setTitle( title.getText().toString() );
		note.setDescription( text.getText().toString() );
		note.setImagePath(imagePath);
		getContentResolver().update( PowerNoteProvider.CONTENT_URI_NOTES, Methods.getNoteValues( note ), noteFilter, null );
		setResult( RESULT_OK );
		finish();
	}
	
	private void saveNewNote() {
		
		note.setTitle( title.getText().toString() );
		Log.d("note", note.getTitle());
		note.setDescription( text.getText().toString() );
		note.setImagePath(imagePath);
		getContentResolver().insert( PowerNoteProvider.CONTENT_URI_NOTES, Methods.getNoteValues( note ) );
		setResult( RESULT_OK );
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate( R.menu.menu_note, menu );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save:
				if (contentType == -1){
					saveNewNote();
				} else {
					saveExistingNote();
				}
				break;
			case R.id.action_delete:
				getContentResolver().delete( PowerNoteProvider.CONTENT_URI_NOTES, noteFilter, null );
				setResult( RESULT_OK );
				finish();
				break;
			case R.id.action_change_color:
				changingColorDialog();
				break;
			case R.id.action_take_photo:
				dispatchTakePictureIntent();
				break;
			case R.id.action_add_image:
				addImageFromGallery();
				break;
			case R.id.action_record:
				break;
			case R.id.action_add_checklist:
				break;
			default:
				return super.onOptionsItemSelected( item );
		}
		return super.onOptionsItemSelected( item );
	}
	
	private void initializeChecklist() {
		// Use the layout to set the checklist visibility
		//        layoutChecklist = (LinearLayout) findViewById(R.id.layout_checklist);
		
		// Find listView
		lvChecklist = (ListView) findViewById( R.id.lv_checklist_edit );
		final EditText etCheckListInput = (EditText) findViewById( R.id.et_checklist_input );
		
		// Init items array
		items = note.getCheckList();
		
		// Define and set adapter for checklist listView
		final ChecklistEditAdapter adapter = new ChecklistEditAdapter( getApplicationContext(), R.layout.checklist_item_alt, items );
		//lvChecklist.setAdapter( adapter );
		
		buttonAddChecklistItem = (Button) findViewById( R.id.bt_add_checklist_item );
		
		buttonAddChecklistItem.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String inputText = etCheckListInput.getText().toString();
				if(inputText.isEmpty()) {
					inputText = "Empty";
				}
				items.add( new ChecklistItem( inputText, false ) );
				etCheckListInput.setText( "" );
				adapter.notifyDataSetChanged();
			}
		} );
	}







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
				Log.e("dispatch photo ", "" + photoFile.getAbsolutePath());
				Uri photoURI = FileProvider.getUriForFile(this,
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
			Methods.setPic(imagePath, imageView, this);



			Log.e("setted picture", "");
			layoutImages.setVisibility(View.VISIBLE);
		}else if(requestCode == REQUEST_ADD_PHOTO && resultCode == RESULT_OK){
			Log.e("add gallery picture", "");


			Uri selectedImage = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			Methods.setPic(picturePath, imageView, this);
			imagePath = picturePath;
			layoutImages.setVisibility(View.VISIBLE);
		}
		Log.e("fragment task edit ", "code:" + requestCode);

	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: imagePath for use with ACTION_VIEW intents
		note.setImagePath(image.getAbsolutePath());

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


	private void changingColorDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.choose_color_dialog);
		dialog.setTitle("Choose Color");

		dialog.show();

		Button btnGreen = (Button) dialog.findViewById(R.id.colorGreenButton);
		Button btnRed = (Button) dialog.findViewById(R.id.colorRedrButton);
		Button btnPurple = (Button) dialog.findViewById(R.id.colorPurpleButton);

		Button btnBlue = (Button) dialog.findViewById(R.id.colorBlueButton);
		Button btnDarkBlue = (Button) dialog.findViewById(R.id.colorDarkBlueButton);
		Button btnOrange = (Button) dialog.findViewById(R.id.colorOrangeButton);

		Button btnYellow = (Button) dialog.findViewById(R.id.colorYellowButton);
		Button btnPink = (Button) dialog.findViewById(R.id.colorPinkButton);
		Button btnWhite = (Button) dialog.findViewById(R.id.colorWhiteButton);


		setColorButton(getResources().getColor(R.color.colorPurple), btnPurple, dialog);
		setColorButton(getResources().getColor(R.color.colorRed), btnRed, dialog);
		setColorButton(getResources().getColor(R.color.colorGreen), btnGreen, dialog);

		setColorButton(getResources().getColor(R.color.colorBlue), btnBlue, dialog);
		setColorButton(getResources().getColor(R.color.colorDarkBlue), btnDarkBlue, dialog);
		setColorButton(getResources().getColor(R.color.colorOrange), btnOrange, dialog);

		setColorButton(getResources().getColor(R.color.colorYellow), btnYellow, dialog);
		setColorButton(getResources().getColor(R.color.colorPink), btnPink, dialog);
		setColorButton(getResources().getColor(R.color.colorWhite), btnWhite, dialog);


	}

	private void setColorButton(final int color, Button btn, final Dialog dialog){
		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM,
				new int[]{color,color});
		gd.setCornerRadius(100f);

		btn.setBackgroundDrawable(gd);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				note.setBackgroundColor(color);
				rootLayout.setBackgroundColor(note.getBackgroundColor());
				Log.e("color", "" + color);
				dialog.cancel();
			}
		});

	}

}
