package com.livestreetviewmaps.livetrafficupdates.gpstools.compassModule.Callbacks

interface CompassCallbacks {
    fun onNewAzimuth(azimuth: Float)
    fun getXYValues(x: Float, y: Float)
}