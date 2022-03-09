package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyStreetsModel::class], version = 2, exportSchema = false)
abstract class StreetViewDatabaseInstance:RoomDatabase(){
    abstract val mStreetViewDao: StreetViewDao?
    companion object {
        private const val DB_NAME = "live_streets_db"
        private var INSTANCE: StreetViewDatabaseInstance? = null
        fun getDatabaseInstance(context: Context?): StreetViewDatabaseInstance? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context!!,
                    StreetViewDatabaseInstance::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }

}