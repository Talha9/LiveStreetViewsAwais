package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.horoscopeApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

object Api_Instance {
    val urlHoroscope = " https://aztro.sameerkumar.website "

    private var retrofitHoroscope: Retrofit? = null

    fun getRetrofitInstance(): Retrofit? {
        if (retrofitHoroscope == null) {
            try {
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(30, TimeUnit.SECONDS)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(30, TimeUnit.SECONDS)
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
                retrofitHoroscope = Retrofit.Builder()
                    .baseUrl(urlHoroscope)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return retrofitHoroscope
    }
}