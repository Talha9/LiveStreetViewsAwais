package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.horoscopeApi

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface HoroscopeDao {
    @POST("/?")
    fun getHoroscopes(
        @Query("sign")sign:String?,
        @Query("day")day:String?
    ):Call<HoroscopeItemModel?>
}