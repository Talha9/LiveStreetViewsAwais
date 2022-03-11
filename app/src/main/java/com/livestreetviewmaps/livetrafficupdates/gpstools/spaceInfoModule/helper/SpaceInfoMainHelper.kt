package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.helper

import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.models.SpaceInfoMainModel

object SpaceInfoMainHelper {
    fun fillMainSphareTagList():ArrayList<SpaceInfoMainModel>{
        val list=ArrayList<SpaceInfoMainModel>()
        list.add(SpaceInfoMainModel("Earth", R.drawable.earth_planet))
        list.add(SpaceInfoMainModel("Uranus", R.drawable.uranus_planet))
        list.add(SpaceInfoMainModel("Pluto", R.drawable.pluto_planet))
        list.add(SpaceInfoMainModel("Neptune", R.drawable.naptune_planet))
        list.add(SpaceInfoMainModel("Mars", R.drawable.mars_planet))
        list.add(SpaceInfoMainModel("Dione", R.drawable.dione_planet))
        list.add(SpaceInfoMainModel("Europa", R.drawable.euorpa_planet))
        list.add(SpaceInfoMainModel("IO", R.drawable.io_planet))
        list.add(SpaceInfoMainModel("Jupiter", R.drawable.jupiter_planet))
        list.add(SpaceInfoMainModel("Mercury", R.drawable.mercury_planet))
        list.add(SpaceInfoMainModel("Moon", R.drawable.moon_planets))
        list.add(SpaceInfoMainModel("Proteus", R.drawable.proteus_planet))
/*
        list.add(SpaceInfoMainModel("Satrun", R.drawable.satrun_planet))
*/
        list.add(SpaceInfoMainModel("Titan", R.drawable.titan_planet))
        return list
    }



}