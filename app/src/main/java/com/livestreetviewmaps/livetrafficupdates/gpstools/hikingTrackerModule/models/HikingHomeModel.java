package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HikingHomeModel implements Parcelable {
    String workoutName;
    int workoutImg;

    public HikingHomeModel(String workoutName, int workoutImg) {
        this.workoutName = workoutName;
        this.workoutImg = workoutImg;
    }

    protected HikingHomeModel(Parcel in) {
        workoutName = in.readString();
        workoutImg = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workoutName);
        dest.writeInt(workoutImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HikingHomeModel> CREATOR = new Creator<HikingHomeModel>() {
        @Override
        public HikingHomeModel createFromParcel(Parcel in) {
            return new HikingHomeModel(in);
        }

        @Override
        public HikingHomeModel[] newArray(int size) {
            return new HikingHomeModel[size];
        }
    };

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getWorkoutImg() {
        return workoutImg;
    }

    public void setWorkoutImg(int workoutImg) {
        this.workoutImg = workoutImg;
    }
}
