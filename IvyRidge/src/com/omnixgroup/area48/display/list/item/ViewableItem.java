package com.omnixgroup.area48.display.list.item;

import android.os.Parcelable;
import android.view.View;

public abstract class ViewableItem {
	private int itemTemplate;
	private Parcelable itemData;
	
	public int getItemTemplate() {
		return itemTemplate;
	}
	
	public Parcelable getItemData() {
		return itemData;
	}

	public ViewableItem(){}
	
	public ViewableItem(Parcelable itemData, int itemTemplate)
	{
		this.itemTemplate = itemTemplate;
		this.itemData = itemData;
	}
	
	public abstract void paintView( View v);
}
