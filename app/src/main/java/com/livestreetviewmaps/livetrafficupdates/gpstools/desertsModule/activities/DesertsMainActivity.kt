package com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.adapters.DesertsAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.callbacks.DesertsCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.helpers.DesertsHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityDesertsMainBinding

class DesertsMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityDesertsMainBinding?=null
    var manager: GridLayoutManager?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    var adapter:DesertsAdapter?=null
    var list:ArrayList<DesertsModel>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDesertsMainBinding.inflate(layoutInflater)
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
        binding!!.header.headerBarTitleTxt.text=" Top Deserts"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun onClickListeners() {

    }

    private fun listFiller() {
        list= DesertsHelper.fillDesertsItemList()
    }

    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        binding!!.desertsRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= DesertsAdapter(this,list!!,object : DesertsCallback {
                override fun onDesertClick(model: DesertsModel) {
                    val intent= Intent(this@DesertsMainActivity, DesertsDetailsActivity::class.java)
                    intent.putExtra("deserts_model",model)
                    startActivity(intent)
                }

            })
            binding!!.desertsRecView.adapter=adapter
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