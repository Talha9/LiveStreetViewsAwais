package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.FragmentWorkoutHomeBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.activities.HikingMapActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.adapters.HikingHomeAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.HikingHomeCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.helpers.HikingHomeHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models.HikingHomeModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds

class WorkoutHomeFragment : Fragment() {

    var binding: FragmentWorkoutHomeBinding? = null
    var homeAdapter: HikingHomeAdapter? = null
    var homeManager: LinearLayoutManager? = null
    var homeList = ArrayList<HikingHomeModel>()
    var mContext: Context? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutHomeBinding.inflate(layoutInflater)

        listFiller()

        setUpHomeBtnAdapter()

        return binding!!.root
    }

    private fun listFiller() {
        homeList = HikingHomeHelper.fillHomeBtnList()
    }


    private fun setUpHomeBtnAdapter() {
        homeManager = LinearLayoutManager(mContext)
        binding!!.hikingHomeRecView.layoutManager = homeManager
        if (homeList.size>0) {
            try {
                homeAdapter = HikingHomeAdapter(mContext!!, homeList, object : HikingHomeCallback {
                    override fun onWorkoutClick(model: HikingHomeModel, pos: Int) {
                        val mIntent = Intent(mContext, HikingMapActivity::class.java)
                        mIntent.putExtra("hiking_home_model", model)
                        LiveStreetViewMyAppShowAds.meidationForClickSimpleLiveStreetView(mContext,LiveStreetViewMyAppAds.admobInterstitialAd,mIntent)
                    }
                })
                binding!!.hikingHomeRecView.adapter=homeAdapter
            } catch (e: Exception) {
            }
        }
    }


}