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
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

public class GameMapView extends TouchImageView //implements View.OnTouchListener 
{	
	public GameMapView(Context context) {
		super(context);
		initPlayerMarker();
	}

	public GameMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPlayerMarker();
	}
	
	private double bearing;

	public void setBearing(double _bearing) {
		bearing = _bearing;
	}

	public double getBearing() {
		return bearing;
	}
	
	private float mPlayerMarkerX;
	private float mPlayerMarkerY;
	
	private Paint playerMarkerPaint;	
	private Paint playerMarkerArrowPaint;
	private Path playerMarkerArrow = new Path();
	private int playerMarkerRadius;
	
	protected void initPlayerMarker() {
		setFocusable(true);

		Resources r = this.getResources();

		playerMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerMarkerPaint.setColor(r.getColor(R.color.player_marker_color));
		playerMarkerPaint.setStrokeWidth(2);
		playerMarkerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		playerMarkerArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		playerMarkerArrowPaint.setColor(r.getColor(R.color.player_marker_color));
		playerMarkerArrowPaint.setStrokeWidth(2);
		playerMarkerArrowPaint.setStyle(Paint.Style.STROKE);
		
		playerMarkerRadius = r.getInteger(R.integer.player_marker_radius);
		
		// Init player marker position
		//mPlayerMarkerX = getMeasuredWidth() / 2;
		//mPlayerMarkerY = getMeasuredHeight() / 2;
		mPlayerMarkerX = 100;
		mPlayerMarkerY = 100;
	}
	
	/*public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v, event);
	}*/
	
	@Override
	protected void onDraw(Canvas canvas) {
		// Draw map
		super.onDraw(canvas);
		
		// Draw player marker
		drawPlayerMarker(canvas);
	}
	
	// Draw player marker
	void drawPlayerMarker(Canvas canvas)
	{	
		RectF r = new RectF(); 
		matrix.mapRect(r);
		Log.i("IMAGE_VIEW", "Rect " + r.left + " " + r.top + " " + r.right + " " + r.bottom + " " + saveScale + " ");
		
		/*float scaledX =  mPlayerMarkerX * saveScale;
		float scaledY =  mPlayerMarkerY * saveScale;
		
		Log.i("PLAYER_MARKER", "Scaled xy: " + scaledX + " " + scaledY);
		*/
		float newX =  mPlayerMarkerX * saveScale + r.left;
		float newY =  mPlayerMarkerY * saveScale + r.top;
		
		Log.i("PLAYER_MARKER", "New xy: " + newX + " " + newY);
		
		//mPlayerMarkerX = scaledX;
		//mPlayerMarkerY = scaledY;
		
		// Draw the background
		//canvas.drawCircle(mPlayerMarkerX, mPlayerMarkerY, playerMarkerRadius, playerMarkerPaint);
		canvas.drawCircle(newX, newY, playerMarkerRadius, playerMarkerPaint);
		//Log.i("PLAYER_MARKER", "mPlayerMarkerX="+mPlayerMarkerX+",mPlayerMarkerY="+mPlayerMarkerY+",radius="+playerMarkerRadius);
			
		// Save canvas state before painting player marker arrow
		canvas.save();
		
		// Rotate the canvas so the top is matching the current bearing
		canvas.rotate((float)bearing, newX, newY);
		//canvas.rotate(-(float)bearing * 360 / (2 * 3.14159f), px, mPlayerMarkerY);
		
		// Draw the needle
		//canvas.drawLine(px, mPlayerMarkerY - radius - spacingY/2, px, mPlayerMarkerY + radius + spacingY/2, markerPaint);
		
		// Set path points for needle top
		playerMarkerArrow.reset(); 
		playerMarkerArrow.moveTo(newX - playerMarkerRadius, (float) (newY - 0.88*playerMarkerRadius)); 
		playerMarkerArrow.lineTo(newX, (float) (newY - 1.77*playerMarkerRadius));
		playerMarkerArrow.lineTo(newX + playerMarkerRadius, (float) (newY - 0.88*playerMarkerRadius));
		
		// Draw needle top
		canvas.drawPath(playerMarkerArrow, playerMarkerArrowPaint);
		
		// Restore to the 'top' state after painting player marker arrow
		canvas.restore();
	}

	public void addLocation(Location location) {
		String message = "Longitude: " + location.getLongitude() +
				", Latitude: " + location.getLatitude();
        Log.v("GAME LOCATION SET", message);

        Toast.makeText(
                getContext(), "Location set: " + message, Toast.LENGTH_SHORT).show();
		
	}
}