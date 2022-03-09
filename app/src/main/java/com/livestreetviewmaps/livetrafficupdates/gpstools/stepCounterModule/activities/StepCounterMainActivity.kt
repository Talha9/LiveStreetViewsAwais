package com.livestreetviewmaps.livetrafficupdates.gpstools.stepCounterModule.activities


import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style

import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Scanner
import timber.log.Timber
import com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND
import com.mapbox.mapboxsdk.style.layers.Property.VISIBLE
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.Source
import com.mapbox.turf.TurfJoins


class StepCounterMainActivity : AppCompatActivity() {
    private val TAG: String="lineTags"
    private val SEARCH_DATA_SYMBOL_LAYER_SOURCE_ID = "SEARCH_DATA_SYMBOL_LAYER_SOURCE_ID"
    private val FREEHAND_DRAW_LINE_LAYER_SOURCE_ID = "FREEHAND_DRAW_LINE_LAYER_SOURCE_ID"
    private val MARKER_SYMBOL_LAYER_SOURCE_ID = "MARKER_SYMBOL_LAYER_SOURCE_ID"
    private val FREEHAND_DRAW_FILL_LAYER_SOURCE_ID = "FREEHAND_DRAW_FILL_LAYER_SOURCE_ID"
    private val FREEHAND_DRAW_LINE_LAYER_ID = "FREEHAND_DRAW_LINE_LAYER_ID"
    private val SEARCH_DATA_SYMBOL_LAYER_ID = "SEARCH_DATA_SYMBOL_LAYER_ID"
    private val SEARCH_DATA_MARKER_ID = "SEARCH_DATA_MARKER_ID"
    private val LINE_COLOR = "#a0861c"
    private val LINE_WIDTH = 5f
    private val LINE_OPACITY = 1f

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var searchPointFeatureCollection: FeatureCollection? = null
    private var freehandTouchPointListForLine=ArrayList<Point>()
    private var showSearchDataLocations = true
    private var drawSingleLineOnly = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Mapbox.getInstance(
                this,
                constants.mapboxApiKey
            )
        } catch (e: Exception) {
        }
        setContentView(R.layout.activity_step_counter_main)


        mapView = findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(
                Style.LIGHT
            ) { style ->
                this.mapboxMap = mapboxMap

                findViewById<View>(R.id.switch_to_single_line_only_fab)
                    .setOnClickListener {
                        //enableMapDrawing()
                        mapView!!.setOnTouchListener(object :View.OnTouchListener{
                            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                                val latLngTouchCoordinate = mapboxMap!!.projection.fromScreenLocation(
                                    PointF(p1!!.x, p1!!.y)
                                )
                                val screenTouchPoint = Point.fromLngLat(
                                    latLngTouchCoordinate.longitude,
                                    latLngTouchCoordinate.latitude
                                )
// Draw the line on the map as the finger is dragged along the map
                                freehandTouchPointListForLine.add(screenTouchPoint)
                                Log.d(TAG, "onTouch: "+freehandTouchPointListForLine)
                                mapboxMap!!.getStyle(object : Style.OnStyleLoaded {
                                    override fun onStyleLoaded(style: Style) {
//                    val drawLineSource =style.getSourceAs<GeoJsonSource>(FREEHAND_DRAW_LINE_LAYER_SOURCE_ID)
//                    if (drawLineSource!=null) {
//                        drawLineSource.setGeoJson(FeatureCollection.fromFeature(
//                            Feature.fromGeometry(
//                                LineString.fromLngLats(
//                                    freehandTouchPointListForLine
//                                )
//                            )
//                        )
//                        )
//
//                    }
                                        val source =
                                            style.getSourceAs<GeoJsonSource>(FREEHAND_DRAW_LINE_LAYER_SOURCE_ID)
                                        Log.d(TAG, "onStyleLoaded: "+source)
                                        if (source != null) {
                                            source.setGeoJson(
                                                FeatureCollection.fromFeature(
                                                    Feature.fromGeometry(
                                                        LineString.fromLngLats(
                                                            freehandTouchPointListForLine
                                                        )
                                                    )
                                                )
                                            )
                                        }


//                    // Take certain actions when the drawing is done
//                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//
//                        // If drawing polygon, add the first screen touch point to the end of
//                        // the LineLayer list so that it's
//
//                        if (drawSingleLineOnly) {
//                            Toast.makeText(this@StepCounterMainActivity,
//                                "move_map_drawn_line", Toast.LENGTH_SHORT).show()
//                        }
//                        enableMapMovement()
//                    }
                                    }
                                })
                                return true
                            }

                        })
                    }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        mapView!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

}