package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm

import androidx.lifecycle.MutableLiveData
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.FuelApiInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.FuelApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FuelApiRepository(var mFuelApiInterface: FuelApiInterface) {
    var fuelRatesData = MutableLiveData<FuelApiModel>()
    var loading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    val TAG = "RepoLog:"

    fun getFuelRatesData() {
        loading.value = true
        val result = mFuelApiInterface.getFuelRates()
        result.enqueue(object : Callback<FuelApiModel> {
            override fun onResponse(call: Call<FuelApiModel>, response: Response<FuelApiModel>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        fuelRatesData.value = response.body()
                    } else {
                        errorMessage.value = response.message()
                    }
                    loading.value = false
                } else {
                    loading.value = false
                    errorMessage.value = response.message()
                }

            }

            override fun onFailure(call: Call<FuelApiModel>, t: Throwable) {
                loading.value = false
                errorMessage.value = t.localizedMessage
            }

        })
    }
}