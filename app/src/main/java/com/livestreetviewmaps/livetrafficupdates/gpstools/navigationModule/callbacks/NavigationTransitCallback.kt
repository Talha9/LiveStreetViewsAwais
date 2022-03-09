package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.TransitWayPointModel

interface NavigationTransitCallback {
    fun gettingWaypointsData(model:TransitWayPointModel)
}