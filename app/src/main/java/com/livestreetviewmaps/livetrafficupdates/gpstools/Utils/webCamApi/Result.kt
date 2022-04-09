package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi

data class Result(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val webcams: List<Webcam>
)