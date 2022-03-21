package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityHoroscopeMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.adapters.HoroscopeAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.callbacks.HoroscopeCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.helpers.HoroscopeHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models.HoroscopeMainModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds

class HoroscopeMainActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityHoroscopeMainBinding? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var list = ArrayList<HoroscopeMainModel>()
    var adapter: HoroscopeAdapter? = null
    var manager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoroscopeMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        listFiller()
        initialization()
        setUpHeader()
        setUpAdapter()
        mBannerAdsSmall()
    }

    private fun setUpAdapter() {
        manager = GridLayoutManager(this, 3)
        manager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = adapter!!.getItemViewType(position)
                if (itemViewType == 0) {
                    //manager.getSpanCount();
                } else if (itemViewType == 1) {
                    return 1
                } else if (itemViewType == 3) {
                    manager!!.spanCount
                } else {
                    return 3
                }
                return 3
            }
        }
        binding!!.horoscopeRecView.layoutManager = manager
        adapter = HoroscopeAdapter(this, list, object : HoroscopeCallbacks {
            override fun onHoroscopeClick(model: HoroscopeMainModel) {
                val intent =
                    Intent(this@HoroscopeMainActivity, HoroscopeDetailsActivity::class.java)
                intent.putExtra("horoscope_model", model)
                startActivity(intent)
            }

        })
        binding!!.horoscopeRecView.adapter = adapter
    }

    private fun listFiller() {
        list = HoroscopeHelper.FillHoroscopeItemList()
    }

    private fun initialization() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
    }

    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text = "Daily Horoscope"
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