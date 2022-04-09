package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.*
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.WebCamApiInstance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.WebCamApiInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.Webcam
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.mvvm.WindiCamModelFactory
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.mvvm.WindiCamRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.mvvm.WindiCamViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamsMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter.WebCamAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamCountryModel

class WebCamsMainActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener,
    LocationDialogCallback {
    var binding: ActivityWebCamsMainBinding? = null
    var manager: LinearLayoutManager? = null
    var adapter: WebCamAdapter? = null
    var list: ArrayList<Webcam>? = null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    var dataModel: WebCamCountryModel? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    lateinit var mWindiCamViewModel: WindiCamViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebCamsMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (intent.getParcelableExtra<WebCamCountryModel>("country_model") != null) {
            try {
                val model = intent.getParcelableExtra<WebCamCountryModel>("country_model")
                if (model != null) {
                    dataModel = model
                    binding!!.header.headerBarTitleTxt.text = model.country
                    getWindiApiData(dataModel!!.code)
                }
            } catch (e: Exception) {
            }
        }



        initializers()
        setUpHeader()
        mBannerAdsSmall()
    }

    private fun getWindiApiData(code: String) {
        Log.d("mSpaceInfoViewModelTAG", "apiCalling: " +code)
        val webCamRetrofit = WebCamApiInstance.getInstance().create(WebCamApiInterface::class.java)

        val mWindiCamRepository = WindiCamRepository(webCamRetrofit)
        mWindiCamViewModel =
            ViewModelProvider(this, WindiCamModelFactory(mWindiCamRepository)).get(
                WindiCamViewModel::class.java
            )

        mWindiCamViewModel.callForData(
            code,
            20,
            "eNmZSWAlfyYauYpg6c8fphsqtQR2wVR0",
            "webcams:player,image,live"
        )
        mWindiCamViewModel.mWindiCam.observe(this, {
            if (it != null) {
                list = it.result.webcams as ArrayList<Webcam>
                Log.d("mWindiCamViewModelTAG", "getWindiApiData: "+ list!!.size)
                setUpAdapter(list)
            }
        })


    }


    private fun setUpAdapter(list: ArrayList<Webcam>?) {
        Log.d("setUpAdapterTAG", "setUpAdapter: "+list!!.size)
        manager = LinearLayoutManager(this)
        binding!!.recyclerView.layoutManager = manager
        if (this.list != null && this.list!!.size > 0) {
            adapter = WebCamAdapter(this, list, object : WebCamCallback {

                override fun onThumbnailClick(model: Webcam?) {
                    val intent =
                        Intent(this@WebCamsMainActivity, WebCamVideoPlayerActivity::class.java)
                    intent.putExtra("video_url", model!!.player.lifetime.embed.toString())
                    startActivity(intent)
                }

            })
            binding!!.recyclerView.adapter = adapter
            binding!!.progressMainBg.visibility = View.GONE
        }
    }

    private fun initializers() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)

        mFetchLocation = LocationClass(this)
        mFetchLocation.initLocationRequest()
        gpsEnableDialog = LocationDialog(this, this)
        mLocationService = LocationService(this, gpsEnableDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }


    }

    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text = "Web Cams"
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
        unregisterReceiver(mLocationService)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsEnableDialog!!.show()
        } else {
            UtilsFunctionClass.askForPermissions(this)
            Log.d("onResumeTAG", "onResume: ")
        }
    }

    private fun shareMyLocation(loc: String) {
        try {
            val shareAppIntent = Intent(Intent.ACTION_SEND)
            shareAppIntent.type = "text/plain"
            val shareSub = "This is my Location!"
            val shareBody = loc
            shareAppIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            shareAppIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(shareAppIntent, "Share location using..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onEnabledGPS() {
        try {
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(callGPSSettingIntent)
        } catch (e: Exception) {
        }
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
                binding!!.smallAd.adContainer, adView, this
            )
        } else {
            binding!!.smallAd.root.visibility = View.GONE
        }
    }
}