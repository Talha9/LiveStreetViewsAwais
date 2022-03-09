package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyStreetsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;
    @ColumnInfo(name = "cityName")
    public String cityName;
    @ColumnInfo(name = "countryName")
    public String countryName;
    @ColumnInfo(name = "viewName")
    public String viewName;
    @ColumnInfo(name = "imagePath")
    public String imagePath;

    public MyStreetsModel() {
    }

    public MyStreetsModel(String cityName, String countryName, String viewName, String imagePath) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.viewName = viewName;
        this.imagePath = imagePath;
    }

    protected MyStreetsModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        cityName = in.readString();
        countryName = in.readString();
        viewName = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(cityName);
        dest.writeString(countryName);
        dest.writeString(viewName);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyStreetsModel> CREATOR = new Creator<MyStreetsModel>() {
        @Override
        public MyStreetsModel createFromParcel(Parcel in) {
            return new MyStreetsModel(in);
        }

        @Override
        public MyStreetsModel[] newArray(int size) {
            return new MyStreetsModel[size];
        }
    };
}
