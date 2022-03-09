package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WebCamsModel implements Parcelable {
    String webCamUrl;
    String placeName;
    Double placeLatitude;
    Double placeLongitude;
    String countryName;
    String cityName;

    public WebCamsModel(String webCamUrl, String placeName, Double placeLatitude, Double placeLongitude, String countryName, String cityName) {
        this.webCamUrl = webCamUrl;
        this.placeName = placeName;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.countryName = countryName;
        this.cityName = cityName;
    }


    protected WebCamsModel(Parcel in) {
        webCamUrl = in.readString();
        placeName = in.readString();
        if (in.readByte() == 0) {
            placeLatitude = null;
        } else {
            placeLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            placeLongitude = null;
        } else {
            placeLongitude = in.readDouble();
        }
        countryName = in.readString();
        cityName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webCamUrl);
        dest.writeString(placeName);
        if (placeLatitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(placeLatitude);
        }
        if (placeLongitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(placeLongitude);
        }
        dest.writeString(countryName);
        dest.writeString(cityName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebCamsModel> CREATOR = new Creator<WebCamsModel>() {
        @Override
        public WebCamsModel createFromParcel(Parcel in) {
            return new WebCamsModel(in);
        }

        @Override
        public WebCamsModel[] newArray(int size) {
            return new WebCamsModel[size];
        }
    };

    public String getWebCamUrl() {
        return webCamUrl;
    }

    public void setWebCamUrl(String webCamUrl) {
        this.webCamUrl = webCamUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
