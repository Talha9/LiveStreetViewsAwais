package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.NavigationModel

class NavigationHelper {
    companion object{
        fun navigationListFillHelper():ArrayList<NavigationModel>{
            val list=ArrayList<NavigationModel>()
            list.add(NavigationModel("Navigation", R.drawable.navigation_icon,1))
            list.add(NavigationModel("Voice", R.drawable.voice_navigation,2))
            list.add(NavigationModel("Route", R.drawable.roure_finder,3))
            list.add(NavigationModel("Transit", R.drawable.transit_route,4))

            return list
        }



    }
}