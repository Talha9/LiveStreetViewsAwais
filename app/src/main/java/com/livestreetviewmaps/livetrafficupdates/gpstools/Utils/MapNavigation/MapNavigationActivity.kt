package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.MapNavigation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.MapNavigation.model.NavigationModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityMapNavigationBinding
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.navigation.base.internal.route.RouteUrl
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback
import com.mapbox.navigation.ui.NavigationViewOptions
import com.mapbox.navigation.ui.OnNavigationReadyCallback
import com.mapbox.navigation.ui.listeners.NavigationListener
import com.mapbox.navigation.ui.map.NavigationMapboxMap

class MapNavigationActivity : AppCompatActivity(), OnNavigationReadyCallback, NavigationListener {
    var binding: ActivityMapNavigationBinding? = null
    private var origin: Point? = null
    private var destination: Point? = null
    var dataModel: NavigationModel? = null
    var navigateCheck = false
    private lateinit var navigationMapboxMap: NavigationMapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation
    private var routeType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(this, constants.mapboxApiKey)
        } catch (e: Exception) {
        }
        binding = ActivityMapNavigationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (intent.getParcelableExtra<NavigationModel>("navigation_model") != null) {
            try {
                val model = intent.getParcelableExtra<NavigationModel>("navigation_model")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.originLatitude + "," + model.originLongitude + model.destinationLatitude + "," + model.destinationLongitude
                    )

                    origin =
                        Point.fromLngLat(dataModel!!.originLongitude, dataModel!!.originLatitude)
                    destination = Point.fromLngLat(
                        dataModel!!.destinationLongitude,
                        dataModel!!.destinationLatitude
                    )
                    routeType = dataModel!!.routeIndex
                }
            } catch (e: Exception) {
            }
        }

        initialization(savedInstanceState)


    }

    private fun initialization(savedInstanceState: Bundle?) {
        val initialPosition =
            CameraPosition.Builder()
                .target(
                    LatLng(
                        origin!!.latitude(),
                        origin!!.longitude()
                    )
                )
                .zoom(15.0)
                .build()
        binding!!.navigationView.onCreate(savedInstanceState)
        binding!!.navigationView.initialize(this, initialPosition)
    }

    override fun onNavigationReady(isRunning: Boolean) {
        Log.d("NavigationRunningTag", "onNavigationReady: $isRunning")
        if (!isRunning && !::navigationMapboxMap.isInitialized
            && !::mapboxNavigation.isInitialized
        ) {
            if (binding!!.navigationView.retrieveNavigationMapboxMap() != null
            ) {
                Log.d("NavigationRunningTag", "onNavigationReady: Both are not null")
                navigationMapboxMap = binding!!.navigationView.retrieveNavigationMapboxMap()!!
                val navigationOptions = MapboxNavigation
                    .defaultNavigationOptionsBuilder(this, "")
                    .build()
                mapboxNavigation = MapboxNavigationProvider.create(navigationOptions)
                fetchRoute()
            } else {
                Log.d("NavigationRunningTag", "onNavigationReady:  Both are null")
            }
        }
    }

    private fun fetchRoute() {
        Log.d("RouteFetchTag", "fetchRoute: ")
        if (origin != null && destination != null) {
            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .accessToken(constants.mapboxApiKey)
                    .coordinates(listOf(origin, destination))
                    .geometries(RouteUrl.GEOMETRY_POLYLINE6)
                    .alternatives(true)
                    .profile(RouteUrl.PROFILE_DRIVING_TRAFFIC)
                    .user(RouteUrl.PROFILE_DEFAULT_USER)
                    .baseUrl(RouteUrl.BASE_URL)
                    .requestUuid("1")
                    .build(), object : RoutesRequestCallback {
                    override fun onRoutesReady(routes: List<DirectionsRoute>) {
                        Log.d("RouteFetchTag", "onRoutesReady: ")
                        if (routes.isNotEmpty()) {
                            val directionsRoute = routes[routeType]
                            startNavigation(directionsRoute)
                            navigateCheck = true
                        }
                    }

                    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
                        Log.d("RouteFetchTag", "onRoutesRequestCanceled: ")
                        navigateCheck = true
                    }

                    override fun onRoutesRequestFailure(
                        throwable: Throwable,
                        routeOptions: RouteOptions
                    ) {
                        navigateCheck = true
                        Log.d(
                            "RouteFetchTag",
                            "onRoutesRequestFailure: " + throwable.localizedMessage
                        )
                    }

                }
            )

        }
    }

    private fun startNavigation(directionsRoute: DirectionsRoute) {
        Log.d("startNavigationTag", "startNavigation: ")
        val optionsBuilder = NavigationViewOptions.builder(this)
        optionsBuilder.navigationListener(this)
        optionsBuilder.directionsRoute(directionsRoute)
        optionsBuilder.shouldSimulateRoute(false)
        binding!!.navigationView.startNavigation(optionsBuilder.build())

    }

    override fun onNavigationRunning() {
        Log.d("startNavigationTag", "onNavigationRunning: ")
    }

    override fun onNavigationFinished() {
        Log.d("startNavigationTag", "onNavigationFinished: ")
        if (navigateCheck) {
            finish()
        } else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancelNavigation() {
        if (navigateCheck) {
            Log.d("startNavigationTag", "onCancelNavigation: ")
            binding!!.navigationView.stopNavigation()
            finish()
        } else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        binding!!.navigationView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        binding!!.navigationView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding!!.navigationView.onResume()
    }

    override fun onStop() {
        binding!!.navigationView.onStop()
        super.onStop()
    }

    override fun onPause() {
        binding!!.navigationView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding!!.navigationView.onDestroy()
        mapboxNavigation.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding!!.navigationView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding!!.navigationView.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (navigateCheck) {
            super.onBackPressed()
        }else {
            Toast.makeText(this, "Please Wait Route is Being Ready!", Toast.LENGTH_SHORT).show()
        }
    }


}