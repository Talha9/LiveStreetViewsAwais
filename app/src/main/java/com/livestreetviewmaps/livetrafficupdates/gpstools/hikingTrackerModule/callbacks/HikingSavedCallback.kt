package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable

interface HikingSavedCallback {
    fun onDeleteHikingBtnClick(model:HikingTable,pos:Int)
    fun onHikingActivityClick(model:HikingTable)
}