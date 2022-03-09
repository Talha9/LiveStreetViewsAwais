package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamsModel


interface WebCamCallback {
    fun onShareWebCamClick(model:WebCamsModel)
    fun onNavigateWebCamClick(model:WebCamsModel)
    fun onMapViewClick(model:WebCamsModel)
    fun onThumbnailClick(model:WebCamsModel)
}