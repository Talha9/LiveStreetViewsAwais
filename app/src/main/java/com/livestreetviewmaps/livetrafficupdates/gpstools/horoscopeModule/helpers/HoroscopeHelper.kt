package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models.HoroscopeMainModel

object HoroscopeHelper {
    fun FillHoroscopeItemList():ArrayList<HoroscopeMainModel>{
        val list=ArrayList<HoroscopeMainModel>()
        list.add(HoroscopeMainModel("Aries", R.drawable.aries_icon,"0"))
        list.add(HoroscopeMainModel("Taurus", R.drawable.taurus_icon,"1"))
        list.add(HoroscopeMainModel("Gemini", R.drawable.geimni_icon,"2"))
        list.add(HoroscopeMainModel("Cancer", R.drawable.cancer_icon,"3"))
        list.add(HoroscopeMainModel("Leo", R.drawable.leo_icom,"4"))
        list.add(HoroscopeMainModel("Virgo", R.drawable.virgo_icon,"5"))
        list.add(HoroscopeMainModel("Libra", R.drawable.libra_icon,"6"))
        list.add(HoroscopeMainModel("Scorpio", R.drawable.scorpio_icon,"7"))
        list.add(HoroscopeMainModel("Sagittarius", R.drawable.sagittarius_icon,"8"))
        list.add(HoroscopeMainModel("Capricorn", R.drawable.capricorn_ikcon,"9"))
        list.add(HoroscopeMainModel("Aquarius ", R.drawable.aquarius_icon,"10"))
        list.add(HoroscopeMainModel("Pisces", R.drawable.pisces_icon,"11"))
        return list
    }
}