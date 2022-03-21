package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.activities

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdSize
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.RestCountriesInstance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.RestCountriesInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesModelFactory
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.adapter.CountriesInfoMainAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.adapter.CountryInfoDetailsAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.callbacks.RestCountriesMainCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityCountryInfoMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds

class CountryInfoMainActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    lateinit var binding: ActivityCountryInfoMainBinding
    var list = ArrayList<CountriesModel>()
    var isSearchAvailable = false
    var countryInfoListFiltered = ArrayList<CountriesModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    var adapter: CountriesInfoMainAdapter? = null
    var detailsBottomAdapter: CountryInfoDetailsAdapter? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var mRestCountriesModel: RestCountriesViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryInfoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiCalling()
        initialization()
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
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(this,LiveStreetViewMyAppAds.admobInterstitialAd)
    }

    fun getFilteredListForText(text: String) {
        if (text.isNotEmpty()) {
            countryInfoListFiltered.clear()
            for (i in 0..list.size - 1) {
                val myCountry = list.get(i)
                val name = myCountry.name
                if (name!!.toLowerCase().contains(text.toLowerCase())) {
                    countryInfoListFiltered.add(myCountry)
                }
            }
            Log.i("filteredFlagList", "new:filteredFlagList" + countryInfoListFiltered)
            adapter!!.setCountryInfoList(countryInfoListFiltered)

        }
    }

    private fun setUpAdapter(list: ArrayList<CountriesModel>) {
        val manager = LinearLayoutManager(this)
        binding.countriesInfoRecView.layoutManager = manager
        if (list.size > 0) {
            adapter = CountriesInfoMainAdapter(this, list, object : RestCountriesMainCallbacks {
                override fun onCountryClick(model: CountriesModel) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    UtilsFunctionClass.hideKeyboard(this@CountryInfoMainActivity)
                    setUpBottomSheetData(model)
                }

            })
        }
        binding.countriesInfoRecView.adapter = adapter
        /*  binding.progressMainBg.visibility=View.GONE*/

    }

    private fun initialization() {
        binding.header.headerBarTitleTxt.text = "Country Info."
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bSheet.bottomSheetDrawer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun apiCalling() {
        val restCountriesRetrofit =
            RestCountriesInstance.getInstance().create(RestCountriesInterface::class.java)
        val mRestCountriesRepository = RestCountriesRepository(restCountriesRetrofit)
        mRestCountriesModel =
            ViewModelProvider(this, RestCountriesModelFactory(mRestCountriesRepository)).get(
                RestCountriesViewModel::class.java
            )


        mRestCountriesModel!!.callRestCountriesData()
        mRestCountriesModel!!.mRestCountriesData.observe(this, {
            if (it != null && it.size > 0) {
                list = it
            }
            countryInfoListFiltered.clear()
            countryInfoListFiltered.addAll(list)
            Log.i("filteredFlagList", "apiCalling" + countryInfoListFiltered.size)
            setUpAdapter(countryInfoListFiltered)
        })


    }

    override fun networkAvailable() {
        try {
            internetDialog!!.dismiss()
            apiCalling()
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

    fun setUpBottomSheetData(model: CountriesModel) {
        try {
            val flage =
                "https://flagpedia.net/data/flags/normal/${model.alpha2Code.toLowerCase()}.png"
            Log.i("flagezzz", ": " + flage)
            Glide.with(this).load(flage).into(binding.bSheet.countryInfiDetailImg)
        } catch (e: Exception) {
        }
        val list = ArrayList<String>()
        model.name?.let {
            list.add(it)
        }
        model.population.toString().let {
            list.add(it)
        }
        model.area.toString().let {
            list.add(it)
        }
        model.timezones[0].toString().let {
            list.add(it)
        }
        model.currencies?.let {
            if (it.size > 0) {
                list.add(it[0].name)
            }
        }

        model.languages[0]?.let {
            it.name?.let {
                list.add(it)
            }
        }
        model.latlng[0]?.let {
            list.add(it.toString())
        }

        model.latlng[1].let {
            list.add(it.toString())
        }
        val manager = LinearLayoutManager(this)
        binding.bSheet.countryInfoDetailsRecView.layoutManager = manager
        if (list.size > 0) {
            detailsBottomAdapter = CountryInfoDetailsAdapter(this, list)
        }
        binding.bSheet.countryInfoDetailsRecView.adapter = detailsBottomAdapter
        binding.bSheet.progressMainBg.visibility = View.GONE
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
                binding.smallAd.adContainer,adView,this
            )
        }else{
            binding.smallAd.root.visibility= View.GONE
        }
    }
}