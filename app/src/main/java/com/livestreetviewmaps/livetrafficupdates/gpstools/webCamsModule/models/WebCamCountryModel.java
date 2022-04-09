package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WebCamCountryModel implements Parcelable {
    String country;
    String dialcode;
    String code;

    public WebCamCountryModel(String country, String dialcode, String code) {
        this.country = country;
        this.dialcode = dialcode;
        this.code = code;
    }

    protected WebCamCountryModel(Parcel in) {
        country = in.readString();
        dialcode = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(dialcode);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebCamCountryModel> CREATOR = new Creator<WebCamCountryModel>() {
        @Override
        public WebCamCountryModel createFromParcel(Parcel in) {
            return new WebCamCountryModel(in);
        }

        @Override
        public WebCamCountryModel[] newArray(int size) {
            return new WebCamCountryModel[size];
        }
    };

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDialcode() {
        return dialcode;
    }

    public void setDialcode(String dialcode) {
        this.dialcode = dialcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
