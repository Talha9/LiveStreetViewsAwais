package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi

data class Image(
    val current: Current,
    val daylight: Daylight,
    val sizes: Sizes,
    val update: Int
)