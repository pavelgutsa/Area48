package com.omnixgroup.area48;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListObjectivesActivity extends Activity implements
		AdapterView.OnItemClickListener {

	public final static String OBJECTIVE_ITEM = "com.omnixgroup.area48.objective_text";

	// Declare the UI components
	private ListView objectivesListView;
	private ProgressBar mProgress;

	private Handler mHandler = new Handler();

	private ArrayList<TeamObjective> mTeamObjectives = new ArrayList<TeamObjective>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Start loading objectives list
		loadObjectivesList();
	}

	void loadObjectivesList() {

		Log.i("LOADING", "Loading Team Objectives");
		
		// Show progress bar
		setContentView(R.layout.load_objectives_progress);

		// Start loading objectives list
		mProgress = (ProgressBar) findViewById(R.id.objectives_loading_progress_bar);

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
					
					TeamObjective o1 = new TeamObjective();
					o1.setObjectiveName("Eliminate Camo Team Leader");
					o1.setObjectiveStatus(TeamObjective.ObjectiveStatus.PENDING);
					o1.setObjectiveType(TeamObjective.ObjectiveType.FIND_TARGET);
					o1.setObjectiveDescription("Camo Team Leader is armed and very dangerous.  Exercise extreme caution while approaching him.  Shoot him out and take a picture to prove that you did it.");
					
					mTeamObjectives.add(o1);
					Thread.sleep(200);

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(50);
						}
					});

					TeamObjective o2 = new TeamObjective();
					o2.setObjectiveName("Locate Camo Team");
					o2.setObjectiveStatus(TeamObjective.ObjectiveStatus.COMPLETED);
					o2.setObjectiveType(TeamObjective.ObjectiveType.FIND_LOCATION);
					o2.setObjectiveDescription("Our intelligence operatives have managed to hide a GPS transmitter on one of Camo Team members.  Use the map to locate the Camo Team.");
					mTeamObjectives.add(o2);
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

					Log.i("ARRAY", "" + mTeamObjectives.size());
				} catch (Exception e) {
					Log.e("BACKGROUND_PROC", e.getMessage());
				}
			}
		}).start();
	}

	void displayObjectivesList() {

		setContentView(R.layout.activity_list_objectives);

		// Initialize the UI components
		objectivesListView = (ListView) findViewById(R.id.objectives_list);

		// Change the Activity's label
		this.setTitle(R.string.title_activity_list_objectives);

		// Create array adapter
		final ObjectivesArrayAdapter adapter = new ObjectivesArrayAdapter(this,
				R.layout.team_objectives_listitem, mTeamObjectives);
		objectivesListView.setAdapter(adapter);

		// Set onClick listener
		objectivesListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			int position, long id) {
		// Get item
		final TeamObjective o = (TeamObjective) parent.getItemAtPosition(position);

		// Remove the item
		// list.remove(item);

		// Refresh view
		// adapter.notifyDataSetChanged();

		// Start objective display activity
		Intent intent;
		switch(o.getObjectiveType()) {
		case FIND_LOCATION:
			intent = new Intent(this, DisplayLocationObjectiveActivity.class);
			break;
		case FIND_TARGET:
			intent = new Intent(this, DisplayTargetObjectiveActivity.class);
			break;
		default:
			return;
		}	
		
		// Attach the objective to the intent
		intent.putExtra(OBJECTIVE_ITEM, o);

		startActivity(intent);
	}

	private class ObjectivesArrayAdapter extends ArrayAdapter<TeamObjective> {

		HashMap<TeamObjective, Integer> mIdMap = new HashMap<TeamObjective, Integer>();

		public ObjectivesArrayAdapter(Context context, int textViewResourceId,
				ArrayList<TeamObjective> objectives) {
			super(context, textViewResourceId, objectives);
			for (int i = 0; i < objectives.size(); ++i) {
				mIdMap.put(objectives.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			TeamObjective item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.team_objectives_listitem, null);
			}
			TeamObjective o = mTeamObjectives.get(position);
			if (o != null) {
				// Set objectivev icon
				ImageView icon = (ImageView) v.findViewById(R.id.icon);
				if (icon != null) {
					switch(o.getObjectiveType()) {
					case FIND_LOCATION:
						icon.setImageResource(R.drawable.ic_map_objective);
						break;
					case FIND_TARGET:
						icon.setImageResource(R.drawable.ic_target_objective);
						break;
					}					
				}
				
				// Set objective name
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				if (tt != null) {
					tt.setText(o.getObjectiveName());
				}
				
				// Set objective status
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (bt != null) {
					switch(o.getObjectiveStatus()) {
					case PENDING:
						bt.setText(R.string.objective_status_pending);
						bt.setPaintFlags(bt.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
						break;
					case COMPLETED:
						bt.setText(R.string.objective_status_completed);
						bt.setPaintFlags(bt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
						break;
					}	
					
				}
			}
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
