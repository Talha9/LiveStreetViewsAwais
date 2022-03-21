package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel

interface RestCountriesMainCallbacks {
    fun onCountryClick(model:CountriesModel)
}