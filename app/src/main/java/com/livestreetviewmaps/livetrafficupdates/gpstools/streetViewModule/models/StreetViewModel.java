package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StreetViewModel implements Parcelable {
    String imageLink;
    String cityName;
    String countryName;
    String details;
    String viewName;

    public StreetViewModel(String imageLink, String cityName, String countryName, String details, String viewName) {
        this.imageLink = imageLink;
        this.cityName = cityName;
        this.countryName = countryName;
        this.details = details;
        this.viewName = viewName;
    }

    public StreetViewModel(String imageLink, String countryName, String details, String viewName) {
        this.imageLink = imageLink;
        this.countryName = countryName;
        this.details = details;
        this.viewName = viewName;
    }

    public StreetViewModel() {
    }

    protected StreetViewModel(Parcel in) {
        imageLink = in.readString();
        cityName = in.readString();
        countryName = in.readString();
        details = in.readString();
        viewName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageLink);
        dest.writeString(cityName);
        dest.writeString(countryName);
        dest.writeString(details);
        dest.writeString(viewName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StreetViewModel> CREATOR = new Creator<StreetViewModel>() {
        @Override
        public StreetViewModel createFromParcel(Parcel in) {
            return new StreetViewModel(in);
        }

        @Override
        public StreetViewModel[] newArray(int size) {
            return new StreetViewModel[size];
        }
    };

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
