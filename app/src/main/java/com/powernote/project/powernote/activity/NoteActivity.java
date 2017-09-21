package com.powernote.project.powernote.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.R;
import com.powernote.project.powernote.adapter.ChecklistEditAdapter;
import com.powernote.project.powernote.model.ChecklistItem;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Note;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
	
	// For getting the right notes from the database
	private String noteFilter;
	
	// Storing the note object received from the database
	private Note note;
	private ListView lvChecklist;
	private LinearLayout layoutChecklist;
	private List<ChecklistItem> items;
	private Button buttonAddChecklistItem;
	private long contentType;
	private EditText title;
	private EditText text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.note );
		
		title = (EditText) findViewById( R.id.et_note_edit_title );
		text = (EditText) findViewById( R.id.et_note_edit_text );
		
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
			title.setText( note.getTitle() );
			text.setText( note.getDescription() );
		}
		
		//todo : fix TimeStamp and add this note to array of notes in PowerNotes
		
		initializeChecklist();
	}
	
	private void saveExistingNote() {
		Log.d("note", note.getTitle());
		note.setTitle( title.getText().toString() );
		note.setDescription( text.getText().toString() );
		getContentResolver().update( PowerNoteProvider.CONTENT_URI_NOTES, Methods.getNoteValues( note ), noteFilter, null );
		setResult( RESULT_OK );
		finish();
	}
	
	private void saveNewNote() {
		
		note.setTitle( title.getText().toString() );
		Log.d("note", note.getTitle());
		note.setDescription( text.getText().toString() );
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
			case R.id.action_take_photo:
				Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
				if(takePictureIntent.resolveActivity( getPackageManager() ) != null) {
					startActivityForResult( takePictureIntent, 1 );
				}
				break;
			case R.id.action_add_image:
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
		lvChecklist.setAdapter( adapter );
		
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
}
