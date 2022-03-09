package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeMainSubCategoryModel implements Parcelable {
    private int itemDrawable;
    private String itemSubCategoryName;
    private int itemColor;
    private int itemBackground;

    public HomeMainSubCategoryModel() {
    }

    public HomeMainSubCategoryModel(int itemDrawable, String itemSubCategoryName, int itemColor, int itemBackground) {
        this.itemDrawable = itemDrawable;
        this.itemSubCategoryName = itemSubCategoryName;
        this.itemColor = itemColor;
        this.itemBackground = itemBackground;
    }

    protected HomeMainSubCategoryModel(Parcel in) {
        itemDrawable = in.readInt();
        itemSubCategoryName = in.readString();
        itemColor = in.readInt();
        itemBackground = in.readInt();
    }

    public static final Creator<HomeMainSubCategoryModel> CREATOR = new Creator<HomeMainSubCategoryModel>() {
        @Override
        public HomeMainSubCategoryModel createFromParcel(Parcel in) {
            return new HomeMainSubCategoryModel(in);
        }

        @Override
        public HomeMainSubCategoryModel[] newArray(int size) {
            return new HomeMainSubCategoryModel[size];
        }
    };

    public int getItemDrawable() {
        return itemDrawable;
    }

    public void setItemDrawable(int itemDrawable) {
        this.itemDrawable = itemDrawable;
    }

    public String getItemSubCategoryName() {
        return itemSubCategoryName;
    }

    public void setItemSubCategoryName(String itemSubCategoryName) {
        this.itemSubCategoryName = itemSubCategoryName;
    }

    public int getItemColor() {
        return itemColor;
    }

    public void setItemColor(int itemColor) {
        this.itemColor = itemColor;
    }

    public int getItemBackground() {
        return itemBackground;
    }

    public void setItemBackground(int itemBackground) {
        this.itemBackground = itemBackground;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemDrawable);
        parcel.writeString(itemSubCategoryName);
        parcel.writeInt(itemColor);
        parcel.writeInt(itemBackground);
    }
}
