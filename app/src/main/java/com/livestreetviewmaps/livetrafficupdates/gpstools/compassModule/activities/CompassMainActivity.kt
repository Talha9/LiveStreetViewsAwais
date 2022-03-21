package com.livestreetviewmaps.livetrafficupdates.gpstools.compassModule.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.compassModule.Callbacks.CompassCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.compassModule.utils.CompassFormatter
import com.livestreetviewmaps.livetrafficupdates.gpstools.compassModule.utils.CompassFunctionality
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityCompassMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds
import java.text.DecimalFormat

class CompassMainActivity : AppCompatActivity(){
    lateinit var binding: ActivityCompassMainBinding
    private var currentAzimuth = 0f
    var df2: DecimalFormat? = null
    var mCompassFunctionality: CompassFunctionality? = null
    var mCompassFormatter: CompassFormatter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompassMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializers()
        mBannerAdsSmall()
        onClickListeners()
        nativeAds()
    }

    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(this,LiveStreetViewMyAppAds.admobInterstitialAd)
    }

    private fun initializers() {
        df2 = DecimalFormat("#.##")
        mCompassFormatter = CompassFormatter(this)
        mCompassFunctionality = CompassFunctionality(this)
        val l = getCompassListener()
        mCompassFunctionality!!.setListener(l)
    }

    override fun onStart() {
        super.onStart()
        mCompassFunctionality!!.start()
    }

    override fun onPause() {
        mCompassFunctionality!!.stop()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mCompassFunctionality!!.start()
    }

    override fun onStop() {
        mCompassFunctionality!!.stop()
        super.onStop()
    }

    private fun getCompassListener(): CompassCallbacks {
        return object : CompassCallbacks {
            override fun onNewAzimuth(azimuth: Float) {
                runOnUiThread {
                    adjustArrow(azimuth)
                    adjustSotwLabel(azimuth)
                }
            }

            override fun getXYValues(x: Float, y: Float) {
                binding.XValue.setText(df2!!.format(x.toDouble()) + 0x00B0.toChar())
                binding.YValue.setText(df2!!.format(y.toDouble()) + 0x00B0.toChar())
            }
        }
    }

    private fun adjustArrow(azimuth: Float) {
        Log.d(
            "Comp", "will set rotation from " + currentAzimuth + " to "
                    + azimuth
        )
        val an: Animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.CompassDailer.startAnimation(an)
    }

    private fun adjustSotwLabel(azimuth: Float) {
        binding.CompassText.setText(mCompassFormatter!!.format(azimuth))
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