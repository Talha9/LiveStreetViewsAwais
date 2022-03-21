package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.activities

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.SpaceInfoApiInstance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.SpaceInfoApiInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm.SpaceInfoModelFactory
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm.SpaceInfoRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.spaceInfoApi.mvvm.SpaceInfoViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivitySpaceInfoDetailBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.adapters.SpaceInfoDetailsTagSphareAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.models.SpaceInfoMainModel

class SpaceInfoDetailActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    lateinit var binding: ActivitySpaceInfoDetailBinding
    lateinit var dataModel: SpaceInfoMainModel
    var adapter: SpaceInfoDetailsTagSphareAdapter? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    private var mSpaceInfoViewModel: SpaceInfoViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpaceInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.getParcelableExtra<SpaceInfoMainModel>("space_info") != null) {
            try {
                val model = intent.getParcelableExtra<SpaceInfoMainModel>("space_info")
                if (model != null) {
                    dataModel = model
                    Log.d("SpaceInfoDetailActivityTAG", "onCreate: " + dataModel!!.planetName)
                    binding.header.headerBarTitleTxt.text = "Planet: " + dataModel.planetName
                    UtilsFunctionClass.setImageInGlideFromDrawable(
                        this,
                        getDrawable(dataModel.planetImg)!!,
                        binding.planetProgress,
                        binding.planetImg
                    )
                    apiCalling(dataModel.planetName)

                }
            } catch (e: Exception) {
            }
        }
        onClickListeners()
        mBannerAdsSmall()
        initialization()

    }

    private fun initialization() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        internetDialog = InternetDialog(this)

    }

    private fun apiCalling(planetImg: String) {
        val spaceInfoRetrofit =
            SpaceInfoApiInstance.getInstance().create(SpaceInfoApiInterface::class.java)
        val mSpaceInfoRepository = SpaceInfoRepository(spaceInfoRetrofit)
        mSpaceInfoViewModel =
            ViewModelProvider(this, SpaceInfoModelFactory(mSpaceInfoRepository)).get(
                SpaceInfoViewModel::class.java
            )
        mSpaceInfoViewModel!!.callForData(planetImg)
        mSpaceInfoViewModel!!.mSpaceData.observe(this, {
            if (it != null) {
                Log.d("mSpaceInfoViewModelTAG", "apiCalling: " + it.name)
                binding.planetNameTxt.text = it.englishName

                binding.dataTxtview.text = "Name: ${it.name}\n " +
                        "Moons: ${if (it.moons == null) "No Moon" else it.moons.size}\n" +
                        "Mass: ${it.mass.massValue}\n" +
                        "Planet:${it.isPlanet}\n" +
                        "Volume: ${it.vol.volValue}\n" +
                        "Density: ${it.density}\n" +
                        "Gravity: ${it.gravity}\n" +
                        "Discovered by: ${it.discoveredBy}\n" +
                        "Discovered Date: ${it.discoveryDate}\n" +
                        "Body Type: ${it.bodyType}\n"

            }
        })


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

    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
    }
}