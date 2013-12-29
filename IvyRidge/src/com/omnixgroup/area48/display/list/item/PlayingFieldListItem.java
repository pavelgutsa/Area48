package com.omnixgroup.area48.display.list.item;

import com.omnixgroup.area48.R;
import com.omnixgroup.area48.display.list.item.ViewableItem;
import com.omnixgroup.area48.persistence.data.PlayingField;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PlayingFieldListItem extends ViewableItem {
	
	public PlayingFieldListItem(PlayingField playingField) {
		super(playingField, R.layout.playing_field_listitem);
	}
	
	public void paintView( View v)
	{
		// Set objective name
		TextView text = (TextView) v.findViewById(R.id.fieldName);
		if (text != null) {
			String fieldName = ((PlayingField)getItemData()).getfieldName();
			Log.i("PlayingFieldListItem", "Setting R.id.fieldName to "+fieldName);
			text.setText(fieldName);
		} else {
			Log.i("PlayingFieldListItem", "Could not find R.id.fieldName");
		}
		
	}
}
