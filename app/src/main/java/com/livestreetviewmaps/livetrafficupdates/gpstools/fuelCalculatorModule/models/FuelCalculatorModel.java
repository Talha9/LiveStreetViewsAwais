package com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FuelCalculatorModel implements Parcelable {
    String country_name;
    String flagLink;
    String rate;

    public FuelCalculatorModel(String country_name, String flagLink, String rate) {
        this.country_name = country_name;
        this.flagLink = flagLink;
        this.rate = rate;
    }

    protected FuelCalculatorModel(Parcel in) {
        country_name = in.readString();
        flagLink = in.readString();
        rate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country_name);
        dest.writeString(flagLink);
        dest.writeString(rate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FuelCalculatorModel> CREATOR = new Creator<FuelCalculatorModel>() {
        @Override
        public FuelCalculatorModel createFromParcel(Parcel in) {
            return new FuelCalculatorModel(in);
        }

        @Override
        public FuelCalculatorModel[] newArray(int size) {
            return new FuelCalculatorModel[size];
        }
    };

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getFlagLink() {
        return flagLink;
    }

    public void setFlagLink(String flagLink) {
        this.flagLink = flagLink;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
