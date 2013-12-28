package com.omnixgroup.area48.display.landing;

import com.omnixgroup.area48.R;
import com.omnixgroup.area48.display.edit.DisplayPlayingFieldActivity;
import com.omnixgroup.area48.display.list.ListItemsActivity;
import com.omnixgroup.area48.display.list.item.PlayingFieldListItem;
import com.omnixgroup.area48.persistence.data.PlayingField;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;

public class FieldsActivity extends ListItemsActivity<PlayingFieldListItem> {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,
				PlayingField.class,
				DisplayPlayingFieldActivity.class,
				R.layout.landing_activity_field);
		
		// Start loading fields list
		loadFieldsList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.field, menu);
		return true;
	}

	void loadFieldsList() {

		Log.i("LOADING", "Loading Playing Fields");
		
		// Show progress bar
		setContentView(R.layout.load_list_progress);

		// Start loading objectives list
		mProgress = (ProgressBar) findViewById(R.id.loading_progress_bar);

		// Start lengthy operation in a background thread
		new Thread(new Runnable() {
			public void run() {
				try {
					// Fill objectives array
					
					Thread.sleep(200);

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(25);
						}
					});
					
					mItemList.add(new PlayingFieldListItem(new PlayingField("Area48", "San Jose")));
					Thread.sleep(200);

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(50);
						}
					});

					mItemList.add(new PlayingFieldListItem(new PlayingField("Ivy Ridge", "Salinas")));
					Thread.sleep(200);

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(75);
						}
					});

					Thread.sleep(200);
					
					// Display objectives list
					mHandler.post(new Runnable() {
						public void run() {
							displayObjectivesList();
						}
					});

					Log.i("ARRAY", "" + mItemList.size());
				} catch (Exception e) {
					Log.e("BACKGROUND_PROC", e.getMessage());
				}
			}
		}).start();
	}
}
