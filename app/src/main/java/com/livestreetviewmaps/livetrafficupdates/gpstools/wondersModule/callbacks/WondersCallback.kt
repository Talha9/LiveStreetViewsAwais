package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel

interface WondersCallback {
    fun onWonderClick(model: WondersModel)
}