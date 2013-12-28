package com.omnixgroup.area48.persistence.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayingField implements Parcelable {
	
	private String fieldName;
    private String fieldLocation;
    
    public PlayingField(String fieldName, String fieldLocation) 
    {
    	this.fieldName = fieldName;
    	this.fieldLocation = fieldLocation;
    }
    
	public String getfieldName() {
        return fieldName;
    }
    public void setfieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getfieldLocation() {
		return fieldLocation;
	}
	public void setfieldLocation(String fieldLocation) {
		this.fieldLocation = fieldLocation;
	}
    
    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(fieldName);
    	out.writeString(fieldLocation);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<PlayingField> CREATOR = new Parcelable.Creator<PlayingField>() {
        public PlayingField createFromParcel(Parcel in) {
            return new PlayingField(in);
        }

        public PlayingField[] newArray(int size) {
            return new PlayingField[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private PlayingField(Parcel in) {
        fieldName = in.readString();
    	fieldLocation = in.readString();
    }
}
