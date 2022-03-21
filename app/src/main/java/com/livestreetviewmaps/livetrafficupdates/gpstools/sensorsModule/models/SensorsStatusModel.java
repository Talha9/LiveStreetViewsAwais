package com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.models;

public class SensorsStatusModel {
    private String Value;
    private String Name;

    public SensorsStatusModel(String value, String name) {
        Value = value;
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
