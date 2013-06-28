package com.omnixgroup.area48;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamObjective implements Parcelable {
	
	public enum ObjectiveType {
	    FIND_LOCATION, FIND_TARGET 
	}
	
	public enum ObjectiveStatus {
	    PENDING, COMPLETED 
	}
	
	private String objectiveName;
    private String objectiveDescription;
    
	private ObjectiveType objectiveType;
	private ObjectiveStatus objectiveStatus;
   
    public ObjectiveType getObjectiveType() {
		return objectiveType;
	}
	public void setObjectiveType(ObjectiveType objectiveType) {
		this.objectiveType = objectiveType;
	}
	public String getObjectiveName() {
        return objectiveName;
    }
    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }
    public String getObjectiveDescription() {
		return objectiveDescription;
	}
	public void setObjectiveDescription(String objectiveDescription) {
		this.objectiveDescription = objectiveDescription;
	}
    public ObjectiveStatus getObjectiveStatus() {
        return objectiveStatus;
    }
    public void setObjectiveStatus(ObjectiveStatus objectiveStatus) {
        this.objectiveStatus = objectiveStatus;
    }
    
    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
    	out.writeString(objectiveName);
    	out.writeString(objectiveDescription);
        
    	out.writeValue(objectiveType);
    	out.writeValue(objectiveStatus);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TeamObjective> CREATOR = new Parcelable.Creator<TeamObjective>() {
        public TeamObjective createFromParcel(Parcel in) {
            return new TeamObjective(in);
        }

        public TeamObjective[] newArray(int size) {
            return new TeamObjective[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private TeamObjective(Parcel in) {
        objectiveName = in.readString();
    	objectiveDescription = in.readString();
    }
	public TeamObjective() {
		// TODO Auto-generated constructor stub
	}
}
