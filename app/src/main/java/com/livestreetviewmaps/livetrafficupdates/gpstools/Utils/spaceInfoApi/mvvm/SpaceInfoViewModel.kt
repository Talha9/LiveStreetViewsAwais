package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel

class SpaceInfoViewModel(var mSpaceInfoRepository: SpaceInfoRepository):ViewModel() {
    var mSpaceData=mSpaceInfoRepository.spaceInfoData
    var loading=mSpaceInfoRepository.loading
    var errorMessage=mSpaceInfoRepository.errorMessage
    val TAG = "RepoLog:"

    fun callForData(planet:String){
        mSpaceInfoRepository.getSpaceData(planet)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: View model")
        super.onCleared()
    }
}