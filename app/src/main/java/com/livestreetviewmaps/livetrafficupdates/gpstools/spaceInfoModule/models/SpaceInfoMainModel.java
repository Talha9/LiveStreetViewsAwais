package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SpaceInfoMainModel implements Parcelable {
    String planetName;
    int planetImg;

    public SpaceInfoMainModel(String planetName, int planetImg) {
        this.planetName = planetName;
        this.planetImg = planetImg;
    }

    protected SpaceInfoMainModel(Parcel in) {
        planetName = in.readString();
        planetImg = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planetName);
        dest.writeInt(planetImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpaceInfoMainModel> CREATOR = new Creator<SpaceInfoMainModel>() {
        @Override
        public SpaceInfoMainModel createFromParcel(Parcel in) {
            return new SpaceInfoMainModel(in);
        }

        @Override
        public SpaceInfoMainModel[] newArray(int size) {
            return new SpaceInfoMainModel[size];
        }
    };

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public int getPlanetImg() {
        return planetImg;
    }

    public void setPlanetImg(int planetImg) {
        this.planetImg = planetImg;
    }
}
