package com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.activities

import android.content.DialogInterface
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityDesertsDetailsBinding

class DesertsDetailsActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener {
    var binding:ActivityDesertsDetailsBinding?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog?=null
    var youtubeVideoStreetViewFragment: YouTubePlayerFragment? = null
    var dataModel:DesertsModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDesertsDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initializers()
        if (intent.getParcelableExtra<DesertsModel>("deserts_model") != null) {
            try {
                val model = intent.getParcelableExtra<DesertsModel>("deserts_model")
                if (model != null) {
                    dataModel=model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.desertsImg + "," + model.deserts360Url + "," + model.desertsWebCamUrl + "," + model.desertsName)

                    binding!!.header.headerBarTitleTxt.text=model.desertsName
                    binding!!.desertsBg.visibility= View.VISIBLE
                    binding!!.btnStreetView.setBackgroundDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.back_btn_bg
                        ))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding!!.btnStreetView.compoundDrawableTintList= ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.ThemeColor))
                        binding!!.btnStreetView.setTextColor(getColor(R.color.ThemeColor))
                    }
                    youtubeVideoStreetViewFragment!!.view!!.visibility= View.GONE
                    UtilsFunctionClass.setImageInGlideFromString(
                        this,
                        dataModel!!.desertsImg,
                        binding!!.progressMainBg,
                        binding!!.desertsBg
                    )
                }
            } catch (e: Exception) {
            }
        }
        onClickListeners()
    }
    private fun onClickListeners() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.btn360View.setOnClickListener {
            binding!!.desertsBg.visibility=View.GONE
            youtubeVideoStreetViewFragment!!.view!!.visibility=View.VISIBLE
            youTubeCalling(dataModel!!.deserts360Url)
            it.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.back_btn_bg))
            binding!!.btnStreetView.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            binding!!.btnDroneView.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding!!.btn360View.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.ThemeColor))
                binding!!.btnStreetView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btnDroneView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btn360View.setTextColor(getColor(R.color.ThemeColor))
                binding!!.btnStreetView.setTextColor(getColor(R.color.white))
                binding!!.btnDroneView.setTextColor(getColor(R.color.white))

            }
        }
        binding!!.btnDroneView.setOnClickListener {
            binding!!.desertsBg.visibility=View.GONE
            youtubeVideoStreetViewFragment!!.view!!.visibility=View.VISIBLE
            youTubeCalling(dataModel!!.desertsWebCamUrl)
            it.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.back_btn_bg))
            binding!!.btnStreetView.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            binding!!.btn360View.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding!!.btnDroneView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.ThemeColor))
                binding!!.btnStreetView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btn360View.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btnDroneView.setTextColor(getColor(R.color.ThemeColor))
                binding!!.btnStreetView.setTextColor(getColor(R.color.white))
                binding!!.btn360View.setTextColor(getColor(R.color.white))

            }
        }
        binding!!.btnStreetView.setOnClickListener {
            binding!!.desertsBg.visibility=View.VISIBLE
            youtubeVideoStreetViewFragment!!.view!!.visibility=View.GONE
            UtilsFunctionClass.setImageInGlideFromString(
                this,
                dataModel!!.desertsImg,
                binding!!.progressMainBg,
                binding!!.desertsBg
            )
            it.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.back_btn_bg))
            binding!!.btn360View.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            binding!!.btnDroneView.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_background_theme_color))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding!!.btnStreetView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.ThemeColor))
                binding!!.btn360View.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btnDroneView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                binding!!.btnStreetView.setTextColor(getColor(R.color.ThemeColor))
                binding!!.btnDroneView.setTextColor(getColor(R.color.white))
                binding!!.btn360View.setTextColor(getColor(R.color.white))

            }
        }
    }


    private fun initializers() {
        youtubeVideoStreetViewFragment =
            fragmentManager.findFragmentById(R.id.youtubeFragmentDeserts) as YouTubePlayerFragment
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog= InternetDialog(this)

    }
    private fun youTubeCalling(Url: String) {
        val uri = Uri.parse(Url)
        youtubeVideoStreetViewFragment!!.initialize(
            constants.api_key,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer,
                    b: Boolean
                ) {
                    youTubePlayer.loadVideo(uri.toString())
                    youTubePlayer.play()
                }
                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
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