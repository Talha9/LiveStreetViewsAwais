package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.SpaceInfoPlanetDetailsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebCamApiInterface {
    @GET("list/country={country}/limit={limit}")
    fun getWindiCams(
        @Path("country") country: String,
        @Path("limit") limit: Int,
        @Query("key") key: String,
        @Query("show") show: String
    ): Call<WindiCamModel>
}