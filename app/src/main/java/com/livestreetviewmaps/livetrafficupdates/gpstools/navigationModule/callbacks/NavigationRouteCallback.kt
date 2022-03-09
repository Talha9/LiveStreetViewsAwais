package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.NavigationRouteButtonsModel

interface NavigationRouteCallback {
    fun onRouteButtonClick(model: NavigationRouteButtonsModel, position: Int)
}