package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SpaceInfoApiInstance {
    fun getInstance(): Retrofit {
        val client = OkHttpClient.Builder()
        client.connectTimeout(1, TimeUnit.MINUTES)
        client.readTimeout(1, TimeUnit.MINUTES)
        client.writeTimeout(1, TimeUnit.MINUTES)

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)

        return Retrofit.Builder()
            .baseUrl("https://api.le-systeme-solaire.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}