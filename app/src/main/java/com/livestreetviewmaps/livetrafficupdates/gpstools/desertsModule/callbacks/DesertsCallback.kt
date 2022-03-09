package com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel

interface DesertsCallback {
    fun onDesertClick(model: DesertsModel)
}