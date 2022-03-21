package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.*
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.MapNavigation.MapNavigationActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.MapNavigation.model.NavigationModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamsMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter.WebCamAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.helpers.WebCamHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamMapMarkerLocationModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamVideoLinkModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamsModel

class WebCamsMainActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener,
    LocationDialogCallback {
    var binding:ActivityWebCamsMainBinding?=null
    var manager:LinearLayoutManager?=null
    var adapter:WebCamAdapter?=null
    var list:ArrayList<WebCamsModel>?=null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWebCamsMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        listFiller()
        initializers()
        setUpHeader()
        setUpAdapter()
        mBannerAdsSmall()
    }

    private fun listFiller() {
        list=WebCamHelper.fillWebCamList()
    }

    private fun setUpAdapter() {
       manager= LinearLayoutManager(this)
        binding!!.recyclerView.layoutManager=manager
        if (list!=null && list!!.size>0) {
            adapter= WebCamAdapter(this,list!!,object :WebCamCallback{
                override fun onShareWebCamClick(model: WebCamsModel) {
                    val address =
                        "${model.placeName}\nhttps://maps.google.com/maps?q=@${model.placeLatitude},${model.placeLongitude}"
                    shareMyLocation(address)
                }
                override fun onNavigateWebCamClick(model: WebCamsModel) {
                    val intent=Intent(this@WebCamsMainActivity,MapNavigationActivity::class.java)
                    intent.putExtra("navigation_model",NavigationModel(constants.mLatitude,constants.mLongitude,model.placeLatitude,model.placeLongitude,0))
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + constants.mLatitude + "," + constants.mLongitude+ model.placeLatitude + "," + model.placeLongitude)
                    if (Build.VERSION.SDK_INT<31) {
                       startActivity(intent)
                    }else{
                        Toast.makeText(this@WebCamsMainActivity,"Feature Not Available!",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onMapViewClick(model: WebCamsModel) {
                    val intent=Intent(this@WebCamsMainActivity,WebCamMapActivity::class.java)
                    intent.putExtra("map_loc",WebCamMapMarkerLocationModel(model.placeLatitude,model.placeLongitude,model.placeName))
                    startActivity(intent)
                }

                override fun onThumbnailClick(model: WebCamsModel) {
                    val intent=Intent(this@WebCamsMainActivity,WebCamVideoPlayerActivity::class.java)
                    intent.putExtra("video_url",WebCamVideoLinkModel(model.placeName,model.webCamUrl))
                    startActivity(intent)
                }

            })
            binding!!.recyclerView.adapter=adapter
            binding!!.progressMainBg.visibility= View.GONE
        }
    }

    private fun initializers() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)

        mFetchLocation = LocationClass(this)
        mFetchLocation.initLocationRequest()
        gpsEnableDialog= LocationDialog(this,this)
        mLocationService=LocationService(this, gpsEnableDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }


    }
    private fun setUpHeader() {
        binding!!.header.headerBarTitleTxt.text="Web Cams"
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
                binding!!.smallAd.adContainer,adView,this
            )
        }else{
            binding!!.smallAd.root.visibility= View.GONE
        }
    }
}