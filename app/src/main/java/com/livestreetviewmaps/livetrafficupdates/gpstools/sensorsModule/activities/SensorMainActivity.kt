package com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.activities

import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.SimpleDateFormat
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivitySensorMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.adapter.SensorsStatusAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.models.SensorsStatusModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.*

class SensorMainActivity : AppCompatActivity(), NetworkStateReceiver.NetworkStateReceiverListener,
    LocationDialogCallback, SensorEventListener {
    private var maxLux = 0.0
    lateinit var binding: ActivitySensorMainBinding
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var timeVal = "0.0"
    var networkVal: String? = "null"
    var deviceManufacturer: String? = null
    var telephonyManager: TelephonyManager? = null
    var carrierName = "null"
    var deviceName = "null"
    var sensorManager: SensorManager? = null
    var adapter: SensorsStatusAdapter? = null
    var df2 = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializers()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getStatusValues()
        }
        onClickListeners()
        mBannerAdsSmall()
    }

    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getStatusValues() {
        val df = SimpleDateFormat("HH:mm:ss a, dd/MM/yyyy", Locale.getDefault())
        timeVal = df.format(Date())

        binding.StatusRecView.layoutManager = LinearLayoutManager(this)
        val list = ArrayList<SensorsStatusModel>()
        list.add(SensorsStatusModel(timeVal, "Time: "))
        list.add(SensorsStatusModel(getBatteryPercentage().toString() + "%", "Battery: "))
        list.add(SensorsStatusModel(constants.mLatitude.toString(), "Latitude: "))
        list.add(SensorsStatusModel(constants.mLongitude.toString(), "Longitude: "))
        list.add(SensorsStatusModel(constants.mAltitude.toString(), "Altitude: "))
        list.add(SensorsStatusModel(carrierName, "Service Provider: "))
        list.add(SensorsStatusModel(networkVal, "Internet: "))
        list.add(SensorsStatusModel(deviceManufacturer, "Device Model: "))
        list.add(SensorsStatusModel(deviceName, "Device Name: "))
        adapter = SensorsStatusAdapter(this, list)
        binding.StatusRecView.adapter = adapter

    }

    private fun initializers() {
        binding.header.headerBarTitleTxt.text = "Sensors"
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
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

        deviceName = Build.MODEL // returns model name
        deviceManufacturer = Build.MANUFACTURER // returns manufacturer
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        carrierName = telephonyManager!!.getNetworkOperatorName()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val se = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        val se1 = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se1,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        val se2 = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se2,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        val se3 = sensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se3,
            SensorManager.SENSOR_DELAY_GAME
        )
        val se4 = sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se4,
            SensorManager.SENSOR_DELAY_GAME
        )
        val se5 = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager!!.registerListener(
            this as SensorEventListener,
            se5,
            SensorManager.SENSOR_DELAY_FASTEST
        )

        if (!checkSensorAvailability(Sensor.TYPE_ACCELEROMETER)) {
            Toast.makeText(this, "Accelerometer Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

        if (!checkSensorAvailability(Sensor.TYPE_GYROSCOPE)) {
            Toast.makeText(this, "Gyroscope Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

        if (!checkSensorAvailability(Sensor.TYPE_GRAVITY)) {
            Toast.makeText(this, "Gravity Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

        if (!checkSensorAvailability(Sensor.TYPE_MAGNETIC_FIELD)) {
            Toast.makeText(this, "Magnetic Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

        if (!checkSensorAvailability(Sensor.TYPE_ROTATION_VECTOR)) {
            Toast.makeText(this, "Rotation Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

        if (!checkSensorAvailability(Sensor.TYPE_LIGHT)) {
            Toast.makeText(this, "Light Sensor Not available.!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkSensorAvailability(sensorType: Int): Boolean {
        var isSensor = false
        if (sensorManager!!.getDefaultSensor(sensorType) != null) {
            isSensor = true
        }
        return isSensor
    }

    override fun networkAvailable() {
        networkVal = "Internet Connected"
    }

    override fun networkUnavailable() {
        networkVal = "Internet Not Connected"
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
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(mLocationService)
        mFetchLocation.stopLocationRequest()
        sensorManager!!.unregisterListener(this)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                gpsEnableDialog!!.show()
            } catch (e: Exception) {
            }
        } else {
            try {
                gpsEnableDialog!!.dismiss()
            } catch (e: Exception) {
            }
        }
    }


    private fun getBatteryPercentage(): Int {
        return if (Build.VERSION.SDK_INT >= 21) {
            val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
            bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = registerReceiver(null, iFilter)
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = level / scale.toDouble()
            (batteryPct * 100).toInt()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (checkSensorAvailability(Sensor.TYPE_MAGNETIC_FIELD)) {
                if (event!!.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    withContext(Dispatchers.Main) {
                        binding.ValMagX.setText(df2.format(event.values.get(0)))
                        binding.ValMagY.setText(df2.format(event.values.get(1)))
                        binding.ValMagZ.setText(df2.format(event.values.get(2)))
                    }
                }
            }
            if (checkSensorAvailability(Sensor.TYPE_ACCELEROMETER)) {
                if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    withContext(Dispatchers.Main) {
                        binding.ValAccelX.text = df2.format(event.values[0])
                        binding.ValAccelY.text = df2.format(event.values[1])
                        binding.ValAccelZ.text = df2.format(event.values[2])
                    }
                }
            }

            if (checkSensorAvailability(Sensor.TYPE_GYROSCOPE)) {
                if (event!!.sensor.type == Sensor.TYPE_GYROSCOPE) {
                    withContext(Dispatchers.Main) {
                        binding.ValGyroX.text = df2.format(event.values[0])
                        binding.ValGyroY.text = df2.format(event.values[1])
                        binding.ValGyroZ.text = df2.format(event.values[2])
                    }
                }
            }

            if (checkSensorAvailability(Sensor.TYPE_GRAVITY)) {
                if (event!!.sensor.type == Sensor.TYPE_GRAVITY) {
                    withContext(Dispatchers.Main) {
                        binding.ValGravityX.text = df2.format(event.values[0])
                        binding.ValGravityY.text = df2.format(event.values[1])
                        binding.ValGravityZ.text = df2.format(event.values[2])
                    }
                }
            }

            if (checkSensorAvailability(Sensor.TYPE_ROTATION_VECTOR)) {
                if (event!!.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    withContext(Dispatchers.Main) {
                        binding.ValRotation.text = df2.format(event.values[0])
                    }
                }
            }

            if (event!!.sensor.type == Sensor.TYPE_LIGHT) {
                val currentLux = event.values[0]
                if (currentLux > maxLux) maxLux = currentLux.toDouble()
                withContext(Dispatchers.Main) {
                    binding.ValLight.setText(df2.format(maxLux))
                }
            }

        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
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