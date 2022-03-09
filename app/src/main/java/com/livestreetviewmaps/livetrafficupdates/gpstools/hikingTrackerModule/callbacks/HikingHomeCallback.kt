package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models.HikingHomeModel

interface HikingHomeCallback {
    fun onWorkoutClick(model:HikingHomeModel,pos:Int)
}