package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryInfoDetailsModel implements Parcelable {
    String countryName;
    String countryPopulation;
    String countryArea;
    String countryTimeZone;
    String countryCurrency;
    String countryLanguage;
    String countryLatitude;
    String countryLongitude;

    public CountryInfoDetailsModel(String countryName, String countryPopulation, String countryArea, String countryTimeZone, String countryCurrency, String countryLanguage, String countryLatitude, String countryLongitude) {
        this.countryName = countryName;
        this.countryPopulation = countryPopulation;
        this.countryArea = countryArea;
        this.countryTimeZone = countryTimeZone;
        this.countryCurrency = countryCurrency;
        this.countryLanguage = countryLanguage;
        this.countryLatitude = countryLatitude;
        this.countryLongitude = countryLongitude;
    }

    protected CountryInfoDetailsModel(Parcel in) {
        countryName = in.readString();
        countryPopulation = in.readString();
        countryArea = in.readString();
        countryTimeZone = in.readString();
        countryCurrency = in.readString();
        countryLanguage = in.readString();
        countryLatitude = in.readString();
        countryLongitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryName);
        dest.writeString(countryPopulation);
        dest.writeString(countryArea);
        dest.writeString(countryTimeZone);
        dest.writeString(countryCurrency);
        dest.writeString(countryLanguage);
        dest.writeString(countryLatitude);
        dest.writeString(countryLongitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CountryInfoDetailsModel> CREATOR = new Creator<CountryInfoDetailsModel>() {
        @Override
        public CountryInfoDetailsModel createFromParcel(Parcel in) {
            return new CountryInfoDetailsModel(in);
        }

        @Override
        public CountryInfoDetailsModel[] newArray(int size) {
            return new CountryInfoDetailsModel[size];
        }
    };

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryPopulation() {
        return countryPopulation;
    }

    public void setCountryPopulation(String countryPopulation) {
        this.countryPopulation = countryPopulation;
    }

    public String getCountryArea() {
        return countryArea;
    }

    public void setCountryArea(String countryArea) {
        this.countryArea = countryArea;
    }

    public String getCountryTimeZone() {
        return countryTimeZone;
    }

    public void setCountryTimeZone(String countryTimeZone) {
        this.countryTimeZone = countryTimeZone;
    }

    public String getCountryCurrency() {
        return countryCurrency;
    }

    public void setCountryCurrency(String countryCurrency) {
        this.countryCurrency = countryCurrency;
    }

    public String getCountryLanguage() {
        return countryLanguage;
    }

    public void setCountryLanguage(String countryLanguage) {
        this.countryLanguage = countryLanguage;
    }

    public String getCountryLatitude() {
        return countryLatitude;
    }

    public void setCountryLatitude(String countryLatitude) {
        this.countryLatitude = countryLatitude;
    }

    public String getCountryLongitude() {
        return countryLongitude;
    }

    public void setCountryLongitude(String countryLongitude) {
        this.countryLongitude = countryLongitude;
    }
}
