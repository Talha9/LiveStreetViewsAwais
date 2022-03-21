package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryInfoDetailsParamsModel implements Parcelable {
    String paramName;
    String paramUnit;

    public CountryInfoDetailsParamsModel(String paramName, String paramUnit) {
        this.paramName = paramName;
        this.paramUnit = paramUnit;
    }

    protected CountryInfoDetailsParamsModel(Parcel in) {
        paramName = in.readString();
        paramUnit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paramName);
        dest.writeString(paramUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CountryInfoDetailsParamsModel> CREATOR = new Creator<CountryInfoDetailsParamsModel>() {
        @Override
        public CountryInfoDetailsParamsModel createFromParcel(Parcel in) {
            return new CountryInfoDetailsParamsModel(in);
        }

        @Override
        public CountryInfoDetailsParamsModel[] newArray(int size) {
            return new CountryInfoDetailsParamsModel[size];
        }
    };

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamUnit() {
        return paramUnit;
    }

    public void setParamUnit(String paramUnit) {
        this.paramUnit = paramUnit;
    }
}
