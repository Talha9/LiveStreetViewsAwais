package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel

interface onStreetViewClickCallback {
    fun onClick(model: StreetViewModel)
}