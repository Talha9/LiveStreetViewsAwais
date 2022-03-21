package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.helpers

import android.content.Context
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel

class WonderHelpers {
    companion object{
        fun fillWonderItemList():ArrayList<WondersModel>{
            val list=ArrayList<WondersModel>()
            list.add(WondersModel("Giza Pyramids",29.9769531,31.1238963,  "https://drive.google.com/uc?export=download&id=1m8LX-Mkvp6a6-87T06DenbQE-7mktkS8","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Great Wall of China",40.4319118,116.5681862,  "https://drive.google.com/uc?export=download&id=1XkI_4f2gBwjTX4hkCCIkzXecxiWo_ppZ","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Petra",30.328459,35.4421735,  "https://drive.google.com/uc?export=download&id=1p0G97KGsV4mmIjcQ9tQ9ghEHy-nGUQZx","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Colosseum",41.8902142,12.4900422,  "https://drive.google.com/uc?export=download&id=11I8Z-MenFrwRNIaO1-5EYlDR2daKYtlr","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Chichén Itzá",20.6828175,-88.5727964,  "https://drive.google.com/uc?export=download&id=1GXayPtrKW6KZ34lwbE-vxoEZArhZ_K7o","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Machu Picchu",-13.163136,-72.5471516,  "https://drive.google.com/uc?export=download&id=1m2ZO0_LTCTkc7IhHFp3Hsy6vc22n1N1o","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Taj Mahal",27.1751496,78.0399535,  "https://drive.google.com/uc?export=download&id=1siLwl3zFiDZ-oBjv5bglXLrPyAagwzLB","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(WondersModel("Christ the Redeemer",-22.951911,-43.2126759,  "https://drive.google.com/uc?export=download&id=1-m2-Gjjiu6PbhctL-ROvNjVzTIBMiXJ","ygkWTK8nbmo","ygkWTK8nbmo"))
            return list
        }
    }
}