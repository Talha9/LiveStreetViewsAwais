package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm.SpaceInfoViewModel

class RestCountriesModelFactory(var mRestCountriesRepository: RestCountriesRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RestCountriesViewModel::class.java)) {
            RestCountriesViewModel(this.mRestCountriesRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}