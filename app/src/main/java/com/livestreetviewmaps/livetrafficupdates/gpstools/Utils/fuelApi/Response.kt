package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi

data class Response(
    val country_name: String,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val petrol_price: String,
    val short_form: String,
    val updated_at: String
)