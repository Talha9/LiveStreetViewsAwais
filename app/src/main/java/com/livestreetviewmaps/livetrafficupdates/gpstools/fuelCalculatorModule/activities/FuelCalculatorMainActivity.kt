package com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.FuelApiInstance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.FuelApiInterface
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm.FuelApiModelFactory
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm.FuelApiRepository
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.fuelApi.mvvm.FuelApiViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityFuelCalculatorMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.callbacks.FuelCountrySpinnerCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.dialogs.FuelCountrySpinnerDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.models.FuelCalculatorModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import java.text.DecimalFormat

class FuelCalculatorMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityFuelCalculatorMainBinding
    var mFuelApiViewModel: FuelApiViewModel? = null
    var list = ArrayList<FuelCalculatorModel>()
    var distanceUnitItem: String? = null
    var fuelUnitItem: String? = null
    var fuelPrice = 0.0
    var fuelDistance = 0.0
    var fuelMilage = 0.0
    var totalPrice = 0.0
    var df2 = DecimalFormat("#.#")
    var mFuelCountrySpinnerDialog: FuelCountrySpinnerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelCalculatorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.headerBarTitleTxt.text="Fuel Calculator"

        onClickListeners()
        apiCalling()
        setUpSpinners()
        mBannerAdsSmall()

    }

    private fun setUpSpinners() {
        binding.DistanceTypeSpinner.setOnClickListener { binding.DistanceTypeSpinner.show() }
        binding.DistanceTypeSpinner.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String?> {
            override fun onItemSelected(
                oldIndex: Int,
                oldItem: String?,
                newIndex: Int,
                newItem: String?
            ) {
                distanceUnitItem = newItem!!

            }
        })
        binding.FuelTypeSpinner.setOnClickListener { binding.FuelTypeSpinner.show() }
        binding.FuelTypeSpinner.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String> {
            override fun onItemSelected(
                oldIndex: Int,
                oldItem: String?,
                newIndex: Int,
                newItem: String
            ) {
                fuelUnitItem = newItem
            }

        })

    }

    private fun onClickListeners() {
        binding.CountryTypeSpinner.setOnClickListener {
            if (list.size > 0) {
                mFuelCountrySpinnerDialog =
                    FuelCountrySpinnerDialog(this, object : FuelCountrySpinnerCallbacks {
                        override fun onSpinnerItemClicked(model: FuelCalculatorModel) {
                            binding.fuelCalculationTxt.text = model.country_name
                            try {
                                Glide.with(this@FuelCalculatorMainActivity).load(model.flagLink)
                                    .into(binding.fuelCalculationFlagImg)
                            } catch (e: Exception) {
                            }
                            binding.fuelCountrySelectedTxt.text = model.rate
                            fuelPrice = (model.rate.split(" ")[0]).toDouble()
                        }

                    }, list)
                try {
                    mFuelCountrySpinnerDialog!!.show()
                } catch (e: Exception) {
                }
            } else {
                Toast.makeText(this, "Data Not Loaded!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.ageCalculatorBtn.setOnClickListener {
            if (fuelPrice != 0.0 && fuelUnitItem != null && distanceUnitItem != null) {
                checkValues()
            } else {
                Toast.makeText(this, "Values Missing!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(this,LiveStreetViewMyAppAds.admobInterstitialAd)
    }

    private fun checkValues() {
        if (binding.fuelDisUnitSelectedTxt.text!!.isEmpty()) {
            binding.fuelDisUnitSelectedTxt.error = "Empty"
        } else if (binding.fuelUnitSelectedTxt.text!!.isEmpty()) {
            binding.fuelUnitSelectedTxt.error = "Empty"
        } else {
            fuelDistance = (binding.fuelDisUnitSelectedTxt.text.toString()).toDouble()
            fuelMilage = (binding.fuelUnitSelectedTxt.text.toString()).toDouble()
            calculateFuelRate(
                fuelPrice,
                fuelUnitItem!!,
                distanceUnitItem!!,
                fuelDistance,
                fuelMilage
            )
            UtilsFunctionClass.hideKeyboard(this)
        }

    }

    private fun calculateFuelRate(
        fuelPrice: Double,
        fuelUnitItem: String,
        distanceUnitItem: String,
        fuelDistance: Double,
        fuelMilage: Double
    ) {

        var exactDis = 0.0
        var exactFuel = 0.0
        if (distanceUnitItem == "miles") {
            exactDis = fuelDistance * 0.621371
        } else if (distanceUnitItem == "m") {
            exactDis = fuelDistance / 100
        } else if (distanceUnitItem == "km") {
            exactDis = fuelDistance
        } else {
            Toast.makeText(this, "Please Select Distance Unit", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("TotalResult", exactDis.toString())

        if (fuelUnitItem == "barrel") {
            exactFuel = exactDis / fuelMilage
            exactFuel = exactFuel * 158.987
        } else if (fuelUnitItem == "gallon") {
            exactFuel = exactDis / fuelMilage
            exactFuel = exactFuel * 3.78541
        } else if (fuelUnitItem == "Litre") {
            exactFuel = exactDis / fuelMilage
        } else {
            Toast.makeText(this, "Please Select Fuel Unit", Toast.LENGTH_SHORT).show()
            return
        }

        totalPrice = exactFuel * fuelPrice
        binding.fuelTotalLitreTxt.setText(df2.format(exactFuel))
        binding.fuelTotalPricetxt.setText(df2.format(totalPrice))
        binding.parentLayoutNested.scrollTo(0, binding.parentLayoutNested.getBottom())
    }


    private fun apiCalling() {
        val mFuelRatesRetrofit = FuelApiInstance.getInstance().create(FuelApiInterface::class.java)
        val mFuelApiRepository = FuelApiRepository(mFuelRatesRetrofit)
        mFuelApiViewModel = ViewModelProvider(this, FuelApiModelFactory(mFuelApiRepository)).get(
            FuelApiViewModel::class.java
        )
        mFuelApiViewModel!!.callForData()
        mFuelApiViewModel!!.fuelRatesData.observe(this, {
            if (it != null) {
                Log.d("ViewModelTAG", "apiCalling: " + it.response[0].country_name)
                for (i in it.response) {
                    list.add(
                        FuelCalculatorModel(
                            i.country_name,
                            "https://flagpedia.net/data/flags/normal/${
                                i.short_form.toLowerCase()
                            }.png",
                            i.petrol_price
                        )
                    )
                }
            }

        })
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