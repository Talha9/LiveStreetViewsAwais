package com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityTimeSearchBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.adapter.TimeSearchAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.callbacks.TimeSeachCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.models.TimeZoneModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

class TimeSearchActivity : AppCompatActivity(), CoroutineScope {
    lateinit var binding: ActivityTimeSearchBinding
    var isSearchAvailable = false
    var timeZoneCountryList = ArrayList<TimeZoneModel>()
    var timeZoneCountryListFiltered = ArrayList<TimeZoneModel>()
    var adapter: TimeSearchAdapter? = null
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllStreetViewList()
        onClickListeners()
        mBannerAdsSmall()
    }

    private fun onClickListeners() {

        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.header.headerBarSearchBtn.setOnClickListener {
            if (isSearchAvailable) {
                val searchTxt = binding.header.headerBarSearchTxt.text.toString().trim()
                if (searchTxt != null) {
                    binding.header.headerBarSearchTxt.setText(searchTxt)



                    UtilsFunctionClass.hideKeyboard(this)
                } else {
                    Toast.makeText(this, "Please Enter Place..!", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.header.headerBarBackBtn.visibility = View.GONE
                binding.header.headerBarTitleTxt.visibility = View.GONE
                binding.header.headerBarCancelBtn.visibility = View.VISIBLE
                binding.header.headerBarSearchTxt.visibility = View.VISIBLE
                isSearchAvailable = true
            }
        }
        binding.header.headerBarSearchTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != "") {
                    getFilteredListForText(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.header.headerBarCancelBtn.setOnClickListener {
            binding.header.headerBarBackBtn.visibility = View.VISIBLE
            binding.header.headerBarTitleTxt.visibility = View.VISIBLE
            binding.header.headerBarCancelBtn.visibility = View.GONE
            binding.header.headerBarSearchTxt.visibility = View.GONE
            isSearchAvailable = false
        }
    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
    }

    fun getFilteredListForText(text: String) {
        if (text.isNotEmpty()) {
            timeZoneCountryListFiltered.clear()
            for (i in 0..timeZoneCountryList.size - 1) {
                val myCountry = timeZoneCountryList.get(i)
                val name = myCountry.country
                if (name!!.toLowerCase().contains(text.toLowerCase())) {
                    timeZoneCountryListFiltered.add(myCountry)
                }
            }
            Log.i("filteredFlagList", "new:filteredFlagList" + timeZoneCountryListFiltered)
            adapter!!.setNewTimeZoneCountryList(timeZoneCountryListFiltered)
        }
    }


    private fun getAllStreetViewList() = launch {
        val jsonString: String =
            async {
                getdataFromJson()
            }.await()
        parseJsonStringToNewsList(jsonString)
        timeZoneCountryListFiltered.clear()
        timeZoneCountryListFiltered.addAll(timeZoneCountryList)
        withContext(Dispatchers.Main) {
            addDateToRecyclerView(timeZoneCountryListFiltered)
        }
    }

    private suspend fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = assets.open("country_clock_data/clock_countries.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }

    private fun parseJsonStringToNewsList(jsonString: String) {
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        timeZoneCountryList.clear()
        for (i in 0 until newsArray.length()) {
            Log.d("timeZoneCountryListTAG", "parseJsonStringToNewsList: " + i)
            val objinside = JSONObject(newsArray.get(i).toString())
            val timezone = objinside.getString("countriesTimezone")
            val country_code = objinside.getString("countryCode")
            val country = objinside.getString("countryName")
            try {
                timeZoneCountryList.add(TimeZoneModel(timezone, country, country_code))
            } catch (e: Exception) {
            }
        }
    }

    private fun addDateToRecyclerView(timeZoneCountryList: ArrayList<TimeZoneModel>) {
        if (timeZoneCountryList.size > 0) {
            val gridLayoutManager = GridLayoutManager(this, 1)
            binding.timeZoneRecView.layoutManager = gridLayoutManager
            adapter = TimeSearchAdapter(this, timeZoneCountryList, object : TimeSeachCallback {
                override fun onTimeZoneClick(model: TimeZoneModel) {
                    val intent = Intent()
                    intent.putExtra("timezone", model.timezone)
                    setResult(RESULT_OK, intent)
                    finish()
                }

            })
            binding.timeZoneRecView.adapter = adapter
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