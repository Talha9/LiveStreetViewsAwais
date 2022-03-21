package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdSize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.SphericalUtil
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.MapStylesDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.MyLatLng
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.viewModel.LiveStreetViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.MapStylesDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityHikingMapBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.CloseHikingActivityCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.dialogs.CloseHikingActivityConfirmDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models.HikingHomeModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.mapbox.android.core.location.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import java.lang.Double
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HikingMapActivity : AppCompatActivity(), OnMapReadyCallback,
    NetworkStateReceiver.NetworkStateReceiverListener, LocationDialogCallback {
    private var mySpeed: kotlin.Double = 0.0
    private var maxSpeed: kotlin.Double = 0.0
    var currentLocation: Location? = null
    var timeWhenStopped: Long = 0
    var isRunStopped = true
    var mMapLayersDialog: MapStylesDialog? = null
    var binding: ActivityHikingMapBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    var dataModel: HikingHomeModel? = null
    var isStart = false
    var lStart: Location? = null
    var lEnd: Location? = null
    var distance: kotlin.Double = 0.0
    lateinit var oMapboxMap: MapboxMap
    private var mBuildingPlugin: BuildingPlugin? = null
    var mLocationDialog: LocationDialog? = null
    var oneTimeMarker: Boolean = true
    var oneTimeLocation: Boolean = true
    var lEndLocation: Location? = null
    var lStartLocation: Location? = null
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    private var callback: LocationChangeListeningActivityLocationCallback =
        LocationChangeListeningActivityLocationCallback(
            this
        )
    var exitDialog: CloseHikingActivityConfirmDialog? = null
    lateinit var mLocationService: LocationService
    var position: CameraPosition? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var mLiveStreetViewModel: LiveStreetViewModel? = null
    var lstLngList = ArrayList<MyLatLng>()
    var currentDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding = ActivityHikingMapBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.mapView.onCreate(savedInstanceState)
        initialization()

        if (intent.getParcelableExtra<HikingHomeModel>("hiking_home_model") != null) {
            try {
                val model = intent.getParcelableExtra<HikingHomeModel>("hiking_home_model")
                if (model != null) {
                    dataModel = model
                    binding!!.header.headerBarTitleTxt.text = dataModel!!.workoutName
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.workoutName + "," + model.workoutImg
                    )
                }
            } catch (e: Exception) {
            }
        }
        defaultCondition()
        onClickListeners()
        setUpBottomSheet()
        mBannerAdsSmall()

    }

    private fun initialization() {
        mLiveStreetViewModel = ViewModelProvider(this)[LiveStreetViewModel::class.java]
        mLocationDialog = LocationDialog(this, this)
        mLocationService = LocationService(this, mLocationDialog!!)
        try {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mLocationService, filter)
        } catch (e: Exception) {
        }
        callback = LocationChangeListeningActivityLocationCallback(this)
        binding!!.mapView.onStart()
        binding!!.mapView.getMapAsync(this)
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)

        currentDate = getLiveDate("dd-MMM-YYYY", 0)
    }

    @SuppressLint("MissingPermission")
    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.bSheet.hikingStartBtn.setOnClickListener {
            isStart = true
            isRunStopped=false
            binding!!.bSheet.hikingPauseBtn.visibility = View.VISIBLE
            binding!!.bSheet.hikingStopBtn.visibility = View.VISIBLE
            binding!!.bSheet.hikingStartBtn.visibility = View.GONE
            binding!!.bSheet.durationtxt.setBase(SystemClock.elapsedRealtime() + timeWhenStopped)
            binding!!.bSheet.durationtxt.start()
            oMapboxMap.locationComponent.isLocationComponentEnabled = true

        }
        binding!!.bSheet.topLine.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding!!.bSheet.hikingPauseBtn.setOnClickListener {
            binding!!.bSheet.hikingPauseBtn.visibility = View.GONE
            binding!!.bSheet.hikingStopBtn.visibility = View.VISIBLE
            binding!!.bSheet.hikingStartBtn.visibility = View.VISIBLE
            timeWhenStopped = binding!!.bSheet.durationtxt.getBase() - SystemClock.elapsedRealtime()
            binding!!.bSheet.durationtxt.stop()
            oMapboxMap.locationComponent.isLocationComponentEnabled = false
            isStart = false
        }
        binding!!.bSheet.hikingStopBtn.setOnClickListener {
            isRunStopped=true
            onBackPressed()
        }
        binding!!.currentLocationBtn.setOnClickListener {
            if (currentLocation != null) {
                if (currentLocation!!.latitude != 0.0 && currentLocation!!.longitude != 0.0) {
                    getcurrent(currentLocation!!)
                }
            }
        }
        binding!!.mapLayersBtn.setOnClickListener {
            mMapLayersDialog = MapStylesDialog(this, object : MapStylesDialogCallback {
                override fun onMapDefaultMapClick() {
                    oMapboxMap.setStyle(Style.MAPBOX_STREETS)
                    try {
                        mMapLayersDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onMapNightMapClick() {
                    oMapboxMap.setStyle(Style.DARK)
                    try {
                        mMapLayersDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onMapSatelliteMapClick() {
                    oMapboxMap.setStyle(Style.SATELLITE_STREETS)
                    try {
                        mMapLayersDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            })
            try {
                mMapLayersDialog!!.show()
            } catch (e: Exception) {
            }
        }

    }

    override fun onBackPressed() {
        if (!isRunStopped) {
            exitActivityDialog()
        } else {
            val intent = Intent(this@HikingMapActivity, HikingMainActivity::class.java)
            LiveStreetViewMyAppShowAds.meidationForClickFinishLiveStreetView(
                this,
                LiveStreetViewMyAppAds.admobInterstitialAd,intent
            )
        }

    }

    private fun saveActivityData() {
        if (distance != null && lstLngList.size > 0) {
            mLiveStreetViewModel!!.insert(
                HikingTable(
                    dataModel!!.workoutName, binding!!.bSheet.durationtxt.text.toString(), distance,
                    currentDate!!,
                    lstLngList.toMutableList()
                )
            )
            Toast.makeText(this@HikingMapActivity, "Data Saved Sucessfully!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun defaultCondition() {
        binding!!.bSheet.hikingPauseBtn.visibility = View.GONE
        binding!!.bSheet.hikingStopBtn.visibility = View.GONE
        binding!!.bSheet.hikingStartBtn.visibility = View.VISIBLE
        binding!!.bSheet.distancetxt.text = "0.00 Km"
        binding!!.bSheet.durationtxt.setText("00:00:00")
        binding!!.bSheet.speedtxt.text = "0.0 Km/h"
        distance = 0.0
        lStart = null
        lEnd = null
        maxSpeed = 0.0
        mySpeed = 0.0
        lStartLocation = null
        lEndLocation = null


    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        oMapboxMap = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(
            Style.SATELLITE_STREETS
        ) { style ->
            enableLocationComponent(style)
            mBuildingPlugin = BuildingPlugin(binding!!.mapView, mapboxMap, style)
            try {
                mBuildingPlugin!!.setOpacity(1.0f)
                mBuildingPlugin!!.setMinZoomLevel(10f)
            } catch (e: Exception) {
            }


        }
    }

    private fun drawPoliLine(currentLocation: Location?) {
        lStartLocation = lEndLocation
        lEndLocation = currentLocation
        if (lStartLocation != null && lEndLocation != null) {
            if (oneTimeMarker) {
                val factory = IconFactory.getInstance(this)
                val icon = factory.fromResource(R.drawable.location_marker)
                oMapboxMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            lStartLocation!!.latitude,
                            lStartLocation!!.longitude
                        )
                    ).icon(icon)
                )
                oneTimeMarker = false
            }

            Log.d(
                "drawPoliLineTAG",
                "drawPoliLine: " + lStartLocation!!.latitude + ":" + lStartLocation!!.longitude + ",," + lEndLocation!!.latitude + ":" + lEndLocation!!.longitude
            )
            oMapboxMap.addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(lStartLocation!!.latitude, lStartLocation!!.longitude),
                        LatLng(lEndLocation!!.latitude, lEndLocation!!.longitude)
                    )
                    .width(3f)
                    .color(getColor(R.color.ThemeColor))
            )
            lstLngList.add(
                MyLatLng(
                    lStartLocation!!.latitude.toString(),
                    lStartLocation!!.longitude.toString()
                )
            )
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bSheet.bottomSheetDrawer)

    }

    private fun getcurrent(location: Location) {
        position = CameraPosition.Builder()
            .target(
                LatLng(
                    Double.valueOf(location.latitude),
                    Double.valueOf(location.longitude)
                )
            ) // Sets the new camera position
            .build() // Creates a CameraPosition from the builder

        oMapboxMap.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position!!), 5000
        )


    }

    private fun getOneTimeCurrentLocation(location: Location) {
        if (oneTimeLocation) {
            position = CameraPosition.Builder()
                .target(
                    LatLng(
                        Double.valueOf(location.latitude),
                        Double.valueOf(location.longitude)
                    )
                ) // Sets the new camera position
                .zoom(17.0) // Sets the zoom
                .bearing(180.0) // Rotate the camera
                .tilt(40.0) // Set the camera tilt
                .build() // Creates a CameraPosition from the builder


            oMapboxMap.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position!!), 5000
            )
            oneTimeLocation = false
        }

    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val customOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .foregroundTintColor(getColor(R.color.ThemeColor))
                .build()

            // Get an instance of the component
            locationComponent = oMapboxMap.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .locationComponentOptions(customOptions)
                    .build()


            // Activate with options
            locationComponent?.activateLocationComponent(locationComponentActivationOptions)
            // Enable to make component visible
            locationComponent?.setLocationComponentEnabled(true)


            // Set the component's camera mode
            locationComponent?.setCameraMode(CameraMode.TRACKING)

            // Set the component's render mode
            initLocationEngine()


            // Add the location icon click listener

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }
    }

    private class LocationChangeListeningActivityLocationCallback internal constructor(activity: HikingMapActivity) :
        LocationEngineCallback<LocationEngineResult?> {
        var location: Location? = null
        private val activityWeakReference: WeakReference<HikingMapActivity>
        override fun onSuccess(p0: LocationEngineResult?) {
            val activity: HikingMapActivity? =
                activityWeakReference.get()
            if (activity != null) {
                location = p0?.lastLocation
                if (location == null) {
                    return
                }

                if (p0?.lastLocation != null) {
                    activity.oMapboxMap.locationComponent
                        .forceLocationUpdate(p0.lastLocation)
                    if (location != null) {
                        activity.currentLocation = location
                        activity.getOneTimeCurrentLocation(location!!)
                        Log.d("LocationCheckingLogs", "onSuccess: " + location)
                        if (activity.isStart) {
                            activity.getcurrent(location!!)
                            activity.drawPoliLine(location)
                            activity.distanceCovered(location!!)
                            activity.getSpeed(location!!.speed)
                        }
                    }
                }
            }
        }

        override fun onFailure(exception: java.lang.Exception) {
            val activity: HikingMapActivity? =
                activityWeakReference.get()
            if (activity != null) {
                Toast.makeText(
                    activity, exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        init {
            activityWeakReference =
                WeakReference<HikingMapActivity>(
                    activity
                )
        }

    }

    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request =
            LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationEngine!!.requestLocationUpdates(request, callback, mainLooper)
        locationEngine!!.getLastLocation(callback)

    }

    fun distanceCovered(location: Location) {
        if (lStart == null) {
            lEnd = location
            lStart = location
        } else {
            lEnd = location
        }
        distance += GetDistance(lStart!!, lEnd!!)
        binding!!.bSheet.distancetxt.text = DecimalFormat("#.#").format(distance) + " Km."
    }

    private fun GetDistance(lStart: Location, lEnd: Location): kotlin.Double {
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

    private fun getDuration() {
        binding!!.bSheet.durationtxt.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        })
        binding!!.bSheet.durationtxt.setBase(SystemClock.elapsedRealtime())
        binding!!.bSheet.durationtxt.setText("00:00:00")

    }

    fun getSpeed(speed: Float) {
        mySpeed = speed * 3.6
        if (mySpeed > maxSpeed) {
            try {
                maxSpeed = "${DecimalFormat("#.##").format(mySpeed)}".toDouble()
            } catch (e: Exception) {
            }
        }
        binding!!.bSheet.speedtxt.text = "${DecimalFormat("#.##").format(mySpeed)}Km/h"
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
                    val intent = Intent(this@HikingMapActivity, HikingMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                return@OnKeyListener false
            })
        } catch (e: Exception) {
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

    override fun onStart() {
        super.onStart()
        binding!!.mapView.onStart()
    }

    override fun onPause() {
        binding!!.mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding!!.mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        binding!!.mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        binding!!.mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(mLocationService)
        binding!!.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        getDuration()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                mLocationDialog!!.show()
            } catch (e: Exception) {
            }
        } else {
            try {
                mLocationDialog!!.dismiss()
            } catch (e: Exception) {
            }
        }
    }

    fun getLiveDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    private fun exitActivityDialog() {
        exitDialog = CloseHikingActivityConfirmDialog(this, object : CloseHikingActivityCallback {
            override fun onYesClick() {
                saveActivityData()
                timeWhenStopped =
                    binding!!.bSheet.durationtxt.getBase() - SystemClock.elapsedRealtime()
                binding!!.bSheet.durationtxt.stop()
                isStart = false
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                defaultCondition()
                oMapboxMap.removeAnnotations()
                exitDialog!!.dismiss()
                val intent = Intent(this@HikingMapActivity, HikingMainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onNoClick() {
                val intent = Intent(this@HikingMapActivity, HikingMainActivity::class.java)
                startActivity(intent)
                finish()
            }

        })
        try {
            exitDialog!!.show()
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