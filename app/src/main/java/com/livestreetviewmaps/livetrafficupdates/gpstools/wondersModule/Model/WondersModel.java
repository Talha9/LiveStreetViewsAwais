package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class WondersModel implements Parcelable {
    String wonderName;
    double wonderLatitude;
    double wonderLongitude;
    String wonderImg;
    String wonder360Url;
    String wonderWebCamUrl;

    public WondersModel(String wonderName, double wonderLatitude, double wonderLongitude, String wonderImg, String wonder360Url, String wonderWebCamUrl) {
        this.wonderName = wonderName;
        this.wonderLatitude = wonderLatitude;
        this.wonderLongitude = wonderLongitude;
        this.wonderImg = wonderImg;
        this.wonder360Url = wonder360Url;
        this.wonderWebCamUrl = wonderWebCamUrl;
    }

    protected WondersModel(Parcel in) {
        wonderName = in.readString();
        wonderLatitude = in.readDouble();
        wonderLongitude = in.readDouble();
        wonderImg = in.readString();
        wonder360Url = in.readString();
        wonderWebCamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wonderName);
        dest.writeDouble(wonderLatitude);
        dest.writeDouble(wonderLongitude);
        dest.writeString(wonderImg);
        dest.writeString(wonder360Url);
        dest.writeString(wonderWebCamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WondersModel> CREATOR = new Creator<WondersModel>() {
        @Override
        public WondersModel createFromParcel(Parcel in) {
            return new WondersModel(in);
        }

        @Override
        public WondersModel[] newArray(int size) {
            return new WondersModel[size];
        }
    };

    public String getWonderName() {
        return wonderName;
    }

    public void setWonderName(String wonderName) {
        this.wonderName = wonderName;
    }

    public double getWonderLatitude() {
        return wonderLatitude;
    }

    public void setWonderLatitude(double wonderLatitude) {
        this.wonderLatitude = wonderLatitude;
    }

    public double getWonderLongitude() {
        return wonderLongitude;
    }

    public void setWonderLongitude(double wonderLongitude) {
        this.wonderLongitude = wonderLongitude;
    }

    public String getWonderImg() {
        return wonderImg;
    }

    public void setWonderImg(String wonderImg) {
        this.wonderImg = wonderImg;
    }

    public String getWonder360Url() {
        return wonder360Url;
    }

    public void setWonder360Url(String wonder360Url) {
        this.wonder360Url = wonder360Url;
    }

    public String getWonderWebCamUrl() {
        return wonderWebCamUrl;
    }

    public void setWonderWebCamUrl(String wonderWebCamUrl) {
        this.wonderWebCamUrl = wonderWebCamUrl;
    }
}
