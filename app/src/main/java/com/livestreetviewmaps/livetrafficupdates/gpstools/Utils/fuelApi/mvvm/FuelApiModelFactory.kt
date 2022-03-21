package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm.SpaceInfoViewModel

class FuelApiModelFactory(var mFuelApiRepository: FuelApiRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FuelApiViewModel::class.java)) {
            FuelApiViewModel(this.mFuelApiRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}