package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WebCamVideoLinkModel implements Parcelable {
    String placeName;
    String url;

    public WebCamVideoLinkModel(String placeName, String url) {
        this.placeName = placeName;
        this.url = url;
    }

    protected WebCamVideoLinkModel(Parcel in) {
        placeName = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeName);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebCamVideoLinkModel> CREATOR = new Creator<WebCamVideoLinkModel>() {
        @Override
        public WebCamVideoLinkModel createFromParcel(Parcel in) {
            return new WebCamVideoLinkModel(in);
        }

        @Override
        public WebCamVideoLinkModel[] newArray(int size) {
            return new WebCamVideoLinkModel[size];
        }
    };

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
