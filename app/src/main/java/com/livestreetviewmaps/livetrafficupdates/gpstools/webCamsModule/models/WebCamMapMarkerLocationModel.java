package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WebCamMapMarkerLocationModel implements Parcelable {
    Double latitude;
    Double longitude;
    String placeName;

    public WebCamMapMarkerLocationModel(Double latitude, Double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    protected WebCamMapMarkerLocationModel(Parcel in) {
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        placeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(placeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebCamMapMarkerLocationModel> CREATOR = new Creator<WebCamMapMarkerLocationModel>() {
        @Override
        public WebCamMapMarkerLocationModel createFromParcel(Parcel in) {
            return new WebCamMapMarkerLocationModel(in);
        }

        @Override
        public WebCamMapMarkerLocationModel[] newArray(int size) {
            return new WebCamMapMarkerLocationModel[size];
        }
    };

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
