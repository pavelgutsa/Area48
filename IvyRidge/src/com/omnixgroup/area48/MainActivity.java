package com.omnixgroup.area48;

import com.omnixgroup.area48.display.landing.FieldsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Start objective display activity
		Intent intent;
		
		switch (item.getItemId()) {
		case R.id.action_field:
			intent = new Intent(this, FieldsActivity.class);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		startActivity(intent);
		return true;
	}
}
