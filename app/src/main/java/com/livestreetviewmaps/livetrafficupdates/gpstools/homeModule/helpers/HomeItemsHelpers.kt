package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models.HomeMainCategoryModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models.HomeMainSubCategoryModel

class HomeItemsHelpers {
    companion object{
        fun FillHomeitemsList():ArrayList<HomeMainCategoryModel>{
            val list=ArrayList<HomeMainCategoryModel>()
            list.add(HomeMainCategoryModel("Tools",toolsList()))
            list.add(HomeMainCategoryModel("Navigation",NavigationList()))
            list.add(HomeMainCategoryModel("Home",HomeList()))
            list.add(HomeMainCategoryModel("Saved",toolsList()))
            list.add(HomeMainCategoryModel("Settings",settingList()))
            return list
       }

        private fun settingList(): java.util.ArrayList<HomeMainSubCategoryModel>{
            val sublist=ArrayList<HomeMainSubCategoryModel>()
            sublist.add(HomeMainSubCategoryModel(R.drawable.remove_ads_ic,"Remove Ads",R.color.settingsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.rating_ic,"Rate Us",R.color.settingsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.more_apps_ic,"More App",R.color.settingsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.privicy_policy_ic,"Privacy \nPolicy",R.color.settingsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.feedback_ic,"Feedback",R.color.settingsThemeColor,R.drawable.home_tools_item_bg))
         return sublist
        }

        private fun toolsList(): ArrayList<HomeMainSubCategoryModel> {
            val sublist=ArrayList<HomeMainSubCategoryModel>()
            sublist.add(HomeMainSubCategoryModel(R.drawable.sensor_icon,"Sensors",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.compass_icon,"Compass",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.fuel_calculator_ic,"Fuel \nCalculator",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.speedometer_icon,"Speedometer",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.country_info,"Country \nInfo",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.iso_code,"ISO \nCode",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.age_calculatopre,"Age \nCalculator",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.world_clock_fm,"World \nTime",R.color.toolsThemeColor,R.drawable.home_tools_item_bg))
            return sublist
        }
        private fun NavigationList(): ArrayList<HomeMainSubCategoryModel> {
            val sublist=ArrayList<HomeMainSubCategoryModel>()
            sublist.add(HomeMainSubCategoryModel(R.drawable.navigtion_icon,"Navigation",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.my_location,"My Location",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.earth_map,"Earth \nMap",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.daily_horoscape,"Daily \nHoroscope",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            //sublist.add(HomeMainSubCategoryModel(R.drawable.network_antenas,"Network \nAntenna",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.gps_hikking,"Hiking \nTracker",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.space_info,"Space Info",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
           // sublist.add(HomeMainSubCategoryModel(R.drawable.footpath_step,"Step \nCounter",R.color.navigationThemeColor,R.drawable.home_navigation_item_bg))
            return sublist
        }
        private fun HomeList(): ArrayList<HomeMainSubCategoryModel> {
            val sublist=ArrayList<HomeMainSubCategoryModel>()
            sublist.add(HomeMainSubCategoryModel(R.drawable.street_view,"Street \nViews",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.nearby_places,"Nearby",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.wonder_places,"Wonders",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.oceans_waves,"Oceans",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.tallest_peaks,"Tallest \nPeaks",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.desert_places,"Top \nDeserts",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.river_area,"Rivers",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            sublist.add(HomeMainSubCategoryModel(R.drawable.web_cams,"Web \nCams",R.color.homeThemeColor,R.drawable.home_home_item_bg))
            return sublist
        }
    }
}