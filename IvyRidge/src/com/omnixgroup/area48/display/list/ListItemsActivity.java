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

public class ListItemsActivity< ListItem extends ViewableItem > extends Activity /*implements
		AdapterView.OnItemClickListener*/ {

	public static final String OBJECTIVE_ITEM = null;
	
	// Declare the UI components
	protected ListView itemsListView;
	protected ProgressBar mProgress;
	protected Handler mHandler = new Handler();

	protected ArrayList<ListItem> mItemList = new ArrayList<ListItem>();
	protected ItemsListArrayAdapter mItemsListArrayAdapter = null;
	
	private Class<? extends Parcelable> dataItemClass;
	private Class<? extends Activity> listItemOnClickActivityClass;

	protected void onCreate(Bundle savedInstanceState, 
			Class<? extends Parcelable> dataItemClass,
			Class<? extends Activity> listItemOnClickActivityClass,
			int listItemActivityLayout) {

		super.onCreate(savedInstanceState);

		// Save object classes
		this.dataItemClass = dataItemClass;
		this.listItemOnClickActivityClass = listItemOnClickActivityClass;
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(listItemActivityLayout);

		// Set custom window title
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
	}

	/*
	public void displayList() {

		setContentView(R.layout.activity_list_items);

		// Initialize the UI components
		itemsListView = (ListView) findViewById(R.id.items_list);

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
*/
	public class ItemsListArrayAdapter extends ArrayAdapter<ListItem> {

		HashMap<ListItem, Integer> mIdMap = new HashMap<ListItem, Integer>();

		public ItemsListArrayAdapter(Context context, int textViewResourceId,
				ArrayList<ListItem> items_list) {
			super(context, textViewResourceId, items_list);
			for (int i = 0; i < items_list.size(); ++i) {
				mIdMap.put(items_list.get(i), i);
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
				Log.i("ItemsListArrayAdapter", "Inflating template "+listItem.getItemTemplate());
				v = vi.inflate(listItem.getItemTemplate(), null);
			}
			listItem.paintView(v);
			return v;
		}
	}
}
