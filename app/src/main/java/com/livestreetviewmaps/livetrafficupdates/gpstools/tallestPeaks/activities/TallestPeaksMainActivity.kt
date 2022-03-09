package com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityTallestPeaksMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.adapters.TallestPeaksAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.callbacks.TallestPeaksCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.helpers.TallestPeaksHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.model.TallestPeakModel


class TallestPeaksMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityTallestPeaksMainBinding?=null
    var adapter:TallestPeaksAdapter?=null
    var manager:GridLayoutManager?=null
    var list:ArrayList<TallestPeakModel>?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTallestPeaksMainBinding.inflate(layoutInflater)
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
        binding!!.header.headerBarTitleTxt.text="Tallest Peaks"
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun onClickListeners() {

    }
    private fun listFiller(){
        list=TallestPeaksHelper.fillTallestPeaksItemList()
    }
    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        binding!!.tallestPeaksRecView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= TallestPeaksAdapter(this,list!!,object : TallestPeaksCallback {
                override fun onTallestPeakClick(model: TallestPeakModel) {
                    val intent= Intent(this@TallestPeaksMainActivity, TallestPeaksDetailsActivity::class.java)
                    intent.putExtra("tallest_peaks_model",model)
                    startActivity(intent)
                }


            })
            binding!!.tallestPeaksRecView.adapter=adapter
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