package com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.callbacks.CalenderCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.dialogs.CalenderDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.models.AgeCalculatorModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.utilities.CalenderParams
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityAgeCalculatorMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppShowAds
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class AgeCalculatorMainActivity : AppCompatActivity() {
    var minimumAge = 0
    var calDialog: CalenderDialog? = null
    lateinit var calHelper: Calendar
    var calculatedDay: Long = 0
    var temp: Long = 0
    var resDay: Long = 0
    var resMonth: Long = 0
    var resYear: Long = 0
    var mDay: Int = 0
    var mMonth: Int = 0
    var mYear: Int = 0
    var timp2: Long = 0
    var list = ArrayList<AgeCalculatorModel>()
    lateinit var binding: ActivityAgeCalculatorMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgeCalculatorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializers()
        setUpCalenders()
        mBannerAdsSmall()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpCalenders() {
        binding.ageCalculatorBirthCalender.setOnClickListener {
            calDialog = CalenderDialog(this, object : CalenderCallback {
                override fun onOkPressed(y: Int, m: Int, d: Int) {
                    mDay = d
                    mMonth = m
                    mYear = y
                    Log.d("setUpCalendersTAG", "onOkPressed: " + y + "," + m + "," + d)
                    binding.ageCalculatorBirthTxt.text =
                        d.toString() + " - " + m.toString() + " - " + y.toString()
                    initialiseData()

                }
            })
            try {
                calDialog!!.show()
            } catch (e: Exception) {
            }
        }
        binding.ageCalculatorBtn.setOnClickListener {
            if (mDay != 0 && mMonth != 0 && mYear != 0) {
                calculateAge()
            } else {
                Toast.makeText(this, "Please First Select Date!", Toast.LENGTH_SHORT).show()
            }

        }
        binding.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding.ageClearBtn.setOnClickListener {
            defaults()
        }


    }

    override fun onBackPressed() {
        LiveStreetViewMyAppShowAds.mediationBackPressedSimpleLiveStreetView(this,LiveStreetViewMyAppAds.admobInterstitialAd)
    }

    fun defaults() {
        calculatedDay = 0
        temp = 0
        resDay = 0
        resMonth = 0
        resYear = 0
        mDay = 0
        mMonth = 0
        mYear = 0
        timp2 = 0
        binding.ageCalculatorBirthTxt.text = "0 - 0 - 0"
        binding.ageCalculatorYearTxt.text = "0"
        binding.ageCalculatorMonthTxt.text = "0"
        binding.ageCalculatorDayTxt.text = "0"
        binding.ageCalculatorBirthYearTxt.text = "0"
        binding.ageCalculatorBirthDayCountTxt.text = "0"
    }

    fun calculateAge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            calculatedDay = getDiff(CalenderParams.DAYS)
            temp = (calculatedDay.toInt() / 365).toLong()
            resYear = temp

            //Years
            //Years
            binding.ageCalculatorYearTxt.text = resYear.toString()

            //Months
            //Months
            temp = (calculatedDay.toInt() % 365).toLong()
            temp = temp / 31
            resMonth = temp
            binding.ageCalculatorMonthTxt.text = resMonth.toString()

            //Days
            //Days
            temp = (calculatedDay.toInt() % 365).toLong()
            temp %= 31
            resDay = temp
            binding.ageCalculatorDayTxt.text = resDay.toString()


            val cal = Calendar.getInstance()

            cal[Calendar.DAY_OF_MONTH] = mDay
            cal[Calendar.MONTH] = mMonth
            cal[Calendar.YEAR] = mYear


            val today: LocalDate = LocalDate.now()
            val birthday: LocalDate = LocalDate.of(
                calHelper[Calendar.YEAR],
                (calHelper[Calendar.MONTH] + 1),
                calHelper[Calendar.DAY_OF_MONTH]
            )

            var nextBDay: LocalDate = birthday.withYear(today.year)
            Log.d("nextBDayTAG", "calculateAge: " + nextBDay)

            if (nextBDay.isBefore(today) || nextBDay.isEqual(today)) {
                nextBDay = nextBDay.plusYears(1)
            }

            var p2: Long = ChronoUnit.DAYS.between(today, nextBDay)

            timp2 = (p2 / 30.5).toLong()
            val mont = timp2
            if (mont != 12L)
                binding.ageCalculatorBirthYearTxt.text = mont.toString()

            p2 = (p2 % 30.5).toLong()
            binding.ageCalculatorBirthDayCountTxt.text = p2.toString()

        } else {
            Toast.makeText(this, "Your device version is low", Toast.LENGTH_LONG).show()
        }
    }


    private fun initializers() {
        calHelper = Calendar.getInstance()

        calHelper.add(Calendar.YEAR, minimumAge)

        binding.ageCalculatorCrntTxt.text =
            calHelper[Calendar.DAY_OF_MONTH].toString() + " - " + (calHelper[Calendar.MONTH] + 1).toString() + " - " + calHelper[Calendar.YEAR]
        binding.ageCalculatorCrntDayTxt.text = getCalculatedDate("EEEE", 0)
        binding.header.headerBarTitleTxt.text = "Age Calculator"
    }

    private fun initialiseData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            list = ArrayList()
            list.add(AgeCalculatorModel(" " + getDiff(CalenderParams.MONTHS), "months"))
            list.add(AgeCalculatorModel(" " + getDiff(CalenderParams.WEEKS), "weeks"))
            list.add(AgeCalculatorModel(" " + getDiff(CalenderParams.DAYS), "days"))
            list.add(AgeCalculatorModel(" " + getDiff(CalenderParams.SECONDS), "seconds"))
        } else {
            Toast.makeText(this, "Your device version is low", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDiff(duration: CalenderParams?): Long {

        val currentMonth = calHelper[Calendar.MONTH] + 1 //+1 since months are 0 indexed
        val currentDayOfMonth = calHelper[Calendar.DAY_OF_MONTH]
        val currentYear = calHelper[Calendar.YEAR]
        val startDate: LocalDate = LocalDate.of(
            mYear, mMonth, mDay
        )
        val endDate: LocalDate = LocalDate.of(currentYear, currentMonth, currentDayOfMonth)
        var diff: Long = 0 //default value
        when (duration) {
            CalenderParams.SECONDS -> {
                val startDateSec: LocalDateTime = LocalDateTime.of(
                    mYear, mMonth, mDay, 0, 0, 0
                )
                val endDateSec: LocalDateTime = LocalDateTime.of(
                    currentYear, currentMonth,
                    currentDayOfMonth,
                    calHelper[Calendar.HOUR_OF_DAY],
                    calHelper[Calendar.MINUTE],
                    calHelper[Calendar.SECOND]
                )
                val temp: LocalDateTime = LocalDateTime.from(startDateSec)
                diff = temp.until(endDateSec, ChronoUnit.SECONDS)
            }

            CalenderParams.DAYS -> diff = ChronoUnit.DAYS.between(startDate, endDate)
            CalenderParams.WEEKS -> diff = ChronoUnit.WEEKS.between(startDate, endDate)
            CalenderParams.MONTHS -> diff = ChronoUnit.MONTHS.between(startDate, endDate)
            CalenderParams.YEARS -> diff = ChronoUnit.YEARS.between(startDate, endDate)
        }
        return diff
    }

    fun getCalculatedDate(dateFormat: String?, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
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