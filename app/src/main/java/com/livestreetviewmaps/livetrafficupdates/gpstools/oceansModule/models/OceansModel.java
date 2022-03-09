package com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OceansModel implements Parcelable {
    String oceansName;
    double oceansLatitude;
    double oceansLongitude;
    String oceansImg;
    String oceans360Url;
    String oceansWebCamUrl;

    public OceansModel(String oceansName, double oceansLatitude, double oceansLongitude, String oceansImg, String oceans360Url, String oceansWebCamUrl) {
        this.oceansName = oceansName;
        this.oceansLatitude = oceansLatitude;
        this.oceansLongitude = oceansLongitude;
        this.oceansImg = oceansImg;
        this.oceans360Url = oceans360Url;
        this.oceansWebCamUrl = oceansWebCamUrl;
    }

    protected OceansModel(Parcel in) {
        oceansName = in.readString();
        oceansLatitude = in.readDouble();
        oceansLongitude = in.readDouble();
        oceansImg = in.readString();
        oceans360Url = in.readString();
        oceansWebCamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oceansName);
        dest.writeDouble(oceansLatitude);
        dest.writeDouble(oceansLongitude);
        dest.writeString(oceansImg);
        dest.writeString(oceans360Url);
        dest.writeString(oceansWebCamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OceansModel> CREATOR = new Creator<OceansModel>() {
        @Override
        public OceansModel createFromParcel(Parcel in) {
            return new OceansModel(in);
        }

        @Override
        public OceansModel[] newArray(int size) {
            return new OceansModel[size];
        }
    };

    public String getOceansName() {
        return oceansName;
    }

    public void setOceansName(String oceansName) {
        this.oceansName = oceansName;
    }

    public double getOceansLatitude() {
        return oceansLatitude;
    }

    public void setOceansLatitude(double oceansLatitude) {
        this.oceansLatitude = oceansLatitude;
    }

    public double getOceansLongitude() {
        return oceansLongitude;
    }

    public void setOceansLongitude(double oceansLongitude) {
        this.oceansLongitude = oceansLongitude;
    }

    public String getOceansImg() {
        return oceansImg;
    }

    public void setOceansImg(String oceansImg) {
        this.oceansImg = oceansImg;
    }

    public String getOceans360Url() {
        return oceans360Url;
    }

    public void setOceans360Url(String oceans360Url) {
        this.oceans360Url = oceans360Url;
    }

    public String getOceansWebCamUrl() {
        return oceansWebCamUrl;
    }

    public void setOceansWebCamUrl(String oceansWebCamUrl) {
        this.oceansWebCamUrl = oceansWebCamUrl;
    }
}
