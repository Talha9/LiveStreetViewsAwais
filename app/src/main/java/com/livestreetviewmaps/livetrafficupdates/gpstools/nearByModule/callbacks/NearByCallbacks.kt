package com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.models.NearByModel

interface NearByCallbacks {
    fun onNearByCategoryClick(model:NearByModel)
}