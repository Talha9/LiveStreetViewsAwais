package com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Geocoders.ConvertLatLngToPlace
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityNavigationMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.adapters.NavigationAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.adapters.NavigationRouteButtonAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.callbacks.*
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.fragments.RouteNavigation
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.fragments.TextNavigation
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.fragments.TransitNavigation
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.fragments.VoiceNavigation
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.helpers.NavigationHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.models.*
import com.mapbox.android.core.location.*
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnLocationClickListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double
import java.lang.ref.WeakReference


class NavigationMainActivity : AppCompatActivity(), OnMapReadyCallback, LocationDialogCallback,
    NetworkStateReceiver.NetworkStateReceiverListener, onPlaceTextChangeCallback,
    onPlaceVoiceCallback, onPlaceRouteCallback, NavigationTransitCallback {
    private var destinationPoint: Point? = null
    var originPoint: Point? = null
    private var mapBoxCurrentRoute: DirectionsRoute? = null
    private var whichFragment: String = "Navigation"
    private var currentFragment: String = "Navigation"
    var binding: ActivityNavigationMainBinding? = null
    var adapter: NavigationAdapter? = null
    var manager: LinearLayoutManager? = null
    var fragment: Fragment? = null
    var list: ArrayList<NavigationModel>? = null
    var mConvertLatLngToPlaceName: ConvertLatLngToPlace? = null
    var onTimeLocationCheck = true
    var oMapboxMap: MapboxMap? = null
    private var mBuildingPlugin: BuildingPlugin? = null
    var mLocationDialog: LocationDialog? = null
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null
    private var callback: LocationChangeListeningActivityLocationCallback =
        LocationChangeListeningActivityLocationCallback(
            this
        )
    lateinit var mLocationService: LocationService
    var routesBtnAdapter: NavigationRouteButtonAdapter? = null
    var position: CameraPosition? = null
    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    private var mapBoxDirectionClient: MapboxDirections? = null
    val ROUTE_LAYER_ID = "ROUTE_LAYER_ID"
    val ROUTE_SOURCE_ID = "ROUTE_SOURCE_ID"
    val ICON_LAYER_ID = "ICON_LAYER_ID"
    val ICON_SOURCE_ID = "ICON_SOURCE_ID"
    val RED_PIN_ICON_ID = "RED_PIN_ICON_ID"
    var transitMarker = ArrayList<Marker>()
    var routesList = ArrayList<NavigationRouteButtonsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        binding = ActivityNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.mapView.onCreate(savedInstanceState)


        initializers()
        listFiller()
        setUpAdapter()
        fragmentChecker(whichFragment)
        onClickListeners()
        mBannerAdsSmall()
    }

    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initializers() {
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
    }

    private fun setUpAdapter() {
        manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.navigationFragBtnRecView.layoutManager = manager
        if (list != null && list!!.size > 0) {
            try {
                adapter = NavigationAdapter(this, list!!, object : NavigationCallbacks {
                    override fun onNavigationFragmentBtnClick(mTitle: String) {
                        Log.i("mTitle", " : " + mTitle)
                        fragmentChecker(mTitle)
                        currentFragment = mTitle
                        oMapboxMap!!.setStyle(
                            Style.SATELLITE_STREETS
                        ) { style ->
                        }
                        routesList.clear()
                        if (routesBtnAdapter != null) {
                            routesBtnAdapter!!.notifyDataSetChanged()
                        }


                    }

                })
                binding!!.navigationFragBtnRecView.adapter = adapter
            } catch (e: Exception) {
            }
        }
    }

    private fun listFiller() {
        list = NavigationHelper.navigationListFillHelper()
    }

    private fun fragmentChecker(whichFragment: String) {
        when (whichFragment) {
            "Navigation" -> {
                fragment = TextNavigation(this)
                loadFragment(fragment!!)

            }
            "Voice" -> {
                fragment = VoiceNavigation(this)
                loadFragment(fragment!!)

            }
            "Route" -> {
                fragment = RouteNavigation(this)
                loadFragment(fragment!!)

            }
            "Transit" -> {
                fragment = TransitNavigation(this)
                loadFragment(fragment!!)

            }
        }


    }

    private fun loadFragment(fragment: Fragment) {

        LiveStreetViewMyAppShowAds.meidationForClickFragmentLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )

        try {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down
            )


            transaction.replace(R.id.navHostFragmentNavigation, fragment)
            transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()


        } catch (e: Exception) {
        }
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

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val customOptions = LocationComponentOptions.builder(this)
                .elevation(5f)
                .foregroundTintColor(getColor(R.color.ThemeColor))
                .accuracyColor(getColor(R.color.themetrans))
                .accuracyAlpha(0.3f)
                .accuracyAnimationEnabled(true)
                .build()

            // Get an instance of the component
            locationComponent = oMapboxMap!!.locationComponent
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
            locationComponent?.addOnLocationClickListener(OnLocationClickListener {
                if (locationComponent?.getLastKnownLocation() != null) {
                    Toast.makeText(
                        this, String.format(
                            "Current Location",
                            locationComponent?.getLastKnownLocation()!!.latitude,
                            locationComponent?.getLastKnownLocation()!!.longitude
                        ), Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }
    }

    private class LocationChangeListeningActivityLocationCallback internal constructor(activity: NavigationMainActivity) :
        LocationEngineCallback<LocationEngineResult?> {
        var location: Location? = null
        private val activityWeakReference: WeakReference<NavigationMainActivity>
        override fun onSuccess(p0: LocationEngineResult?) {
            val activity: NavigationMainActivity? =
                activityWeakReference.get()
            if (activity != null) {
                location = p0?.lastLocation
                if (location == null) {
                    return
                }

                if (p0?.lastLocation != null) {
                    activity.oMapboxMap!!.locationComponent
                        .forceLocationUpdate(p0.lastLocation)
                    if (location != null) {
                        Log.d("LocationCheckingLogs", "onSuccess: " + location)
                        activity.getCurrent(location!!)

                    }
                }
            }
        }

        override fun onFailure(exception: java.lang.Exception) {
            val activity: NavigationMainActivity? =
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
                WeakReference<NavigationMainActivity>(
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

    private fun getCurrent(location: Location) {
        Log.d("LocationCheckingLogs", "getcurrent: " + location)
        if (onTimeLocationCheck) {
            getLocation(LatLng(location.latitude, location.longitude))
            position = CameraPosition.Builder()
                .target(
                    LatLng(
                        Double.valueOf(location.latitude),
                        Double.valueOf(location.longitude)
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
            onTimeLocationCheck = false
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

    private fun getLocation(mLatLngs: LatLng) {
        mConvertLatLngToPlaceName = ConvertLatLngToPlace(
            this,
            mLatLngs,
            object : ConvertLatLngToPlace.GeoTaskCallbackPlace {
                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                    if (fetchedAddress != null) {
                        constants.countryName = fetchedAddress

                    } else {
                        Toast.makeText(
                            this@NavigationMainActivity,
                            "Internet ERROR!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailedLocationFetched() {
                    Toast.makeText(
                        this@NavigationMainActivity,
                        "Internet ERROR!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        mConvertLatLngToPlaceName!!.execute()
    }


    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    private fun getDestinationLatLng(style: Style, model: PlaceResultModel) {
        if (model.currentLat != 0.0 && model.currentLng != 0.0) {
            originPoint = Point.fromLngLat(model.currentLng, model.currentLat)
        } else {
            originPoint = Point.fromLngLat(constants.mLongitude, constants.mLatitude)
        }
        if (model.destinationLat != 0.0 && model.destinationLng != 0.0) {
            destinationPoint = Point.fromLngLat(model.destinationLng, model.destinationLat)
            animateToBounds(
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteSources(
                style,
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteLayers(style)
            if (originPoint != null && destinationPoint != null) {
                getDirectionalRoute(
                    oMapboxMap!!,
                    originPoint!!,
                    destinationPoint!!
                )
            }
        }

    }

    private fun getDirectionalRoute(
        mapBoxMap: MapboxMap,
        originPoint: Point,
        destinationPoint: Point
    ) {
        Log.d("mapBoxCurrentRouteTAG", "onResponse: ")

        mapBoxDirectionClient = MapboxDirections.builder()
            .accessToken(constants.mapboxApiKey)
            .origin(originPoint)
            .destination(destinationPoint)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .build()

        mapBoxDirectionClient!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                Toast.makeText(
                    this@NavigationMainActivity,
                    "Cannot Find Route!\nTry again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                Log.d("mapBoxCurrentRouteTAG", "onResponse: " + response)
                if (response.body() == null) {
                    return
                } else if (response.body()!!.routes().size < 1) {
                    return
                }
                mapBoxCurrentRoute = response.body()!!.routes()[0]

                mapBoxMap.getStyle { style ->
                    val source =
                        style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    if (source != null) {
                        source.setGeoJson(
                            FeatureCollection.fromFeature(
                                Feature.fromGeometry(
                                    LineString.fromPolyline(
                                        mapBoxCurrentRoute!!.geometry()!!,
                                        PRECISION_6
                                    )
                                )
                            )
                        )
                        binding!!.routeProgress.visibility = View.GONE
                    } else {
                        Toast.makeText(
                            this@NavigationMainActivity,
                            "Cannot find Route!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        })

    }

    private fun initRouteSources(
        style: Style,
        latitude: kotlin.Double,
        longitude: kotlin.Double,
        latitude1: kotlin.Double,
        longitude1: kotlin.Double
    ) {
        try {
            style.addSource(
                GeoJsonSource(
                    ROUTE_SOURCE_ID,
                    FeatureCollection.fromFeatures(arrayOf())
                )
            )
            val iconGeoJsonSource = GeoJsonSource(
                ICON_SOURCE_ID, FeatureCollection.fromFeatures(
                    arrayOf(
                        Feature.fromGeometry(
                            Point.fromLngLat(
                                longitude,
                                latitude

                            )
                        ),
                        Feature.fromGeometry(
                            Point.fromLngLat(
                                longitude1,
                                latitude1
                            )
                        )
                    )
                )
            )
            style.addSource(iconGeoJsonSource)
        } catch (e: Exception) {
        }
    }

    private fun initRouteLayers(style: Style) {
        try {
            val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)
            routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.parseColor("#212073"))
            )
            style.addLayer(routeLayer)
            style.addImage(
                RED_PIN_ICON_ID,
                BitmapUtils.getBitmapFromDrawable(resources!!.getDrawable(R.drawable.mapbox_marker_icon_default))!!
            )
            style.addLayer(
                SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID)
                    .withProperties(
                        PropertyFactory.iconImage(RED_PIN_ICON_ID),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconOffset(arrayOf(0f, -9f))
                    )
            )
        } catch (e: Exception) {
        }
    }

    private fun animateToBounds(
        longitude: kotlin.Double,
        latitude: kotlin.Double,
        destinationLat: kotlin.Double,
        destinationLng: kotlin.Double
    ) {
        val latLngBounds = LatLngBounds.Builder()
            .include(LatLng(longitude, latitude))
            .include(LatLng(destinationLat, destinationLng))
            .build()
        try {
            oMapboxMap!!.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, 60),
                5000
            )
        } catch (e: Exception) {
        }
    }

    override fun onTextChange(model: PlaceResultModel) {
        binding!!.routeProgress.visibility = View.VISIBLE
        if (model != null) {
            oMapboxMap!!.setStyle(
                Style.SATELLITE_STREETS
            ) { style ->
                getDestinationLatLng(style, model)
            }

        }
    }

    override fun onVoiceTextGet(model: PlaceResultModel) {
        binding!!.routeProgress.visibility = View.VISIBLE
        if (model != null) {
            if (model.currentLat != null && model.currentLng != null && model.destinationLat != null && model.destinationLng != null) {
                oMapboxMap!!.setStyle(
                    Style.SATELLITE_STREETS
                ) { style ->
                    getDestinationLatLng(style, model)
                }
            }

        }
    }

    override fun onRoutesGenerate(model: PlaceResultModel) {
        binding!!.routeProgress.visibility = View.VISIBLE
        if (model != null) {
            if (model.currentLat != null && model.currentLng != null && model.destinationLat != null && model.destinationLng != null) {
                oMapboxMap!!.setStyle(
                    Style.SATELLITE_STREETS
                ) { style ->
                    routesList.clear()
                    getRoutesDestinationLatLng(style, model)
                }
            }

        }
    }

    override fun gettingWaypointsData(model: TransitWayPointModel) {
        binding!!.routeProgress.visibility = View.VISIBLE
        if (model != null) {
            if (model.currentLat != null && model.currentLng != null && model.destinationLat != null && model.destinationLng != null) {
                oMapboxMap!!.setStyle(
                    Style.SATELLITE_STREETS
                ) { style ->
                    routesList.clear()
                    oMapboxMap!!.removeAnnotations()
                    getWaypointDestinationLatLng(style, model)
                }
            }

        }
    }

    private fun getRoutesDestinationLatLng(style: Style, model: PlaceResultModel) {
        if (model.currentLat != 0.0 && model.currentLng != 0.0) {
            originPoint = Point.fromLngLat(model.currentLng, model.currentLat)
        } else {
            originPoint = Point.fromLngLat(constants.mLongitude, constants.mLatitude)
        }
        if (model.destinationLat != 0.0 && model.destinationLng != 0.0) {
            destinationPoint = Point.fromLngLat(model.destinationLng, model.destinationLat)
            animateToBounds(
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteSources(
                style,
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteLayers(style)
            if (originPoint != null && destinationPoint != null) {
                getDirectionalMultipleRoute(
                    oMapboxMap!!,
                    originPoint!!,
                    destinationPoint!!
                )
            }
        }
    }

    private fun getDirectionalMultipleRoute(
        oMapboxMap: MapboxMap,
        originPoint: Point,
        destinationPoint: Point
    ) {
        Log.d("mapBoxCurrentRouteTAG", "onResponse: ")

        mapBoxDirectionClient = MapboxDirections.builder()
            .accessToken(constants.mapboxApiKey)
            .origin(originPoint)
            .destination(destinationPoint)
            .alternatives(true)     //use for Multiple Routes
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .build()

        mapBoxDirectionClient!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                Toast.makeText(
                    this@NavigationMainActivity,
                    "Cannot Find Route!\nTry again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                Log.d("mapBoxCurrentRouteTAG", "onResponse: " + response)
                if (response.body() == null) {
                    return
                } else if (response.body()!!.routes().size < 0) {
                    return
                }
                binding!!.routeProgress.visibility = View.GONE
                for (i in 0 until response.body()!!.routes().size) {
                    Log.d("routeSizeTAG", "onResponse: " + response.body()!!.routes()[i])
                    routesList.add(
                        NavigationRouteButtonsModel(
                            response.body()!!.routes()[i].distance().toString(),
                            (i + 1).toString(),
                            response.body()!!.routes()[i],
                            i + 1
                        )
                    )
                }
                if (routesList.size > 0) {
                    setUpRoutesBtnAdapter(routesList)
                } else {
                    Toast.makeText(
                        this@NavigationMainActivity,
                        "No Alternative Route Found!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        })

    }

    private fun setUpRoutesBtnAdapter(routesList: ArrayList<NavigationRouteButtonsModel>?) {
        manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding!!.routeNavigationRecycler.layoutManager = manager
        routesBtnAdapter =
            NavigationRouteButtonAdapter(this, routesList!!, object : NavigationRouteCallback {
                override fun onRouteButtonClick(model: NavigationRouteButtonsModel, position: Int) {
                    constants.routeIndex = position
                    oMapboxMap!!.getStyle { style ->
                        val source =
                            style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                        if (source != null) {
                            source.setGeoJson(
                                FeatureCollection.fromFeature(
                                    Feature.fromGeometry(
                                        LineString.fromPolyline(
                                            model.route!!.geometry()!!,
                                            PRECISION_6
                                        )
                                    )
                                )
                            )


                        } else {
                            Toast.makeText(
                                this@NavigationMainActivity,
                                "Cannot find Route!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            })
        binding!!.routeNavigationRecycler.adapter = routesBtnAdapter
    }

    private fun getWaypointDestinationLatLng(style: Style, model: TransitWayPointModel) {

        if (model.currentLat != 0.0 && model.currentLng != 0.0) {
            originPoint = Point.fromLngLat(model.currentLng, model.currentLat)
        } else {
            originPoint = Point.fromLngLat(constants.mLongitude, constants.mLatitude)
        }
        if (model.destinationLat != 0.0 && model.destinationLng != 0.0) {
            destinationPoint = Point.fromLngLat(model.destinationLng, model.destinationLat)
            animateToBounds(
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteSources(
                style,
                originPoint!!.latitude(),
                originPoint!!.longitude(),
                destinationPoint!!.latitude(),
                destinationPoint!!.longitude()
            )
            initRouteLayers(style)
            if (originPoint != null && destinationPoint != null) {
                getDirectionalWaypointsRoute(
                    originPoint!!,
                    destinationPoint!!,
                    model.list
                )
            }
        }
    }

    private fun getDirectionalWaypointsRoute(
        originPoint: Point,
        destinationPoint: Point,
        wayPointsList: java.util.ArrayList<TransitRoutesModel>
    ) {
        Log.d("mapBoxCurrentRouteTAG", "onResponse: ")

        val builder: MapboxDirections.Builder = MapboxDirections.builder()
            .accessToken(constants.mapboxApiKey)
            .origin(originPoint)
            .destination(destinationPoint)

            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)

        for (i in 0 until wayPointsList.size) { // Adding all the waypoints as pitstops to the route
            builder.addWaypoint(
                Point.fromLngLat(
                    wayPointsList[i].longitude.toDouble(),
                    wayPointsList[i].latitude.toDouble()
                )
            )
        }
        builder.build().enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                Toast.makeText(
                    this@NavigationMainActivity,
                    "Cannot Find Route!\nTry again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                Log.d("mapBoxCurrentRouteTAG", "onResponse: " + response)
                if (response.body() == null) {
                    return
                } else if (response.body()!!.routes().size < 0) {
                    return
                }
                mapBoxCurrentRoute = response.body()!!.routes()[0]
                oMapboxMap!!.getStyle { style ->
                    val source =
                        style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    if (source != null) {
                        source.setGeoJson(
                            FeatureCollection.fromFeature(
                                Feature.fromGeometry(
                                    LineString.fromPolyline(
                                        mapBoxCurrentRoute!!.geometry()!!,
                                        PRECISION_6
                                    )
                                )
                            )
                        )
                        for (i in 0 until wayPointsList.size) {
                            val factory = IconFactory.getInstance(this@NavigationMainActivity)
                            val icon = factory.fromResource(R.drawable.map_default_map_marker)
                            transitMarker.add(
                                oMapboxMap!!.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            wayPointsList[i].latitude.toDouble(),
                                            wayPointsList[i].longitude.toDouble()
                                        )
                                    ).icon(icon)
                                )
                            )

                        }
                        binding!!.routeProgress.visibility = View.GONE
                    } else {
                        Toast.makeText(
                            this@NavigationMainActivity,
                            "Cannot find Route!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        })

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