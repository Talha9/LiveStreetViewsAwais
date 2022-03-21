package com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.LocationDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWorldClockMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import java.text.SimpleDateFormat
import java.util.*

class WorldClockMainActivity : AppCompatActivity(), LocationDialogCallback {
    lateinit var binding: ActivityWorldClockMainBinding
    lateinit var mFetchLocation: LocationClass
    lateinit var mLocationService: LocationService
    var gpsEnableDialog: LocationDialog? = null
    private var sharedPreferences: SharedPreferences? = null
    override fun onResume() {
        super.onResume()
        clockData()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldClockMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.headerBarTitleTxt.text = "World Clock"
        initializers()
        onClickListeners()
        mBannerAdsSmall()
    }


    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.addNewTime1.setOnClickListener {
            val intent = Intent(this, TimeSearchActivity::class.java)
            startActivityForResult(intent, 1)
        }
        binding.addNewTime2.setOnClickListener {
            val intent = Intent(this, TimeSearchActivity::class.java)
            startActivityForResult(intent, 2)
        }
        binding.addNewTime3.setOnClickListener {
            val intent = Intent(this, TimeSearchActivity::class.java)
            startActivityForResult(intent, 3)
        }
        binding.addNewTime4.setOnClickListener {
            val intent = Intent(this, TimeSearchActivity::class.java)
            startActivityForResult(intent, 4)
        }
    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    private fun initializers() {
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
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


    private fun clockData() {

        val timeZone1 = sharedPreferences!!.getString("timeZone1", "America/New_York")
        val timeZone2 = sharedPreferences!!.getString("timeZone2", "Europe/London")
        val timeZone3 = sharedPreferences!!.getString("timeZone3", "Europe/Germany")
        val timeZone4 = sharedPreferences!!.getString("timeZone4", "Asia/Tokyo")

        binding.clock.setColor(getColor(R.color.ThemeColor))
            .setDiameterInDp(150.0f)
            .setOpacity(1.0f)


        binding.placeName1.text = timeZone1
        binding.clock1.timeZone = timeZone1


        binding.placeName2.text = timeZone2
        binding.clock2.timeZone = timeZone2


        binding.placeName3.text = timeZone3
        binding.clock3.timeZone = timeZone3

        binding.placeName4.text = timeZone4
        binding.clock4.timeZone = timeZone4


        binding.date.text = getCalculatedDate("MMMM", 0) + " " + getCalculatedDate(
            "dd",
            0
        ) + ", " + getCalculatedDate("YYYY", 0)
        binding.placeName.text = constants.countryName
    }

    fun getCalculatedDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences!!.edit()
                        .putString("timeZone1", data!!.getStringExtra("timezone")).apply()
                    clockData()
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences!!.edit()
                        .putString("timeZone2", data!!.getStringExtra("timezone")).apply()
                    clockData()
                }
            }
            3 -> {
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences!!.edit()
                        .putString("timeZone3", data!!.getStringExtra("timezone")).apply()
                    clockData()
                }
            }
            4 -> {
                if (resultCode == Activity.RESULT_OK) {
                    sharedPreferences!!.edit()
                        .putString("timeZone4", data!!.getStringExtra("timezone")).apply()
                    clockData()
                }
            }
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

    override fun onDestroy() {
        unregisterReceiver(mLocationService)
        mFetchLocation.stopLocationRequest()
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
                binding!!.smallAd.adContainer, adView, this
            )
        } else {
            binding!!.smallAd.root.visibility = View.GONE
        }
    }
}