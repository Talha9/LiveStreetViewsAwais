package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivitySpaceInfoMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.adapters.SpaceInfoTagSphareAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.callbacks.TagSphereCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.helper.SpaceInfoMainHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.models.SpaceInfoMainModel
import com.moxun.tagcloudlib.view.TagCloudView


class SpaceInfoMainActivity : AppCompatActivity() {
    lateinit var binding: ActivitySpaceInfoMainBinding
    var list = ArrayList<SpaceInfoMainModel>()
    private var tagCloudView: TagCloudView? = null
    private var tagAdapter: SpaceInfoTagSphareAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpaceInfoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializers()
        listFiller()
        setupAdapter()
        onClickListeners()
        mBannerAdsSmall()
    }

    private fun onClickListeners() {
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.header.headerBarTitleTxt.text="Satellite Info."
    }
    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    private fun initializers() {
        tagCloudView = binding.tagSphare
        binding.animationViewInternetEnable.setAnimation(R.raw.stars_bg)
    }

    private fun setupAdapter() {
        tagAdapter = SpaceInfoTagSphareAdapter(this, list, object : TagSphereCallbacks {
            override fun onTagPlanetClick(model: SpaceInfoMainModel) {
                val intent=Intent(this@SpaceInfoMainActivity,SpaceInfoDetailActivity::class.java)
                intent.putExtra("space_info",model)
                startActivity(intent)
            }

        })
        tagCloudView!!.setAdapter(tagAdapter)
        tagCloudView!!.setScrollSpeed(5f)
        tagCloudView!!.setRadiusPercent(0.8f)
        tagCloudView!!.autoScrollMode = TagCloudView.MODE_UNIFORM

    }

    private fun listFiller() {
        list = SpaceInfoMainHelper.fillMainSphareTagList()
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