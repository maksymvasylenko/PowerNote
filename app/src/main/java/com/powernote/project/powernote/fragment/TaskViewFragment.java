package com.powernote.project.powernote.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.NonScrollListView;
import com.powernote.project.powernote.PowerNoteProvider;
import com.powernote.project.powernote.model.DBOpenHelper;
import com.powernote.project.powernote.model.Task;

import com.powernote.project.powernote.adapter.ChecklistViewAdapter;
import com.powernote.project.powernote.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class TaskViewFragment extends Fragment {
	private ChecklistViewAdapter adapter;
	private ListView lvCheckist;
	
	private LinearLayout layoutChecklist,layoutDeadline,
			layoutEffort, layoutImage, layoutDuration;
	
	private TextView tvTime, tvDate, tvDurationHours, tvDurationMinutes;

	private Button btnStartWorking;
	private ProgressBar pbDurationComplet;

	private View view;

	private Task task;

	private String noteFilter;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu( menu, inflater );
		// Inflate the options menu to show the available options
		inflater.inflate( R.menu.menu_task_view, menu );
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Press delete
			case R.id.action_delete:

				getActivity().getContentResolver().delete(PowerNoteProvider.CONTENT_URI_TASKS,
						noteFilter, null);

				getActivity().setResult(RESULT_OK);
				getActivity().finish();
				break;
			// Press edit
			case R.id.action_edit:
				// Define a new TaskEditFragment and pass in the bundle from the parent activity
				Fragment fragmentTaskEdit = new FragmentTaskEdit();
				fragmentTaskEdit.setArguments(getArguments());
				
				// Replace this fragment with an EditTaskFragment
				getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fl_activity_task_details_fragment_container, fragmentTaskEdit )
						// Add the fragment to the backStack to be popped later by the parent activity
						.addToBackStack( null ).commit();
				break;
			case R.id.action_change_color:
				changingColorDialog();
				break;
			default:
				return super.onOptionsItemSelected( item );
		}
		return super.onOptionsItemSelected( item );
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate( R.layout.task_view, container, false );
		
		lvCheckist = (ListView) view.findViewById( R.id.lv_checklist_view );
		
		final TextView title = (TextView) view.findViewById( R.id.tv_task_view_title );
		final TextView description = (TextView) view.findViewById( R.id.tv_task_view_description );
		final ProgressBar effort = (ProgressBar) view.findViewById( R.id.pb_effort );
		final ProgressBar priority = (ProgressBar) view.findViewById( R.id.pb_priority );
		
		tvDate = (TextView) view.findViewById( R.id.tv_task_view_deadline_date );
		tvTime = (TextView) view.findViewById( R.id.tv_task_view_deadline_time );


		//duration xml views
		tvDurationHours = (TextView) view.findViewById( R.id.tv_task_view_duration_hours );
		tvDurationMinutes = (TextView) view.findViewById( R.id.tv_task_view_duration_minutes );
		btnStartWorking = (Button) view.findViewById(R.id.bt_task_view_start_working);
		layoutDuration = (LinearLayout) view.findViewById( R.id.ll_task_view_duration );
		pbDurationComplet = (ProgressBar) view.findViewById(R.id.pb_duration_completed);

		
		layoutEffort = (LinearLayout) view.findViewById( R.id.ll_task_view_effort_priority );
		layoutDeadline = (LinearLayout) view.findViewById( R.id.ll_task_view_deadline );
		layoutChecklist = (LinearLayout) view.findViewById( R.id.layout_checklist );
        layoutImage = (LinearLayout) view.findViewById( R.id.layout_images );



        ImageView imageView = (ImageView) view.findViewById( R.id.image );
		
		if(getArguments() != null) {
			// Get the taskId that was passed in via the bundle and set the current task


			long id = getArguments().getLong(PowerNoteProvider.CONTENT_ITEM_TYPE);
			final Uri uri = Uri.parse(PowerNoteProvider.CONTENT_URI_TASKS + "/" + id);


			Cursor cursor = getActivity().getContentResolver().query(uri,
					DBOpenHelper.TASK_ALL_COLUMNS, null, null, null);
			cursor.moveToFirst();

			task = Methods.getNewTask(cursor);

			view.setBackgroundColor(task.getBackgroundColor());

			// If the task contains a checklist
			if(task.getCheckList() != null) {
				layoutChecklist.setVisibility( View.VISIBLE );
				final List items = task.getCheckList();

				adapter = new ChecklistViewAdapter( getContext(), R.layout.checklist_item_alt, items );
				lvCheckist.setAdapter( adapter );
			}

			// Fill views with their respective data from the task
			if(task.getTitle() != null) {
				title.setText( task.getTitle() );
			}
			if(task.getDescription() != null) {
				description.setText( task.getDescription() );
			}

			if(task.getEffort() != -1) {
				layoutEffort.setVisibility( View.VISIBLE );
				Log.e("View Task", " effort:" + task.getEffort());
				Log.e("View Task", " progress:" + task.getRank());
				effort.setProgress( task.getEffort() );
				priority.setProgress( task.getRank());
			}

			if(task.getDeadline() != -1) {
				layoutDeadline.setVisibility( View.VISIBLE );

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis( task.getDeadline() );

				int mYear = calendar.get( Calendar.YEAR );
				int mMonth = calendar.get( Calendar.MONTH );
				int mDay = calendar.get( Calendar.DAY_OF_MONTH );
				tvDate.setText( mDay + "-" + (mMonth + 1) + "-" + mYear );

				int hour = calendar.get( Calendar.HOUR_OF_DAY );
				int min = calendar.get( Calendar.MINUTE );
				tvTime.setText( hour + ":" + min );
			}

			if(task.getImagePath() != null && !task.getImagePath().isEmpty() ){
                Methods.setPic(task.getImagePath(), imageView, getActivity());
                layoutImage.setVisibility( View.VISIBLE );
            }

            if(task.getDuration() != -1){
				layoutDuration.setVisibility( View.VISIBLE );

				final long duration = task.getDuration();

				long hourConverted = TimeUnit.MILLISECONDS.toHours(duration);
				long minConverted = TimeUnit.MILLISECONDS.toMinutes(duration) -
						TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));

				tvDurationHours.setText(String.valueOf(hourConverted));
				tvDurationMinutes.setText(String.valueOf(minConverted));



				Log.e("spend millis",":"+ task.getSpend());
				long spendInPercentages= (task.getSpend()*100)/(duration);
				int spendInPercentagesInt = ((Number)spendInPercentages).intValue();
				pbDurationComplet.setProgress(spendInPercentagesInt);

				btnStartWorking.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						final Dialog dialog = new Dialog(getContext());
						dialog.setContentView(R.layout.start_working_stopwatch);
						dialog.setTitle("Working for ...");


						TextView tvSecondsLeft = (TextView) dialog.findViewById(R.id.tv_seconds_left);
						ProgressBar pbCompleted = (ProgressBar) dialog.findViewById(R.id.pb_stopwatch_dialog_duration_completed);
						Button btnStop = (Button) dialog.findViewById(R.id.btn_stopwatch_dialog_stop_working);

						tvSecondsLeft.setText("0 left");

						Log.e("millis left", "" + (task.getDuration() - task.getSpend()));
						final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(task.getDuration() - task.getSpend(), 1000, duration, pbCompleted, tvSecondsLeft);
						myCountDownTimer.start();

						btnStop.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								myCountDownTimer.cancel();

								saveSpendTime(myCountDownTimer.getMillisSpend());

								dialog.cancel();
							}
						});

						dialog.show();
					}
				});

			}

			//log
			LinearLayout logLayout = (LinearLayout) view.findViewById(R.id.ll_task_view_log_layout);
			logLayout.setVisibility(View.VISIBLE);
			ArrayList<String> str = new ArrayList<>();
			str.add("10.04.96 # task created");
			str.add("12.04.96 # task edited");
			str.add("12.04.96 # was working on task for 45 sec");
			str.add("12.04.96 # was working on task for 45 sec");
			ArrayAdapter<String> stringAdapter = new ArrayAdapter<>(getContext(), R.layout.log_list_item, str);
			NonScrollListView listView = (NonScrollListView) view.findViewById(R.id.lv_task_view_log);

			listView.setAdapter(stringAdapter);

		}

		return view;
	}

	public class MyCountDownTimer extends CountDownTimer {

		private long millisUntilFinished, millisInFuture, duration, spendBefore;
		private ProgressBar progressBar;
		private TextView seconds;



		public MyCountDownTimer(long millisInFuture, long countDownInterval, long duration, ProgressBar progressBar, TextView seconds) {
			super(millisInFuture, countDownInterval);
			this.millisInFuture = millisInFuture;
			this.progressBar = progressBar;
			this.seconds = seconds;
			this.duration = duration;
			this.spendBefore = this.duration - this.millisInFuture;
		}

		@Override
		public void onTick(long millisUntilFinished) {

			this.millisUntilFinished = millisUntilFinished;

			long spendMillis = duration - millisUntilFinished;
			long spendInPercentages = (spendMillis*100)/(duration);
			int spendInPercentagesInt = ((Number)spendInPercentages).intValue();

			seconds.setText(millisUntilFinished/1000 + " sec left");
			progressBar.setProgress(spendInPercentagesInt);
		}

		@Override
		public void onFinish() {
			seconds.setText("Congrats! you are dumbass");
			saveSpendTime(getMillisSpend());
		}

		public long getMillisSpend() {
			return spendBefore + (millisInFuture - millisUntilFinished);
		}
	}


	private void changingColorDialog(){
		final Dialog dialog = new Dialog(getContext());
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
				saveBackgroundColor(color);
				dialog.cancel();
			}
		});

	}

	private void saveBackgroundColor(int color){

		task.setBackgroundColor(color);
		view.setBackgroundColor(task.getBackgroundColor());

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.KEY_BACKGROUNDCOLOR, color);

		getActivity().getContentResolver().update(PowerNoteProvider.CONTENT_URI_TASKS, values,
				noteFilter, null);

		getActivity().setResult(RESULT_OK);
	}

	private void saveSpendTime(long millisSpend){

		task.setSpend(millisSpend);

		long spendInPercentages= (task.getSpend()*100)/(task.getDuration());
		int spendInPercentagesInt = ((Number)spendInPercentages).intValue();
		pbDurationComplet.setProgress(spendInPercentagesInt);

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.KEY_TASK_SPEND, millisSpend);

		getActivity().getContentResolver().update(PowerNoteProvider.CONTENT_URI_TASKS, values,
				noteFilter, null);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().finish();
	}
}
