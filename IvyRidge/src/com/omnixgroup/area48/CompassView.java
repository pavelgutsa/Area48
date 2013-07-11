package com.omnixgroup.area48;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.util.AttributeSet;
import android.util.Log;
import android.content.res.Resources;

public class CompassView extends View {

	public CompassView(Context context) {
		super(context);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet ats, int defaultStyle) {
		super(context, ats, defaultStyle);
		initCompassView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// The compass is a circle that fills as much space as possible.
		// Set the measured dimensions by figuring out the shortest boundary,
		// height or width.
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		int d = Math.min(measuredWidth, measuredHeight);
		setMeasuredDimension(d, d);
	}

	private int measure(int measureSpec) {
		int result = 0;
		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}

	private double bearing;

	public void setBearing(double _bearing) {
		bearing = _bearing;
	}

	public double getBearing() {
		return bearing;
	}

	private Paint markerPaint;
	private Paint textPaint;
	private Paint circlePaint;
	private Paint needlePaintNorth;
	private Paint needlePaintSouth;
	private String northString;
	private String eastString;
	private String southString;
	private String westString;
	
	private Path compassNeedleTopPath = new Path();
	private Path compassNeedleBottomPath = new Path();
	
	protected void initCompassView() {
		setFocusable(true);

		Resources r = this.getResources();

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.background_color));
		circlePaint.setStrokeWidth(2);
		circlePaint.setStyle(Paint.Style.STROKE);
		
		northString = r.getString(R.string.cardinal_north);
		eastString = r.getString(R.string.cardinal_east);
		southString = r.getString(R.string.cardinal_south);
		westString = r.getString(R.string.cardinal_west);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(R.color.text_color));
		
		markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markerPaint.setColor(r.getColor(R.color.marker_color));
		markerPaint.setStrokeWidth(2);
		markerPaint.setStyle(Paint.Style.STROKE);
		
		needlePaintNorth = new Paint(Paint.ANTI_ALIAS_FLAG);
		needlePaintNorth.setColor(r.getColor(R.color.compass_needle_color_north));
		needlePaintNorth.setStrokeWidth(1);
		needlePaintNorth.setStyle(Paint.Style.FILL_AND_STROKE);
		
		needlePaintSouth = new Paint(Paint.ANTI_ALIAS_FLAG);
		needlePaintSouth.setColor(r.getColor(R.color.compass_needle_color_south));
		needlePaintSouth.setStrokeWidth(1);
		needlePaintSouth.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Find the center of the control, and store the length of the smallest
		// side as the compass’s
		// radius.
		int px = getMeasuredWidth() / 2;
		int py = getMeasuredHeight() / 2;
		int radius = Math.max(px, py);
		
		// Decrease the radius to allow direction letters and spacings
		int textWidth = (int) textPaint.measureText("W");
		int textHeight = (int) textPaint.measureText("yY");
		
		int spacingX = textWidth / 2;
		int spacingY = textHeight / 2;
		
		// Allow double spacing between the letters and circle
		radius -= Math.max(textWidth+spacingX*2, textHeight+spacingY*2);
		
		// Save canvas state before painting compass needle
		canvas.save();
		
		// Rotate the canvas so the top is matching the current bearing
		//canvas.rotate(-(float)bearing, px, py);
		canvas.rotate(-(float)bearing * 360 / (2 * 3.14159f), px, py);
		
		// Draw the needle
		Log.i("COMPASS", "new bearing="+bearing);
		
		//canvas.drawLine(px, py - radius - spacingY/2, px, py + radius + spacingY/2, markerPaint);
		
		// Set path points for needle top
		compassNeedleTopPath.reset(); 
		compassNeedleTopPath.moveTo(px, py - radius - spacingY/2); 
		compassNeedleTopPath.lineTo(px + spacingX, py);
		compassNeedleTopPath.lineTo(px - spacingX, py);
		
		// Draw needle top
		canvas.drawPath(compassNeedleTopPath, needlePaintNorth);
		
		// Set path points for needle bottom
		compassNeedleBottomPath.reset(); 
		compassNeedleBottomPath.moveTo(px, py + radius + spacingY/2); 
		compassNeedleBottomPath.lineTo(px + spacingX, py);
		compassNeedleBottomPath.lineTo(px - spacingX, py);
		
		// Draw needle bottom
		canvas.drawPath(compassNeedleBottomPath, needlePaintSouth);
	
		// Restore to the 'top' state after painting compass needle
		canvas.restore();
		
		// Draw north letter
		canvas.drawText(northString, px-spacingX, textHeight+spacingY, textPaint);

		// Draw east letter
		canvas.drawText(eastString, px+radius+spacingX, py+spacingY/2, textPaint);
		
		// Draw south letter
		canvas.drawText(southString, px-spacingX/2, py+radius+spacingY+textWidth, textPaint);
		
		// Draw west letter
		canvas.drawText(westString, spacingX, py+spacingY/2, textPaint);
				
		// Draw the background
		canvas.drawCircle(px, py, radius, circlePaint);
		
		// Save canvas state before painting the 45 degree marks on the circle
		canvas.save();
		
		// Draw the markers every 45 degrees
		for (int i = 0; i < 8; i++) {
			// Draw the cardinal points
			if (i % 2 == 0) {
				// Draw a marker.
				canvas.drawLine(px, py - radius-spacingY/2, px, py - radius + 5, markerPaint);
				canvas.save();
				canvas.translate(0, textHeight);
			} else {
				// Draw a smaller marker.
				canvas.drawLine(px, py - radius, px, py - radius + 5, markerPaint);
				canvas.save();
				canvas.translate(0, textHeight);
			}
			canvas.restore();
			canvas.rotate(45, px, py);
		}
		
		// Restore canvas state after painting 45 degree marks on the circle
		canvas.restore(); 
	}
}
