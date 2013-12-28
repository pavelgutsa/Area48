package com.omnixgroup.area48;

import com.omnixgroup.area48.TeamObjective.ObjectiveStatus;
import com.omnixgroup.area48.display.list.ListItemsActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayTargetObjectiveActivity extends Activity {

	ProgressDialog progressDialog;
	TeamObjective mTeamObjective;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.activity_display_target_objective);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.team_objective_title);
        
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		mTeamObjective = intent
				.getParcelableExtra(ListItemsActivity.OBJECTIVE_ITEM);
		
		// Set up the title bar
		if (mTeamObjective != null) {

			// Change the Activity's label
			TextView title = (TextView) findViewById(R.id.titleHeading);
			title.setText(mTeamObjective.getObjectiveName());
	       
			// Set activity description text
	        TextView description = (TextView) findViewById(R.id.objective_text);
	        description.setText(mTeamObjective.getObjectiveDescription());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_target_objective, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_report_objective_completion:
			reportObjectiveCompletion();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Report objective completion
	void reportObjectiveCompletion() {
		new AsyncTask<Integer, Integer, Boolean>() {
			@Override
			protected void onPreExecute() {
				/*
				 * This is executed on UI thread before doInBackground(). It is
				 * the perfect place to show the progress dialog.
				 */
				progressDialog = ProgressDialog.show(
						DisplayTargetObjectiveActivity.this, "", "Reporting objevtive completion...");
			}

			@Override
			protected Boolean doInBackground(Integer... params) {
				if (params == null) {
					return false;
				}
				try {
					/*
					 * This is run on a background thread, so we can sleep here
					 * or do whatever we want without blocking UI thread. A more
					 * advanced use would download chunks of fixed size and call
					 * publishProgress();
					 */
					Thread.sleep(3000);
					// HERE I'VE PUT ALL THE FUNCTIONS THAT WORK FOR ME
				} catch (Exception e) {
					Log.e("tag", e.getMessage());
					/*
					 * The task failed
					 */
					return false;
				}

				/*
				 * The task succeeded
				 */
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				progressDialog.dismiss();
				/*
				 * Update here your view objects with content from download. It
				 * is save to dismiss dialogs, update views, etc., since we are
				 * working on UI thread.
				 */
				if (result) {
					// Cross the title bar
					TextView title = (TextView) findViewById(R.id.titleHeading);
					title.setPaintFlags(title.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
					
					// Change the objective status
					mTeamObjective.setObjectiveStatus(TeamObjective.ObjectiveStatus.COMPLETED);
					
					// Return back to the objectives list
					onBackPressed();
				} else {
					AlertDialog.Builder b = new AlertDialog.Builder(
							DisplayTargetObjectiveActivity.this);
					b.setTitle(android.R.string.dialog_alert_title);
					b.setMessage("Communications failed, retry");
					b.setPositiveButton(getString(android.R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dlg, int arg1) {
									dlg.dismiss();
								}
							});
					b.create().show();
				}
				
			}
		}.execute(2000);
	}

}
