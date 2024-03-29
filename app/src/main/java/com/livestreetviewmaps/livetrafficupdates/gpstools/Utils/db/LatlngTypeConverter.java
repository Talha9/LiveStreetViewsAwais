package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.MyLatLng;

import java.util.Collections;
import java.util.List;
import java.lang.reflect.Type;

public class LatlngTypeConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<MyLatLng> stringToUserModelList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<MyLatLng>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<MyLatLng> someObjects) {
        return gson.toJson(someObjects);
    }
}
