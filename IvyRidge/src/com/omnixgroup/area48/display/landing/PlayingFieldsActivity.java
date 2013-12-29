package com.omnixgroup.area48.display.landing;

import com.omnixgroup.area48.R;
import com.omnixgroup.area48.display.edit.DisplayPlayingFieldActivity;
import com.omnixgroup.area48.display.list.ListItemsActivity;
import com.omnixgroup.area48.display.list.item.PlayingFieldListItem;
import com.omnixgroup.area48.persistence.data.PlayingField;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class PlayingFieldsActivity extends ListItemsActivity<PlayingFieldListItem>
implements AdapterView.OnItemClickListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,
				PlayingField.class,
				DisplayPlayingFieldActivity.class,
				R.layout.landing_activity_field);
		
		// Change the Activity's label
		this.setTitle(R.string.title_activity_playing_fields);
		
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
							displayList();
						}
					});

					Log.i("ARRAY", "" + mItemList.size());
				} catch (Exception e) {
					Log.e("BACKGROUND_PROC", e.getMessage());
				}
			}
		}).start();
	}
	
	public void displayList() {

		setContentView(R.layout.activity_list_items);

		// Initialize the UI components
		itemsListView = (ListView) findViewById(R.id.items_list);

		// Create array adapter
		Log.i("PlayingFieldsActivity", "Creating ItemsListArrayAdapter with template "+R.layout.playing_field_listitem);
		mItemsListArrayAdapter = new ItemsListArrayAdapter(this,
				R.layout.playing_field_listitem, mItemList);

		// Set array adapter
		itemsListView.setAdapter(mItemsListArrayAdapter);

		// Set onClick listener
		itemsListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			int position, long id) {
		// Get item
		Log.i("ListItemsActivity", "Getting item at position "+position+",parent="+parent+",id="+id);
		final PlayingFieldListItem listItem = (PlayingFieldListItem) parent.getItemAtPosition(position);

		// Remove the item
		// list.remove(item);

		// Refresh view
		// adapter.notifyDataSetChanged();

		// Start objective display activity
		Intent activityIntent = new Intent(this, DisplayPlayingFieldActivity.class);
		// Attach the objective to the intent
		activityIntent.putExtra(PlayingField.class.toString(), listItem.getItemData());

		startActivity(activityIntent);
	}
}
