package com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.helpers

import android.content.Context
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.models.OceansModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel

class OceansHelpers {
    companion object{
        fun fillOceansItemList():ArrayList<OceansModel>{
            val list=ArrayList<OceansModel>()
            list.add(OceansModel("Pacific Ocean",-8.0653798,-149.2396034, "https://drive.google.com/uc?export=download&id=1p40iy5QNF8fWQeKEZLTYpqaU8ony12CD","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(OceansModel("Atlantic Ocean",-1.288586,-31.4980414, "https://drive.google.com/uc?export=download&id=1awiVwU3TkXJnYPCAEN3y5y5XX3OZiqDc","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(OceansModel("Indian Ocean",-29.9160247,80.0024469, "https://drive.google.com/uc?export=download&id=1fvc6rm38fv9KB0mKRUrILSQoAz4tl9YR","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(OceansModel("Southern Ocean",-68.438005,-160.2428006, "https://drive.google.com/uc?export=download&id=1dnuQu-jgqSFUUJTfEFWMbFiCijHvZ4Pu","ygkWTK8nbmo","ygkWTK8nbmo"))
            list.add(OceansModel("Arctic Ocean",65.2070249,-66.8182757, "https://drive.google.com/uc?export=download&id=14nXrCAkS27zIePXfYXFSwpW65JRMlaOa","ygkWTK8nbmo","ygkWTK8nbmo"))
            return list
        }
    }
}