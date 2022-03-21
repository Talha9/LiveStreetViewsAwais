package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.FavouritesTable

interface FavouriteSavedCallback {
    fun onSavedFavouriteClick(model:FavouritesTable)
}