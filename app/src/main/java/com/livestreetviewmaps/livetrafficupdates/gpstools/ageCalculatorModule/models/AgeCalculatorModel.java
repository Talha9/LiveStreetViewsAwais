package com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AgeCalculatorModel implements Parcelable {
    String calculatedValue;
    String valueUnit;

    public AgeCalculatorModel(String calculatedValue, String valueUnit) {
        this.calculatedValue = calculatedValue;
        this.valueUnit = valueUnit;
    }

    protected AgeCalculatorModel(Parcel in) {
        calculatedValue = in.readString();
        valueUnit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(calculatedValue);
        dest.writeString(valueUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AgeCalculatorModel> CREATOR = new Creator<AgeCalculatorModel>() {
        @Override
        public AgeCalculatorModel createFromParcel(Parcel in) {
            return new AgeCalculatorModel(in);
        }

        @Override
        public AgeCalculatorModel[] newArray(int size) {
            return new AgeCalculatorModel[size];
        }
    };

    public String getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(String calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }
}
