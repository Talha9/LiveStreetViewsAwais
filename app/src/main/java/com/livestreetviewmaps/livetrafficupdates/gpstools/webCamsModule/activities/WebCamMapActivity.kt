package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.*
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.MapStylesDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.MapStylesDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamMapBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamMapMarkerLocationModel
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnLocationClickListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin
import java.lang.Double
import com.mapbox.android.core.permissions.PermissionsManager

import androidx.annotation.NonNull
import com.mapbox.mapboxsdk.location.modes.RenderMode


class WebCamMapActivity : AppCompatActivity(),LocationDialogCallback,NetworkStateReceiver.NetworkStateReceiverListener,OnMapReadyCallback {
    var binding:ActivityWebCamMapBinding?=null
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    var dataModel:WebCamMapMarkerLocationModel?=null
    var position: CameraPosition? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    var oMapboxMap:MapboxMap?=null
    private var mBuildingPlugin: BuildingPlugin? = null
    private var locationComponent: LocationComponent? = null
    var trafficShowingOnMap = false
    var trafficPlugin: TrafficPlugin? = null
    var mMapLayersDialog:MapStylesDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding= ActivityWebCamMapBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.mapView.onCreate(savedInstanceState)


        if (intent.getParcelableExtra<WebCamMapMarkerLocationModel>("map_loc") != null) {
            try {
                val model = intent.getParcelableExtra<WebCamMapMarkerLocationModel>("map_loc")
                if (model != null) {
                    dataModel=model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.latitude + "," + model.longitude )
                    binding!!.header.headerBarTitleTxt.text=model.placeName
                }
            } catch (e: Exception) {
            }
        }


        initializers()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.currentLocationBtn.setOnClickListener {
            if (constants.mLatitude!=0.0  && constants.mLongitude!=0.0){
                getLocation(constants.mLatitude,constants.mLongitude)
            }
        }
        binding!!.mapLayersBtn.setOnClickListener {
            mMapLayersDialog= MapStylesDialog(this,object : MapStylesDialogCallback {
                override fun onMapDefaultMapClick() {
                    if (trafficShowingOnMap) {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(true)
                        }
                    } else {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(false)
                        }
                    }
                    oMapboxMap!!.setStyle(Style.MAPBOX_STREETS)
                    try {
                        mMapLayersDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
                override fun onMapNightMapClick() {
                    if (trafficShowingOnMap) {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(true)
                        }
                    } else {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(false)
                        }
                    }
                    oMapboxMap!!.setStyle(Style.DARK)
                    try {
                        mMapLayersDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onMapSatelliteMapClick() {
                    if (trafficShowingOnMap) {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(true)
                        }
                    } else {
                        if (trafficPlugin != null) {
                            trafficPlugin!!.setVisibility(false)
                        }
                    }
                    oMapboxMap!!.setStyle(Style.SATELLITE_STREETS)

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

    private fun initializers() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)
        binding!!.mapView.getMapAsync(this)
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

    override fun onEnabledGPS() {
        try {
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            startActivity(callGPSSettingIntent)
        } catch (e: Exception) {
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        oMapboxMap=mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        oMapboxMap!!.setStyle(Style.SATELLITE_STREETS, object : Style.OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                enableLocationComponent(style)
                trafficPlugin = TrafficPlugin(binding!!.mapView, mapboxMap, style)
                mBuildingPlugin = BuildingPlugin(binding!!.mapView, mapboxMap, style)
                try {
                    mBuildingPlugin!!.setOpacity(1.0f)
                    mBuildingPlugin!!.setMinZoomLevel(10f)
                } catch (e: Exception) {
                }
                getLocation(dataModel!!.latitude,dataModel!!.longitude)
                placeMarker(dataModel!!.latitude,dataModel!!.longitude)
                    //placeCurrentMarker(constants.mLatitude,constants.mLongitude)

            }
        })
    }

    private fun placeMarker(latitude: kotlin.Double, longitude: kotlin.Double) {
        val factory = IconFactory.getInstance(this)
        val icon = factory.fromResource(R.drawable.mapbox_marker_icon_default)
        oMapboxMap!!.addMarker(
            MarkerOptions().position(LatLng(latitude,longitude)).icon(icon))
        binding!!.progressMainBg.visibility=View.GONE
    }
    private fun placeCurrentMarker(latitude: kotlin.Double, longitude: kotlin.Double) {
        val factory = IconFactory.getInstance(this)
        val icon = factory.fromResource(R.drawable.current_marker)
        oMapboxMap!!.addMarker(
            MarkerOptions().position(LatLng(latitude,longitude)).icon(icon))
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
        binding!!.mapView.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsEnableDialog!!.show()
        } else {
            UtilsFunctionClass.askForPermissions(this)
            Log.d("onResumeTAG", "onResume: ")
        }
    }
    private fun getLocation(mLatitude: kotlin.Double, mLongitude: kotlin.Double) {

        position = CameraPosition.Builder()
            .target(
                LatLng(
                    Double.valueOf(mLatitude),
                    Double.valueOf(mLongitude)
                )
            ) // Sets the new camera position
            .zoom(10.0) // Sets the zoom
            .bearing(180.0) // Rotate the camera
            .tilt(30.0) // Set the camera tilt
            .build() // Creates a CameraPosition from the builder


        oMapboxMap!!.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position!!), 5000
        )
    }

    fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponent: LocationComponent = oMapboxMap!!.getLocationComponent()

            val customOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .foregroundTintColor(getColor(R.color.ThemeColor))
                .accuracyColor(getColor(R.color.themetrans))
                .accuracyAlpha(0.3f)
                .accuracyAnimationEnabled(true)
                .build()


            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .locationComponentOptions(customOptions)
                    .build()
            )

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
            locationComponent.isLocationComponentEnabled = true

            locationComponent.cameraMode = CameraMode.TRACKING

            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }
    }

}