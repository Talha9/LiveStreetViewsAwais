package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.SpaceInfoApiInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.SpaceInfoPlanetDetailsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpaceInfoRepository(var mSpaceInfoApiInterface: SpaceInfoApiInterface) {
    var spaceInfoData = MutableLiveData<SpaceInfoPlanetDetailsModel>()
    var loading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    val TAG = "RepoLog:"

    fun getSpaceData(planet: String) {
        loading.value = true
        val result = mSpaceInfoApiInterface.getPlanetDetails(planet)
        result.enqueue(object : Callback<SpaceInfoPlanetDetailsModel> {
            override fun onResponse(
                call: Call<SpaceInfoPlanetDetailsModel>,
                response: Response<SpaceInfoPlanetDetailsModel>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        spaceInfoData.value = response.body()
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

            override fun onFailure(call: Call<SpaceInfoPlanetDetailsModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                loading.value = false
                errorMessage.value = t.localizedMessage
            }

        })
    }

}