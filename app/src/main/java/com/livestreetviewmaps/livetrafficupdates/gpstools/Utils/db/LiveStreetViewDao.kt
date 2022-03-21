package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.FavouritesTable
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



   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertFavourites(model: FavouritesTable):Long?

   @Query("SELECT * FROM FavouritesTable")
   fun fetchAllMyFavourites():LiveData<List<FavouritesTable>>

   @Query("DELETE FROM FavouritesTable Where id=:id")
   fun DeleteSpecificFavouriteData(id:Int)

}