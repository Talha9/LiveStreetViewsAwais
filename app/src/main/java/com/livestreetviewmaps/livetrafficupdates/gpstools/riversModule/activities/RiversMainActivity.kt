package com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.activities

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityRiversMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.activities.DesertsDetailsActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.adapters.DesertsAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.callbacks.DesertsCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.helpers.DesertsHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.adapters.RiversAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.callbacks.RiversCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.helpers.RiversHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.models.RiversModel

class RiversMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityRiversMainBinding?=null
    var manager: GridLayoutManager?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    var adapter:RiversAdapter?=null
    var list:ArrayList<RiversModel>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRiversMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        initializers()
        setUpHeader()
        listFiller()
        setUpAdapter()
        onClickListeners()
    }
    private fun initializers() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)
    }

    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text=" Top Rivers"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun onClickListeners() {

    }

    private fun listFiller() {
        list= RiversHelper.fillRiversItemList()
    }

    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        binding!!.riversRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= RiversAdapter(this,list!!,object : RiversCallback {
                override fun onRiverClick(model: RiversModel) {
                    val intent= Intent(this@RiversMainActivity, RiversDetailsActivity::class.java)
                    intent.putExtra("rivers_model",model)
                    startActivity(intent)
                }

            })
            binding!!.riversRecView.adapter=adapter
            binding!!.progressMainBg.visibility= View.GONE
        }
    }



    override fun networkAvailable() {
        try {
            internetDialog!!.dismiss()
        } catch (e: Exception) {
        }    }

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