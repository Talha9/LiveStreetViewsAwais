package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SpaceInfoModelFactory(var mSpaceInfoRepository: SpaceInfoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SpaceInfoViewModel::class.java)) {
            SpaceInfoViewModel(this.mSpaceInfoRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}