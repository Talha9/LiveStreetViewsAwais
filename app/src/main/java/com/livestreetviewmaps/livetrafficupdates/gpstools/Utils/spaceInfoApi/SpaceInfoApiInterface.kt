package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SpaceInfoApiInterface {

    @GET("rest.php/bodies/{planet}")
    fun getPlanetDetails(
        @Path("planet")planet:String): Call<SpaceInfoPlanetDetailsModel>
}