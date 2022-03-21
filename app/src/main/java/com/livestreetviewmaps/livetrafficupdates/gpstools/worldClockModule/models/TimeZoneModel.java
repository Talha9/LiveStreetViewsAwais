package com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeZoneModel implements Parcelable {
    String timezone;
    String country;
    String country_code;

    public TimeZoneModel(String timezone, String country, String country_code) {
        this.timezone = timezone;
        this.country = country;
        this.country_code = country_code;
    }

    protected TimeZoneModel(Parcel in) {
        timezone = in.readString();
        country = in.readString();
        country_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timezone);
        dest.writeString(country);
        dest.writeString(country_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeZoneModel> CREATOR = new Creator<TimeZoneModel>() {
        @Override
        public TimeZoneModel createFromParcel(Parcel in) {
            return new TimeZoneModel(in);
        }

        @Override
        public TimeZoneModel[] newArray(int size) {
            return new TimeZoneModel[size];
        }
    };

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
