package com.omnixgroup.area48;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CompassActivity extends Activity implements SensorEventListener {

	Float azimuth; // View to draw a compass

	public class CustomDrawableView extends View {
		Paint paint = new Paint();

		public CustomDrawableView(Context context) {
			super(context);
			paint.setColor(0xff00ff00);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			paint.setAntiAlias(true);
		};

		protected void onDraw(Canvas canvas) {
			int width = getWidth();
			int height = getHeight();
			int centerx = width / 2;
			int centery = height / 2;
			canvas.drawLine(centerx, 0, centerx, height, paint);
			canvas.drawLine(0, centery, width, centery, paint);
			// Rotate the canvas with the azimut
			if (azimuth != null)
				canvas.rotate(-azimuth * 360 / (2 * 3.14159f), centerx, centery);
			paint.setColor(0xff0000ff);
			canvas.drawLine(centerx, -1000, centerx, +1000, paint);
			canvas.drawLine(-1000, centery, 1000, centery, paint);
			canvas.drawText("N", centerx + 5, centery - 10, paint);
			canvas.drawText("S", centerx - 10, centery + 15, paint);
			paint.setColor(0xff00ff00);
		}
	}

	CompassView mCompassView;
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
	}

	protected void onResume() {
		super.onResume();
		
		// Find compass view
		mCompassView = (CompassView) findViewById(R.id.compassView);

		// Zero azimuth
		mCompassView.setBearing(0);
		mCompassView.invalidate();

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
				azimuth = orientation[0]; // orientation contains: azimuth, pitch
											// and roll]
				// Update the compass
				if(mCompassView != null)
				{
					//mCompassView.setBearing(Math.toDegrees(azimuth));
					mCompassView.setBearing(azimuth);
					mCompassView.invalidate();
				}
			}
		}
	}

	void showDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
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
}