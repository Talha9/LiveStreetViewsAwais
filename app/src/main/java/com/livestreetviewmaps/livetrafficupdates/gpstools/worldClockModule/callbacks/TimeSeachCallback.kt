package com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.models.TimeZoneModel

interface TimeSeachCallback {
    fun onTimeZoneClick(model:TimeZoneModel)
}