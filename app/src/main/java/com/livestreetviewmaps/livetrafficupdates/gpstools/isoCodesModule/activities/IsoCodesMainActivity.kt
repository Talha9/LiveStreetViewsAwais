package com.livestreetviewmaps.livetrafficupdates.gpstools.isoCodesModule.activities

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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.RestCountriesInstance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.RestCountriesInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesModelFactory
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.mvvm.RestCountriesViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityIsoCodesMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.isoCodesModule.adapters.IsoCodeMainAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds

class IsoCodesMainActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    lateinit var binding: ActivityIsoCodesMainBinding
    var list = ArrayList<CountriesModel>()
    var adapter: IsoCodeMainAdapter? = null
    var isSearchAvailable = false
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var mRestCountriesModel: RestCountriesViewModel? = null
    var isoCodesListFiltered = ArrayList<CountriesModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIsoCodesMainBinding.inflate(layoutInflater)
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
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(
            this,
            LiveStreetViewMyAppAds.admobInterstitialAd
        )
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

            isoCodesListFiltered.clear()
            isoCodesListFiltered.addAll(list)

            Log.i("filteredFlagList", "apiCalling" + isoCodesListFiltered.size)
            setUpAdapter(isoCodesListFiltered)
        })
    }

    private fun setUpAdapter(list: ArrayList<CountriesModel>) {
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = adapter!!.getItemViewType(position)
                if (itemViewType == 0) {
                    //manager.getSpanCount();
                } else if (itemViewType == 1) {
                    return 1
                } else if (itemViewType == 2) {
                    manager.spanCount
                } else {
                    return 2
                }
                return 2
            }
        }
        binding.isoCodesRecView.layoutManager = manager
        if (list.size > 0) {
            adapter = IsoCodeMainAdapter(this, list)
        }
        binding.isoCodesRecView.adapter = adapter
        binding.progressMainBg.visibility = View.GONE


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

    private fun initialization() {
        binding.header.headerBarTitleTxt.text = "Iso Codes."
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)

    }

    fun getFilteredListForText(text: String) {
        if (text.isNotEmpty()) {
            isoCodesListFiltered.clear()
            for (i in 0..list.size - 1) {
                val myCountry = list.get(i)
                val name = myCountry.name
                if (name!!.toLowerCase().contains(text.toLowerCase())) {
                    isoCodesListFiltered.add(myCountry)
                }
            }
            Log.i("filteredFlagList", "new:filteredFlagList" + isoCodesListFiltered)
            adapter!!.setIsoCodesList(isoCodesListFiltered)

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
                binding!!.smallAd.adContainer,adView,this
            )
        }else{
            binding!!.smallAd.root.visibility= View.GONE
        }
    }

}