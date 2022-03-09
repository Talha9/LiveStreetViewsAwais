package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models.HikingHomeModel

class HikingHomeHelper {
    companion object {
        fun fillHomeBtnList(): ArrayList<HikingHomeModel> {
            val list = ArrayList<HikingHomeModel>()
            list.add(HikingHomeModel("Go Walking", R.drawable.walking_icon))
            list.add(HikingHomeModel("Go Hiking", R.drawable.mountain_hiking))
            list.add(HikingHomeModel("Go Running", R.drawable.running_icon))
            list.add(HikingHomeModel("Go Cycling", R.drawable.bicycle_icon))
            list.add(HikingHomeModel("Go Other Activity", R.drawable.activity_icon))
            return list
        }
    }
}