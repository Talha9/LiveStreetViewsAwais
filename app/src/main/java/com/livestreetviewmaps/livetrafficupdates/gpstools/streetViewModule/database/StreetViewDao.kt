package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public interface StreetViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteStreet(info: MyStreetsModel):Long

    @Query("SELECT * FROM MyStreetsModel")
    fun fetchMyFavourit():List<MyStreetsModel>
}