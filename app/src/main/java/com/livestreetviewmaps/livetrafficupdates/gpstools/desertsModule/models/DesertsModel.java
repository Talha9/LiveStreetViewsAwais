package com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DesertsModel implements Parcelable {
    String desertsName;
    double desertsLatitude;
    double desertsLongitude;
    String desertsImg;
    String deserts360Url;
    String desertsWebCamUrl;

    public DesertsModel(String desertsName, double desertsLatitude, double desertsLongitude, String desertsImg, String deserts360Url, String desertsWebCamUrl) {
        this.desertsName = desertsName;
        this.desertsLatitude = desertsLatitude;
        this.desertsLongitude = desertsLongitude;
        this.desertsImg = desertsImg;
        this.deserts360Url = deserts360Url;
        this.desertsWebCamUrl = desertsWebCamUrl;
    }

    protected DesertsModel(Parcel in) {
        desertsName = in.readString();
        desertsLatitude = in.readDouble();
        desertsLongitude = in.readDouble();
        desertsImg = in.readString();
        deserts360Url = in.readString();
        desertsWebCamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desertsName);
        dest.writeDouble(desertsLatitude);
        dest.writeDouble(desertsLongitude);
        dest.writeString(desertsImg);
        dest.writeString(deserts360Url);
        dest.writeString(desertsWebCamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DesertsModel> CREATOR = new Creator<DesertsModel>() {
        @Override
        public DesertsModel createFromParcel(Parcel in) {
            return new DesertsModel(in);
        }

        @Override
        public DesertsModel[] newArray(int size) {
            return new DesertsModel[size];
        }
    };

    public String getDesertsName() {
        return desertsName;
    }

    public void setDesertsName(String desertsName) {
        this.desertsName = desertsName;
    }

    public double getDesertsLatitude() {
        return desertsLatitude;
    }

    public void setDesertsLatitude(double desertsLatitude) {
        this.desertsLatitude = desertsLatitude;
    }

    public double getDesertsLongitude() {
        return desertsLongitude;
    }

    public void setDesertsLongitude(double desertsLongitude) {
        this.desertsLongitude = desertsLongitude;
    }

    public String getDesertsImg() {
        return desertsImg;
    }

    public void setDesertsImg(String desertsImg) {
        this.desertsImg = desertsImg;
    }

    public String getDeserts360Url() {
        return deserts360Url;
    }

    public void setDeserts360Url(String deserts360Url) {
        this.deserts360Url = deserts360Url;
    }

    public String getDesertsWebCamUrl() {
        return desertsWebCamUrl;
    }

    public void setDesertsWebCamUrl(String desertsWebCamUrl) {
        this.desertsWebCamUrl = desertsWebCamUrl;
    }
}
