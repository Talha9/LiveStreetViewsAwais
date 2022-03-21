package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAdModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds

class MyAppClass: Application() {

    companion object {
        var myApplication: Application? = null
        fun getStr(id: Int): String {
            return myApplication!!.getString(id)
        }
    }

    override fun onCreate() {
        super.onCreate()
        myApplication = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(p0: InitializationStatus) {
            }
        })

        AudienceNetworkAds.initialize(this)
        Firebase.initialize(this)
        //getDataFromFirebase()

    }


    private fun getDataFromFirebase() {
        try {
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("LiveStreetViewSpace")
            databaseReference.child("RelesAds").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val model = dataSnapshot.getValue<LiveStreetViewMyAdModel>(
                        LiveStreetViewMyAdModel::class.java
                    )
                    if (model != null) {
                        LiveStreetViewMyAppAds.haveGotSnapshot = true
                        LiveStreetViewMyAppAds.interstitial_admob_inApp = model.interstitial_admob_inApp
                        LiveStreetViewMyAppAds.banner_admob_inApp = model.banner_admob_inApp
                        LiveStreetViewMyAppAds.native_admob_inApp = model.native_admob_inApp
                        LiveStreetViewMyAppAds.shouldShowAdmob = model.isShould_show_admob
                        LiveStreetViewMyAppAds.next_ads_time = model.next_ads_time.toLong()
                        LiveStreetViewMyAppAds.current_counter = model.current_counter


                        Log.i("LoadAds", "interstitial_admob:  " + model.interstitial_admob_inApp)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i("LoadAds", databaseError.message)
                }
            })
        } catch (e: Exception) {
        }
    }

}