package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.helpers

import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamsModel

class WebCamHelper {
    companion object{
        fun fillWebCamList():ArrayList<WebCamsModel>{
            val list=ArrayList<WebCamsModel>()
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))
            list.add(WebCamsModel("ygkWTK8nbmo","Great Wall",33.1234,74.1234,"China","Wong Yan"))

            return list
        }
    }
}