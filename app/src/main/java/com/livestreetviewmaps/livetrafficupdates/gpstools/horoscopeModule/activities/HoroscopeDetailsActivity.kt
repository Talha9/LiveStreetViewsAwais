package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.activities

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.horoscopeApi.Api_Instance
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.horoscopeApi.HoroscopeDao
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.horoscopeApi.HoroscopeItemModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityHoroscopeDetailsBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models.HoroscopeMainModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HoroscopeDetailsActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityHoroscopeDetailsBinding? = null
    var dataModel: HoroscopeMainModel? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoroscopeDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (intent.getParcelableExtra<HoroscopeMainModel>("horoscope_model") != null) {
            try {
                val model = intent.getParcelableExtra<HoroscopeMainModel>("horoscope_model")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.horoscopeName + "," + model.horoscopeId + "," + model.horoscopeIcon
                    )
                    binding!!.header.headerBarTitleTxt.text = model.horoscopeName
                    apiCalling(model)
                }
            } catch (e: Exception) {
            }
        }
        initialization()
        onClickListeners()
    }

    private fun initialization() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
    }

    private fun apiCalling(model: HoroscopeMainModel) {
        val horoscopeApiService = Api_Instance.getRetrofitInstance()!!.create(
            HoroscopeDao::class.java
        )
        val callHoroscopeModel = horoscopeApiService.getHoroscopes(
            model.horoscopeName, "today"
        )
        callHoroscopeModel.enqueue(object : Callback<HoroscopeItemModel?> {
            override fun onResponse(
                call: Call<HoroscopeItemModel?>,
                response: Response<HoroscopeItemModel?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val mainResponseBody = response.body()
                    binding!!.horoscopeColorInputTxt.text = mainResponseBody!!.color
                    binding!!.horoscopeCurrentDateInputTxt.text = mainResponseBody.current_date
                    binding!!.horoscopeLuckyNumberInputTxt.text = mainResponseBody.lucky_number
                    binding!!.horoscopeLuckyTimeInputTxt.text = mainResponseBody.lucky_time
                    binding!!.horoscopeDesTxt.text = mainResponseBody.description
                    binding!!.horoscopeMoodInputTxt.text = mainResponseBody.mood
                    binding!!.horoscopeTxtId.text = model.horoscopeName
                    UtilsFunctionClass.setImageInGlideFromDrawable(
                        this@HoroscopeDetailsActivity,
                        getDrawable(model.horoscopeIcon)!!,
                        binding!!.horoscopeProgress,
                        binding!!.horoscopeImgId
                    )
                } else {
                    Toast.makeText(
                        this@HoroscopeDetailsActivity,
                        "Internet ERROR..!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<HoroscopeItemModel?>, t: Throwable) {
                Toast.makeText(
                    this@HoroscopeDetailsActivity,
                    "Server Not Responding...!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
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