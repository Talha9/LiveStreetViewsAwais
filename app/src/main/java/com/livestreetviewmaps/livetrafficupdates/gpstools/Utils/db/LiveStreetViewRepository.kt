package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LiveStreetViewRepository(application: Application) {
    private var dao:LiveStreetViewDao
    private var liveData:LiveData<List<HikingTable>>

    init {
        val databaseInstance=LiveStreetViewDB.getDatabaseInstance(application)
        dao=databaseInstance.LiveStreetViewDao()
        liveData=dao.fetchAllMyHiking()
    }

    //methods for database operations :-

    fun insert(table:HikingTable?) {
        InsertActivity(dao).execute(table)
    }

    fun deleteActivity(id:Int) {
        DeleteActivity(dao).execute(id)
    }

    fun deleteAllNotes() {
        DeleteAllActivity(dao).execute()
    }

    fun getAllActivities(): LiveData<List<HikingTable>> {
        getAllActivities(dao).execute()
        return liveData
    }

companion object{
    class InsertActivity(dao: LiveStreetViewDao):CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(table: HikingTable?) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(table) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(table: HikingTable?) {
            dao.insertHiking(table!!)
        }
    }
    class DeleteActivity(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(id: Int) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(id) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(id: Int) {
            dao.DeleteSpecificHikingData(id)
        }

    }

    class DeleteAllActivity(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job

        fun execute() = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground() {
            dao.DeleteAllData()
        }

    }

    class getAllActivities(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job

        fun execute() = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground() {
            dao.fetchAllMyHiking()
        }

    }
}




}





