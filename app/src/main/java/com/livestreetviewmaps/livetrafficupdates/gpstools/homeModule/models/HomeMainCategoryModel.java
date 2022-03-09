package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class HomeMainCategoryModel implements Parcelable {
    private String MainCategory;
   private ArrayList<HomeMainSubCategoryModel> list;

    public HomeMainCategoryModel(String mainCategory, ArrayList<HomeMainSubCategoryModel> list) {
        MainCategory = mainCategory;
        this.list = list;
    }

    public HomeMainCategoryModel() {

    }

    protected HomeMainCategoryModel(Parcel in) {
        MainCategory = in.readString();
        list = in.createTypedArrayList(HomeMainSubCategoryModel.CREATOR);
    }

    public static final Creator<HomeMainCategoryModel> CREATOR = new Creator<HomeMainCategoryModel>() {
        @Override
        public HomeMainCategoryModel createFromParcel(Parcel in) {
            return new HomeMainCategoryModel(in);
        }

        @Override
        public HomeMainCategoryModel[] newArray(int size) {
            return new HomeMainCategoryModel[size];
        }
    };

    public String getMainCategory() {
        return MainCategory;
    }

    public void setMainCategory(String mainCategory) {
        MainCategory = mainCategory;
    }

    public ArrayList<HomeMainSubCategoryModel> getList() {
        return list;
    }

    public void setList(ArrayList<HomeMainSubCategoryModel> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(MainCategory);
        parcel.writeTypedList(list);
    }
}
