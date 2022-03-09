package com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.activities

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityOceansMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.adapters.OceansAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.callbacks.OceansCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.helpers.OceansHelpers
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.models.OceansModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.activities.WonderDetailsActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.adapters.WonderAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks.WondersCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.helpers.WonderHelpers

class OceansMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityOceansMainBinding?=null
    var adapter: OceansAdapter?=null
    var manager:GridLayoutManager?=null
    var list:ArrayList<OceansModel>?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOceansMainBinding.inflate(layoutInflater)
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
        binding!!.header.headerBarTitleTxt.text="Oceans"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }


    private fun onClickListeners() {

    }

    private fun listFiller() {
        list= OceansHelpers.fillOceansItemList()
    }

    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        binding!!.oceansRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= OceansAdapter(this,list!!,object : OceansCallback {
                override fun onOceansClick(model: OceansModel) {
                    val intent= Intent(this@OceansMainActivity, OceansDetailsActivity::class.java)
                    intent.putExtra("oceans_model",model)
                    startActivity(intent)
                }

            })
            binding!!.oceansRecView.adapter=adapter
            binding!!.progressMainBg.visibility= View.GONE
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