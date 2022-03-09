package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HoroscopeMainModel implements Parcelable {
    String horoscopeName;
    int horoscopeIcon;
    String horoscopeId;

    public HoroscopeMainModel(String horoscopeName, int horoscopeIcon, String horoscopeId) {
        this.horoscopeName = horoscopeName;
        this.horoscopeIcon = horoscopeIcon;
        this.horoscopeId = horoscopeId;
    }

    protected HoroscopeMainModel(Parcel in) {
        horoscopeName = in.readString();
        horoscopeIcon = in.readInt();
        horoscopeId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(horoscopeName);
        dest.writeInt(horoscopeIcon);
        dest.writeString(horoscopeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HoroscopeMainModel> CREATOR = new Creator<HoroscopeMainModel>() {
        @Override
        public HoroscopeMainModel createFromParcel(Parcel in) {
            return new HoroscopeMainModel(in);
        }

        @Override
        public HoroscopeMainModel[] newArray(int size) {
            return new HoroscopeMainModel[size];
        }
    };

    public String getHoroscopeName() {
        return horoscopeName;
    }

    public void setHoroscopeName(String horoscopeName) {
        this.horoscopeName = horoscopeName;
    }

    public int getHoroscopeIcon() {
        return horoscopeIcon;
    }

    public void setHoroscopeIcon(int horoscopeIcon) {
        this.horoscopeIcon = horoscopeIcon;
    }

    public String getHoroscopeId() {
        return horoscopeId;
    }

    public void setHoroscopeId(String horoscopeId) {
        this.horoscopeId = horoscopeId;
    }
}
