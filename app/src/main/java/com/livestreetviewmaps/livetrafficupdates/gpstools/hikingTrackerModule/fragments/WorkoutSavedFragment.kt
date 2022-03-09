package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.viewModel.LiveStreetViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.FragmentWorkoutSavedBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.adapters.HikingSavedAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.HikingSavedCallback

class WorkoutSavedFragment : Fragment() {

    var mLiveStreetViewModel: LiveStreetViewModel? = null
    var binding: FragmentWorkoutSavedBinding? = null
    var dataList= ArrayList<HikingTable>()
    var adapter: HikingSavedAdapter? = null
    var manager: LinearLayoutManager? = null
    lateinit var mContext: Context

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkoutSavedBinding.inflate(layoutInflater)
        mLiveStreetViewModel = ViewModelProvider(this)[LiveStreetViewModel::class.java]



        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mLiveStreetViewModel = ViewModelProvider(this)[LiveStreetViewModel::class.java]
        mLiveStreetViewModel!!.getAllActivities().observe(this,
            {
                dataList=it as ArrayList<HikingTable>
                for (i in it){
                    Log.d("mLiveStreetViewModelTAG", "onViewCreated: "+i.activityName)
                }
                setupAdapter(dataList)

            })

    }

    fun setupAdapter(dataList: ArrayList<HikingTable>) {
        manager = LinearLayoutManager(mContext)
        binding!!.hikingSavedRecView.layoutManager = manager
        adapter = HikingSavedAdapter(
            mContext, dataList, object : HikingSavedCallback {
                override fun onDeleteHikingBtnClick(model: HikingTable, pos: Int) {
                    deleteSpecificActivityData(model.id,pos)
                }

                override fun onHikingActivityClick(model: HikingTable) {
                    Toast.makeText(mContext, "Ooops!", Toast.LENGTH_SHORT).show()
                }

            })
        binding!!.hikingSavedRecView.adapter = adapter
    }
    private fun deleteSpecificActivityData(id: Int, pos: Int) {
        mLiveStreetViewModel!!.delete(id)
        dataList.removeAt(pos)
        adapter!!.notifyItemRemoved(pos)
        adapter!!.notifyItemRangeChanged(pos,dataList.size)
        Toast.makeText(mContext, "Data Delete Successfully!", Toast.LENGTH_SHORT).show()
    }

}