package com.omnixgroup.area48;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.Toast;

public class GameMapActivity extends Activity implements SensorEventListener, LocationListener {

	Float azimuth; // View to draw a compass

	CompassView mCompassView;
	PlayerMarkerView mPlayerMarkerView;
	GameMapView mGameMapView;
	
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;
	
	private LocationManager mLocationManager; 
	Location mCurentLocation;
	
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_set_location:
			setMapLocation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Set location on the game map
	void setMapLocation() {
		mGameMapView.addLocation(mCurentLocation);
	}
	
	protected void onResume() {
		super.onResume();

		// Find compass view
		mCompassView = (CompassView) findViewById(R.id.compassView);
		//mPlayerMarkerView = (PlayerMarkerView) findViewById(R.id.playerMarkerView);
		mGameMapView = (GameMapView) findViewById(R.id.area_map);

		// Zero azimuth
		mCompassView.setBearing(0);
		mCompassView.invalidate();
		//mPlayerMarkerView.setBearing(0);
		//mPlayerMarkerView.invalidate();
		mGameMapView.setBearing(0);
		mGameMapView.invalidate();
		
		// Connect to SensorManager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// Register sensor events
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_UI);
		
		// Connect to LocationManager
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Get the last known location
		mCurentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		// Register the listener with the Location Manager to receive location updates
		/*mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 500, 10, this);
				//LocationManager.NETWORK_PROVIDER, 0, 0, this);
*/		mLocationManager.requestLocationUpdates(  
				LocationManager.GPS_PROVIDER, 5000, 10, this);
//				//LocationManager.GPS_PROVIDER, 0, 0, this);

		Log.v("IvyRidge", "Resumed");
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		mLocationManager.removeUpdates(this);
		
		Log.v("IvyRidge", "Paused");
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
				
				// Update the game map
				if (mGameMapView != null) {
					//mGameMapView.setBearing(azimuth);
					mGameMapView.setBearing(rotation_degree);
					mGameMapView.invalidate();
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
	
	/*
	 * Location Listener Events
	 */
	
	public void onLocationChanged(Location newLocation) {
	    // Disregard new location if not better than the last one
		/*if ( !isBetterLocation(newLocation, mCurentLocation))
			return;
			*/
		// Calculate movement between last and new location
		
		mCurentLocation = newLocation;
		
        /*-------to get City-Name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(newLocation.getLatitude(),
            		newLocation.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		String longitude = "Longitude: " + newLocation.getLongitude();
        Log.v("LOCATION CHANGED", longitude);
        String latitude = "Latitude: " + newLocation.getLatitude();
        Log.v("LOCATION CHANGED", latitude);
        String currentCity = "My Current City is: " + cityName;
        Log.v("LOCATION CHANGED", currentCity);
        		
        Toast.makeText(
                getBaseContext(), "Location changed: " + latitude + "\n"
                    + longitude + "\n" + currentCity, Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    	Log.i("LOCATION STATUS CHANGED", "provider="+provider+",status="+status+",bundle="+extras);
    }

    public void onProviderEnabled(String provider) {
    	Log.i("LOCATION PROVIDER ENABLED", "provider="+provider);
    }

    public void onProviderDisabled(String provider) {
    	Log.i("LOCATION PROVIDER DISABLED", "provider="+provider);
    }
    
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
      * @param location  The new Location that you want to evaluate
      * @param currentBestLocation  The current Location fix, to which you want to compare the new one
      */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
