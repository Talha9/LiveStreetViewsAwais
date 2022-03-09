package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.TransitRoutesModel

interface onRouteTransitCallback {
     fun onItemRemovedClick(position:Int)
    fun onEditTxtClick(position: Int,model: TransitRoutesModel)
}