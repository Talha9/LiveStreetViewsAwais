package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fourSquareApi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parent {

    @SerializedName("fsq_id")
    @Expose
    private String fsqId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getFsqId() {
        return fsqId;
    }

    public void setFsqId(String fsqId) {
        this.fsqId = fsqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
