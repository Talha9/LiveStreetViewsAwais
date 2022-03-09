package com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityNearbyMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.adapters.NearByAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.callbacks.NearByCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.helpers.NearByHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.models.NearByModel

class NearbyMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityNearbyMainBinding?=null
    var list:ArrayList<NearByModel>?=null
    var manager:GridLayoutManager?=null
    var adapter:NearByAdapter?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNearbyMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        listFiller()
        initialization()
        setUpHeader()
    }

    private fun setupAdapter() {
        manager= GridLayoutManager(this,3)
        binding!!.nearbyRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= NearByAdapter(this,list!!,object :NearByCallbacks{
                override fun onNearByCategoryClick(model: NearByModel) {
                    val intent=Intent(this@NearbyMainActivity,NearByActivity::class.java)
                    intent.putExtra("near_By",model)
                    startActivity(intent)
                }
            })
            binding!!.nearbyRecView.adapter=adapter
            binding!!.progressMainBg.visibility= View.GONE
        }
    }

    private fun listFiller() {
        list=NearByHelper.fillNearByCategoryList()
        setupAdapter()
    }

    private fun initialization() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)
    }
    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text="Near By"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun networkAvailable() {
        try {
            internetDialog!!.dismiss()
        } catch (e: Exception) {
        }
    }

    override fun networkUnavailable() {
        try {
            internetDialog!!.show()
            internetDialog!!.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, i, keyEvent ->
                if (i == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                }
                return@OnKeyListener false
            })
        } catch (e: Exception) {
        }
    }
    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
    }

}