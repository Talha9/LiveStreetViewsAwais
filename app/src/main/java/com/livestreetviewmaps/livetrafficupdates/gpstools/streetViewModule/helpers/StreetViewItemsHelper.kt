package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel

class StreetViewItemsHelper {
    companion object{
        fun fillStreetCityViews():ArrayList<StreetViewModel>{
            val list=ArrayList<StreetViewModel>()
            list.add(StreetViewModel("https://drive.google.com/uc?export=download&id=1_LdLAEQD8cLjSNFOFRPmo1sRgj7BVy8R","Islamabad","Pakistan","assd","Faisal Mosque"))
            list.add(StreetViewModel("https://drive.google.com/uc?export=download&id=1A_y0wh084e0VkBSoJIrfX4brcKeXoJAF","Lahore","Pakistan","12321fgbdfg547vbn56","Badshahi Mosque"))
            list.add(StreetViewModel("https://drive.google.com/uc?export=download&id=1kuMUL7BD-Pj9c8gXNpg5CLFM7J38J7a_","Karachi","Pakistan","sd  asd6 346 hjk78 4","Sea Views"))
            list.add(StreetViewModel("https://drive.google.com/uc?export=download&id=1J4vPimjTj2wGybHYum3G7DDRK28citqF","Faisalabad","Pakistan","123 dgdb 678967 wq4r g","Clock Tower"))
            return list
        }
        fun fillStreetCountryViews():ArrayList<StreetViewModel>{
            val list=ArrayList<StreetViewModel>()
            list.add(StreetViewModel("aadd","Pakistan","sd asdsa sadsad asdsad sadsa sa","Faisel Mosque"))
            list.add(StreetViewModel("aadd","Pakistan","sd asdsa sadsad asdsad sadsa sa","Badshahi Mosque"))
            list.add(StreetViewModel("aadd","Pakistan","sd asdsa sadsad asdsad sadsa sa","Sea Views"))
            list.add(StreetViewModel("aadd","Pakistan","sd asdsa sadsad asdsad sadsa sa","Clock Tower"))
            return list
        }

    }
}