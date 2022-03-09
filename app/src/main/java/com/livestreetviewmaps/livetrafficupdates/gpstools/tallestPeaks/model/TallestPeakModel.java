package com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TallestPeakModel implements Parcelable {
    String tallestPeakName;
    double tallestLatitude;
    double tallestLongitude;
    String tallestImg;
    String tallest360Url;
    String tallestWebCamUrl;

    public TallestPeakModel(String tallestPeakName, double tallestLatitude, double tallestLongitude, String tallestImg, String tallest360Url, String tallestWebCamUrl) {
        this.tallestPeakName = tallestPeakName;
        this.tallestLatitude = tallestLatitude;
        this.tallestLongitude = tallestLongitude;
        this.tallestImg = tallestImg;
        this.tallest360Url = tallest360Url;
        this.tallestWebCamUrl = tallestWebCamUrl;
    }

    protected TallestPeakModel(Parcel in) {
        tallestPeakName = in.readString();
        tallestLatitude = in.readDouble();
        tallestLongitude = in.readDouble();
        tallestImg = in.readString();
        tallest360Url = in.readString();
        tallestWebCamUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tallestPeakName);
        dest.writeDouble(tallestLatitude);
        dest.writeDouble(tallestLongitude);
        dest.writeString(tallestImg);
        dest.writeString(tallest360Url);
        dest.writeString(tallestWebCamUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TallestPeakModel> CREATOR = new Creator<TallestPeakModel>() {
        @Override
        public TallestPeakModel createFromParcel(Parcel in) {
            return new TallestPeakModel(in);
        }

        @Override
        public TallestPeakModel[] newArray(int size) {
            return new TallestPeakModel[size];
        }
    };

    public String getTallestPeakName() {
        return tallestPeakName;
    }

    public void setTallestPeakName(String tallestPeakName) {
        this.tallestPeakName = tallestPeakName;
    }

    public double getTallestLatitude() {
        return tallestLatitude;
    }

    public void setTallestLatitude(double tallestLatitude) {
        this.tallestLatitude = tallestLatitude;
    }

    public double getTallestLongitude() {
        return tallestLongitude;
    }

    public void setTallestLongitude(double tallestLongitude) {
        this.tallestLongitude = tallestLongitude;
    }

    public String getTallestImg() {
        return tallestImg;
    }

    public void setTallestImg(String tallestImg) {
        this.tallestImg = tallestImg;
    }

    public String getTallest360Url() {
        return tallest360Url;
    }

    public void setTallest360Url(String tallest360Url) {
        this.tallest360Url = tallest360Url;
    }

    public String getTallestWebCamUrl() {
        return tallestWebCamUrl;
    }

    public void setTallestWebCamUrl(String tallestWebCamUrl) {
        this.tallestWebCamUrl = tallestWebCamUrl;
    }
}
