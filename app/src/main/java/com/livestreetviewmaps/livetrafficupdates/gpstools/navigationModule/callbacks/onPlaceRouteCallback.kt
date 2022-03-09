package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.PlaceResultModel

interface onPlaceRouteCallback {
    fun onRoutesGenerate(model:PlaceResultModel)
}