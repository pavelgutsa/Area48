package com.omnixgroup.area48;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;

public class GameMapActivity extends Activity implements SensorEventListener {

	Float azimuth; // View to draw a compass

	CompassView mCompassView;
	PlayerMarkerView mPlayerMarkerView;
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_map);

		// Set map moving handler
		final TouchImageView mapView = (TouchImageView) this
				.findViewById(R.id.area_map);
		if(mapView!=null)
		{
			mapView.setMaxZoom(5f);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_map, menu);
		return true;
	}

	protected void onResume() {
		super.onResume();

		// Find compass view
		mCompassView = (CompassView) findViewById(R.id.compassView);
		mPlayerMarkerView = (PlayerMarkerView) findViewById(R.id.playerMarkerView);

		// Zero azimuth
		mCompassView.setBearing(0);
		mPlayerMarkerView.setBearing(0);
		mCompassView.invalidate();
		mPlayerMarkerView.invalidate();

		// Register sensor events
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	float[] mGravity;
	float[] mGeomagnetic;

	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values.clone();
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values.clone();
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuth = orientation[0]; // orientation contains: azimuth,
											// pitch
											// and roll]
				double rotation_degree = Math.toDegrees(azimuth);
				//double rotation_degree = azimuth * 360 / (2 * 3.14159f);
				
				switch(getWindowManager().getDefaultDisplay().getRotation()) {
				case Surface.ROTATION_0:
					//Log.i("COMPASS", "no rotation degree="+rotation_degree);
					break;
				case Surface.ROTATION_90:
					rotation_degree += 90;
					//Log.i("COMPASS", "90% rotation degree="+rotation_degree);
					break;
				case Surface.ROTATION_180:
					rotation_degree += 180;
					//Log.i("COMPASS", "180% rotation degree="+rotation_degree);
					break;
				case Surface.ROTATION_270:
					rotation_degree += 270;
					//Log.i("COMPASS", "270% rotation degree="+rotation_degree);
					break;
				}
				// Update the compass
				if (mCompassView != null) {
					//mCompassView.setBearing(azimuth);
					mCompassView.setBearing(rotation_degree);
					mCompassView.invalidate();
				}
				
				// Update the player marker
				if (mPlayerMarkerView != null) {
					//mPlayerMarkerView.setBearing(azimuth);
					mPlayerMarkerView.setBearing(rotation_degree);
					mPlayerMarkerView.invalidate();
				}
			}
		}
	}

	void showDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
		// Create the AlertDialog object and return it
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		switch(newConfig.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE:
			Log.i("ORIENTATION", "LANDSCAPE");
			break;
		case Configuration.ORIENTATION_PORTRAIT:
			Log.i("ORIENTATION", "PORTRAIT");
			break;
		}
	}
}
