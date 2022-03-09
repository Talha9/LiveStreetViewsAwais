package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.LiveStreetViewRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable

class LiveStreetViewModel(application: Application) : AndroidViewModel(application) {

    private var repository:LiveStreetViewRepository
    lateinit var liveData: LiveData<List<HikingTable>>

    init {
        repository=LiveStreetViewRepository(application)

    }

    fun insert(table:HikingTable) {
        repository.insert(table)
    }

    fun delete(id:Int) {
        repository.deleteActivity(id)
    }

    fun deleteAllActivities() {
        repository.deleteAllNotes()
    }

    fun getAllActivities(): LiveData<List<HikingTable>> {
        liveData=repository.getAllActivities()
        return liveData
    }

}