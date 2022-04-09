package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamCountryListBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter.WebCamCountryAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCountryClickModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamCountryModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

class WebCamCountryListActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityWebCamCountryListBinding
    private var job: Job = Job()
    var countryList = ArrayList<WebCamCountryModel>()
    var adapter: WebCamCountryAdapter?=null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebCamCountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickListener()
        getAllCountries()
        mBannerAdsSmall()
    }

    private fun getAllCountries() = launch {
        val jsonString: String =
            async {
                getdataFromJson()
            }.await()
        parseJsonStringToNewsList(jsonString)
        withContext(Dispatchers.Main) {
            addDateToRecyclerView(countryList)
        }

    }

    private fun addDateToRecyclerView(countryList: ArrayList<WebCamCountryModel>) {
        binding.webCamCountryRecView.layoutManager=LinearLayoutManager(this)
        adapter= WebCamCountryAdapter(this,countryList,object :WebCamCountryClickModel{
            override fun onCountryClick(model: WebCamCountryModel) {
                val intent = Intent(this@WebCamCountryListActivity, WebCamsMainActivity::class.java)
                intent.putExtra("country_model",model )
                startActivity(intent)
            }
        })
        binding.webCamCountryRecView.adapter=adapter

    }


    private suspend fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = assets.open("country_cam_code.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }



    private fun onClickListener() {
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

    private fun parseJsonStringToNewsList(jsonString: String) {
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            Log.d("timeZoneCountryListTAG", "parseJsonStringToNewsList: " + i)
            val objinside = JSONObject(newsArray.get(i).toString())
            val name = objinside.getString("name")
            val dialcode = objinside.getString("dialcode")
            val code = objinside.getString("code")
            try {
                countryList.add(
                    WebCamCountryModel(
                        name,
                        dialcode,
                        code
                    )
                )
            } catch (e: Exception) {
            }
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
                binding!!.smallAd.adContainer, adView, this
            )
        } else {
            binding!!.smallAd.root.visibility = View.GONE
        }
    }
}