package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel

class FuelApiViewModel(var mFuelApiRepository: FuelApiRepository):ViewModel() {
    var fuelRatesData=mFuelApiRepository.fuelRatesData
    var loading=mFuelApiRepository.loading
    var errorMessage=mFuelApiRepository.errorMessage
    val TAG = "RepoLog:"

    fun callForData(){
        mFuelApiRepository.getFuelRatesData()
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: View model")
        super.onCleared()
    }

}