package com.omnixgroup.area48.display.list;

import java.util.ArrayList;
import java.util.HashMap;

import com.omnixgroup.area48.DisplayLocationObjectiveActivity;
import com.omnixgroup.area48.DisplayTargetObjectiveActivity;
import com.omnixgroup.area48.R;
import com.omnixgroup.area48.R.drawable;
import com.omnixgroup.area48.R.id;
import com.omnixgroup.area48.R.layout;
import com.omnixgroup.area48.R.menu;
import com.omnixgroup.area48.R.string;
import com.omnixgroup.area48.display.list.item.ViewableItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListItemsActivity< ListItem extends ViewableItem > extends Activity implements
		AdapterView.OnItemClickListener {

	public static final String OBJECTIVE_ITEM = null;
	
	// Declare the UI components
	private ListView itemsListView;
	protected ProgressBar mProgress;
	protected Handler mHandler = new Handler();

	protected ArrayList<ListItem> mItemList = new ArrayList<ListItem>();
	
	private Class dataItemClass, listItemOnClickActivityClass;

	protected void onCreate(Bundle savedInstanceState, 
			Class<? extends Parcelable> dataItemClass,
			Class listItemOnClickActivityClass,
			int listItemActivityLayout) {

		super.onCreate(savedInstanceState);

		// Save object classes
		this.dataItemClass= dataItemClass;
		this.listItemOnClickActivityClass = listItemOnClickActivityClass;
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(listItemActivityLayout);

		// TODO: Need to fix custom window title
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
	}

	public void displayObjectivesList() {

		setContentView(R.layout.activity_list_objectives);

		// Initialize the UI components
		itemsListView = (ListView) findViewById(R.id.objectives_list);

		// Change the Activity's label
		this.setTitle(R.string.title_activity_list_objectives);

		// Create array adapter
		final ObjectivesArrayAdapter adapter = new ObjectivesArrayAdapter(this,
				R.layout.team_objectives_listitem, mItemList);
		itemsListView.setAdapter(adapter);

		// Set onClick listener
		itemsListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			int position, long id) {
		// Get item
		final ListItem listItem = (ListItem) parent.getItemAtPosition(position);

		// Remove the item
		// list.remove(item);

		// Refresh view
		// adapter.notifyDataSetChanged();

		// Start objective display activity
		Intent activityIntent = new Intent(this, listItemOnClickActivityClass);
		// Attach the objective to the intent
		activityIntent.putExtra(dataItemClass.toString(), listItem.getItemData());

		startActivity(activityIntent);
	}

	private class ObjectivesArrayAdapter extends ArrayAdapter<ListItem> {

		HashMap<ListItem, Integer> mIdMap = new HashMap<ListItem, Integer>();

		public ObjectivesArrayAdapter(Context context, int textViewResourceId,
				ArrayList<ListItem> objectives) {
			super(context, textViewResourceId, objectives);
			for (int i = 0; i < objectives.size(); ++i) {
				mIdMap.put(objectives.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			ListItem item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItem listItem = mItemList.get(position);
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(listItem.getItemTemplate(), null);
			}
			listItem.paintView(v);
			return v;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_objectives, menu);
		return true;
	}

}
