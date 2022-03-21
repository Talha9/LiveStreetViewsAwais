package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi

import retrofit2.Call
import retrofit2.http.GET

interface RestCountriesInterface {
    @GET("/v2/all")
    fun getCountriesInfo(): Call<ArrayList<CountriesModel>>
}