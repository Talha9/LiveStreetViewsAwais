package com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RiversModel implements Parcelable {
    String riverName;
    double riverLatitude;
    double riverLongitude;
    String riverImg;
    String river360Url;
    String riverWebCamUrl;

    public RiversModel(String riverName, double riverLatitude, double riverLongitude, String riverImg, String river360Url, String riverWebCamUrl) {
        this.riverName = riverName;
        this.riverLatitude = riverLatitude;
        this.riverLongitude = riverLongitude;
        this.riverImg = riverImg;
        this.river360Url = river360Url;
        this.riverWebCamUrl = riverWebCamUrl;
    }

    protected RiversModel(Parcel in) {
        riverName = in.readString();
        riverLatitude = in.readDouble();
        riverLongitude = in.readDouble();
        riverImg = in.readString();
        river360Url = in.readString();
        riverWebCamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(riverName);
        dest.writeDouble(riverLatitude);
        dest.writeDouble(riverLongitude);
        dest.writeString(riverImg);
        dest.writeString(river360Url);
        dest.writeString(riverWebCamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RiversModel> CREATOR = new Creator<RiversModel>() {
        @Override
        public RiversModel createFromParcel(Parcel in) {
            return new RiversModel(in);
        }

        @Override
        public RiversModel[] newArray(int size) {
            return new RiversModel[size];
        }
    };

    public String getRiverName() {
        return riverName;
    }

    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

    public double getRiverLatitude() {
        return riverLatitude;
    }

    public void setRiverLatitude(double riverLatitude) {
        this.riverLatitude = riverLatitude;
    }

    public double getRiverLongitude() {
        return riverLongitude;
    }

    public void setRiverLongitude(double riverLongitude) {
        this.riverLongitude = riverLongitude;
    }

    public String getRiverImg() {
        return riverImg;
    }

    public void setRiverImg(String riverImg) {
        this.riverImg = riverImg;
    }

    public String getRiver360Url() {
        return river360Url;
    }

    public void setRiver360Url(String river360Url) {
        this.river360Url = river360Url;
    }

    public String getRiverWebCamUrl() {
        return riverWebCamUrl;
    }

    public void setRiverWebCamUrl(String riverWebCamUrl) {
        this.riverWebCamUrl = riverWebCamUrl;
    }
}
