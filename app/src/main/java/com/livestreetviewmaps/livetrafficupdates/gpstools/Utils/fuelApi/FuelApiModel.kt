package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi

data class FuelApiModel(
    val message: String,
    val response: List<Response>,
    val status: Int
)