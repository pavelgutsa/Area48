package com.omnixgroup.area48;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.util.AttributeSet;
import android.util.Log;
import android.content.res.Resources;

public class PlayerMarkerView extends View {

	public PlayerMarkerView(Context context) {
		super(context);
		initPlayerMarkerView();
	}

	public PlayerMarkerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPlayerMarkerView();
	}

	public PlayerMarkerView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		initPlayerMarkerView();
	}

	private double bearing;

	public void setBearing(double _bearing) {
		bearing = _bearing;
	}

	public double getBearing() {
		return bearing;
	}
	
	private Paint playerMarkerPaint;	
	private Paint playerMarkerArrowPaint;
	private Path playerMarkerArrow = new Path();
	private int playerMarkerRadius;
	
	protected void initPlayerMarkerView() {
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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int px = getMeasuredWidth() / 2;
		int py = getMeasuredHeight() / 2;
		
		// Draw the background
		canvas.drawCircle(px, py, playerMarkerRadius, playerMarkerPaint);
		//Log.i("PLAYER_MARKER", "px="+px+",py="+py+",radius="+playerMarkerRadius);
			
		// Save canvas state before painting player marker arrow
		canvas.save();
		
		// Rotate the canvas so the top is matching the current bearing
		canvas.rotate((float)bearing, px, py);
		//canvas.rotate(-(float)bearing * 360 / (2 * 3.14159f), px, py);
		
		// Draw the needle
		//canvas.drawLine(px, py - radius - spacingY/2, px, py + radius + spacingY/2, markerPaint);
		
		// Set path points for needle top
		playerMarkerArrow.reset(); 
		playerMarkerArrow.moveTo(px - playerMarkerRadius, (float) (py - 0.88*playerMarkerRadius)); 
		playerMarkerArrow.lineTo(px, (float) (py - 1.77*playerMarkerRadius));
		playerMarkerArrow.lineTo(px + playerMarkerRadius, (float) (py - 0.88*playerMarkerRadius));
		
		// Draw needle top
		canvas.drawPath(playerMarkerArrow, playerMarkerArrowPaint);
		
		// Restore to the 'top' state after painting player marker arrow
		canvas.restore();
	}
}
