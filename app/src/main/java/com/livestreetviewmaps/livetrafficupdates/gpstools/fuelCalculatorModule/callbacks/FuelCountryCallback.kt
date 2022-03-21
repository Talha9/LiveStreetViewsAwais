package com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.models.FuelCalculatorModel

interface FuelCountryCallback {
    fun onCountryItemClick(model:FuelCalculatorModel)
}