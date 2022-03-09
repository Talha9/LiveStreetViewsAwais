package com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.callbacks

import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.model.TallestPeakModel

interface TallestPeaksCallback {
    fun onTallestPeakClick(model:TallestPeakModel)
}