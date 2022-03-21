package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FuelApiInterface {

    @GET("api/getAllPrices")
    fun getFuelRates(): Call<FuelApiModel>
}