package com.livestreetviewmaps.livetrafficupdates.gpstools.speedometerModule.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.google.android.gms.location.*
import com.google.maps.android.SphericalUtil
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivitySpeedometerMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds
import java.text.DecimalFormat

class SpeedometerMainActivity : AppCompatActivity(), LocationDialogCallback {
    private var isStart = true
    private var isStartBtn = true
    lateinit var binding: ActivitySpeedometerMainBinding
    lateinit var mLocationService: LocationService
    private var mySpeed: Double = 0.0
    private var maxSpeed: Double = 0.0
    private var minSpeed: Double = 0.0
    private var avgSpeed: Double = 0.0
    var timeWhenStopped: Long = 0
    var lStart: Location? = null
    var lEnd: Location? = null
    var distance: Double = 0.0
    var gpsEnableDialog: LocationDialog? = null
    var client: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null
    var locationCallback: LocationCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeedometerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.headerBarTitleTxt.text = "Speedometer"

        initializers()
        onClickListeners()
        mBannerAdsSmall()
        nativeAds()
    }

    @SuppressLint("MissingPermission")
    private fun initializers() {
        gpsEnableDialog = LocationDialog(this, this)
        mLocationService = LocationService(this, gpsEnableDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }
        client = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest!!.interval = 100
        locationRequest!!.fastestInterval = 50
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (isStart) {
                    Log.d("GetFusedLocationData", locationResult.toString())
                    SpeedoProcess(locationResult.lastLocation)
                }
            }
        }

    }

    private fun SpeedoProcess(lastLocation: Location) {
        distanceCovered(lastLocation)
        getSpeed(lastLocation.speed)
    }

    @SuppressLint("MissingPermission")
    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.startPauseBtn.setOnClickListener {
            if (isStartBtn) {
                onStartFun()
            } else {
                onPauseFun()
            }
        }
        binding.stopBtn.setOnClickListener {
            setDefaults()
        }
    }
    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    private fun onPauseFun() {
        binding.startPauseBtn.text = "Start"
        timeWhenStopped = binding.DurationValue.getBase() - SystemClock.elapsedRealtime()
        binding.DurationValue.stop()
        isStart = false
        isStartBtn = true
    }

    @SuppressLint("MissingPermission")
    private fun onStartFun() {
        isStart = true
        client!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
        binding.startPauseBtn.text = "Pause"
        binding.stopBtn.visibility = View.VISIBLE
        binding.DurationValue.setBase(SystemClock.elapsedRealtime() + timeWhenStopped)
        binding.DurationValue.start()
        isStartBtn = false
    }


    private fun setDefaults() {
        binding.stopBtn.visibility = View.GONE
        binding.startPauseBtn.text = "Start"
        timeWhenStopped = binding.DurationValue.getBase() - SystemClock.elapsedRealtime()
        binding.DurationValue.stop()
        isStart = false
        distance = 0.0
        minSpeed = 0.0
        maxSpeed = 0.0
        avgSpeed = 0.0
        binding.MaxValue.text = "0.0"
        binding.AverageValue.text = "0.0"
        binding.speedTxt.text = "0.0"
        isStartBtn = true
        timeWhenStopped=0
        binding.DurationValue.setText("00:00:00")
        client!!.removeLocationUpdates(locationCallback!!)
    }

    fun distanceCovered(location: Location) {
        if (lStart == null) {
            lEnd = location
            lStart = location
        } else {
            lEnd = location
        }
        distance += GetDistance(lStart!!, lEnd!!)
        Log.d("ParamsCheckTAG", "distanceCovered: " + distance)
        binding.DistanceValue.text = DecimalFormat("#.#").format(distance) + " Km."
    }

    private fun GetDistance(lStart: Location, lEnd: Location): Double {
        var valueInKm = 0.0
        if (lStart.latitude != 0.0 && lStart.longitude != 0.0) {
            val valueInMeter = SphericalUtil.computeDistanceBetween(
                com.google.android.gms.maps.model.LatLng(
                    lStart.latitude,
                    lStart.longitude
                ), com.google.android.gms.maps.model.LatLng(lEnd.latitude, lEnd.longitude)
            )
            valueInKm = valueInMeter / 1000
        }
        return valueInKm
    }

    fun getSpeed(speed: Float) {
        mySpeed = speed * 3.6
        if (mySpeed > maxSpeed) {
            try {
                maxSpeed = "${DecimalFormat("#.##").format(mySpeed)}".toDouble()
            } catch (e: Exception) {
            }
        } else {
            try {
                minSpeed = "${DecimalFormat("#.##").format(mySpeed)}".toDouble()
            } catch (e: Exception) {
            }
        }
        avgSpeed = (minSpeed + maxSpeed) / 2
        Log.d("ParamsCheckTAG", "getSpeed: " + mySpeed)
        binding.AverageValue.text = "${DecimalFormat("#.##").format(avgSpeed)}"
        binding.speedTxt.text = "${DecimalFormat("#.#").format(mySpeed)}"
        binding.MaxValue.text = "${DecimalFormat("#.##").format(mySpeed)}"
        binding.speedometer.speedTo(mySpeed.toFloat())
        binding.speedometer.setMinMaxSpeed(0f, 220f)
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

    override fun onDestroy() {
        unregisterReceiver(mLocationService)
        client!!.removeLocationUpdates(locationCallback!!)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                gpsEnableDialog!!.show()
                onPauseFun()
            } catch (e: Exception) {
            }
        } else {
            try {
                gpsEnableDialog!!.dismiss()

            } catch (e: Exception) {
            }
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
    private fun nativeAds() {
        val adMobFrame = binding.nativeAd.flAdplaceholder
        LiveStreetViewMyAppNativeAds.loadLiveStreetViewAdmobNativeAdPriority(this, adMobFrame)
    }

}