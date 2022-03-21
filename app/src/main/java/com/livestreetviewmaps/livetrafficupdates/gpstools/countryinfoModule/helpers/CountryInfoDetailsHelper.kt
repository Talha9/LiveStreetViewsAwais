package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.helpers

import android.text.Html
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.models.CountryInfoDetailsParamsModel

class CountryInfoDetailsHelper {
    companion object{
        fun fillParamsNameList():ArrayList<CountryInfoDetailsParamsModel>{
            val list=ArrayList<CountryInfoDetailsParamsModel>()
            val ch=" "
            list.add(CountryInfoDetailsParamsModel("Name",ch))
            list.add(CountryInfoDetailsParamsModel("Population",ch))
            list.add(CountryInfoDetailsParamsModel("Area","m" + 0x00B2.toChar()))
            list.add(CountryInfoDetailsParamsModel("Timezone",ch))
            list.add(CountryInfoDetailsParamsModel("Currency",ch))
            list.add(CountryInfoDetailsParamsModel("Language",ch))
            list.add(CountryInfoDetailsParamsModel("Latitude",ch))
            list.add(CountryInfoDetailsParamsModel("Longitude",ch))
            return list
        }
    }
}