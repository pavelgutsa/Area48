/*
 * GameMapView.java
 * By: Michael Ortiz
 * Updated By: Patrick Lackemacher
 * Updated By: Babay88
 * -------------------
 * Extends Michael Ortiz's TouchImageView to include game locations.
 */

package com.omnixgroup.area48;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

public class GameMapView extends TouchImageView {
	
	public GameMapView(Context context) {
		super(context);
	}

	public GameMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void addLocation(Location location) {
		String message = "Longitude: " + location.getLongitude() +
				", Latitude: " + location.getLatitude();
        Log.v("GAME LOCATION SET", message);

        Toast.makeText(
                getContext(), "Location set: " + message, Toast.LENGTH_SHORT).show();
		
	}
}