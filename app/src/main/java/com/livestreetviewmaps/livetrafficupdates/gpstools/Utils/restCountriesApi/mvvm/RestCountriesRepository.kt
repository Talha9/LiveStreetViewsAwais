package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm

import android.app.Application
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.RestCountriesInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestCountriesRepository(
    var mRestCountriesInterface: RestCountriesInterface
) {
    var restCountriesData = MutableLiveData<ArrayList<CountriesModel>>()
    var loading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    val TAG = "RepoLog:"

    fun getCountriesData() {
        loading.value = true
        val result = mRestCountriesInterface.getCountriesInfo()
        result.enqueue(object :Callback<ArrayList<CountriesModel>>{
            override fun onResponse(
                call: Call<ArrayList<CountriesModel>>,
                response: Response<ArrayList<CountriesModel>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        restCountriesData.value = response.body()
                    } else {
                        errorMessage.value = response.message()
                    }
                    loading.value = false
                } else {
                    Log.d(TAG, "getFmData: Un success")
                    loading.value = false
                    errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<ArrayList<CountriesModel>>, t: Throwable) {
                loading.value = false
                errorMessage.value = t.localizedMessage
            }

        })
    }



}