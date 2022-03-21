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
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityOceansMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
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
        mBannerAdsSmall()
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

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    private fun listFiller() {
        list= OceansHelpers.fillOceansItemList()
    }

    private fun setUpAdapter() {
        manager= GridLayoutManager(this,2)
        manager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = adapter!!.getItemViewType(position)
                if (itemViewType == 0) {
                    //manager.getSpanCount();
                } else if (itemViewType == 1) {
                    return 1
                } else if (itemViewType == 2) {
                    manager!!.spanCount
                } else {
                    return 2
                }
                return 2
            }
        }
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

    private fun mBannerAdsSmall() {
        val billingHelper =
            LiveStreetViewBillingHelper(
                this
            )
        val adView = com.google.android.gms.ads.AdView(this)
        adView.adUnitId = LiveStreetViewMyAppAds.banner_admob_inApp
        adView.adSize = AdSize.BANNER

        if (billingHelper.isNotAdPurchased()) {
            LiveStreetViewMyAppAds.loadEarthMapBannerForMainMediation(
                binding!!.smallAd.adContainer,adView,this
            )
        }else{
            binding!!.smallAd.root.visibility= View.GONE
        }
    }
}