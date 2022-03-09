package com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.models.RiversModel

interface RiversCallback {
    fun onRiverClick(model: RiversModel)
}