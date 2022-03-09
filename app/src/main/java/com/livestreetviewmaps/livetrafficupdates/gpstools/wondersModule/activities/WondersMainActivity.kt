package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.activities

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWondersMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.adapters.WonderAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks.WondersCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.helpers.WonderHelpers

class WondersMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityWondersMainBinding?=null
    var adapter:WonderAdapter?=null
    var manager:GridLayoutManager?=null
    var list:ArrayList<WondersModel>?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWondersMainBinding.inflate(layoutInflater)
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
        binding!!.header.headerBarTitleTxt.text="Wonders"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onClickListeners() {

    }

    private fun listFiller() {
        list=WonderHelpers.fillWonderItemList()
    }

    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        binding!!.wondersRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= WonderAdapter(this,list!!,object :WondersCallback{
                override fun onWonderClick(model: WondersModel) {
                    val intent=Intent(this@WondersMainActivity,WonderDetailsActivity::class.java)
                    intent.putExtra("wonder_model",model)
                    startActivity(intent)
                }

            })
            binding!!.wondersRecView.adapter=adapter
            binding!!.progressMainBg.visibility=View.GONE
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