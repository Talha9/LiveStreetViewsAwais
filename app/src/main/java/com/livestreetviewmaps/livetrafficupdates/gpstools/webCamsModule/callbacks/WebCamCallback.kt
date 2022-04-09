package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.Webcam


interface WebCamCallback {
    fun onThumbnailClick(model: Webcam?)
}