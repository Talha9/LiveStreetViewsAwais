package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable

@Dao
interface LiveStreetViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertHiking(model: HikingTable):Long?

   @Query("SELECT * FROM HIKINGTABLE")
   fun fetchAllMyHiking():LiveData<List<HikingTable>>

   @Query("DELETE FROM HIKINGTABLE Where id=:id")
   fun DeleteSpecificHikingData(id:Int)

   @Query("DELETE FROM HIKINGTABLE")
   fun DeleteAllData()


}