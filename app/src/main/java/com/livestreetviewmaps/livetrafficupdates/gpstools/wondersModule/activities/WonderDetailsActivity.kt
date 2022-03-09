package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.LocationService
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWonderDetailsBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel

class WonderDetailsActivity : AppCompatActivity(),NetworkStateReceiver.NetworkStateReceiverListener{
    var binding: ActivityWonderDetailsBinding? = null
    var youtubeVideoStreetViewFragment: YouTubePlayerFragment? = null
    var dataModel:WondersModel?=null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog:InternetDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWonderDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initializers()
        if (intent.getParcelableExtra<WondersModel>("wonder_model") != null) {
            try {
                val model = intent.getParcelableExtra<WondersModel>("wonder_model")
                if (model != null) {
                    dataModel=model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.wonderImg + "," + model.wonderLatitude + "," + model.wonderLongitude + "," + model.wonderName)
                    binding!!.header.headerBarTitleTxt.text=model.wonderName
                    binding!!.wonderBg.visibility=View.VISIBLE
                    binding!!.btnStreetView.setBackgroundDrawable(AppCompatResources.getDrawable(
                        this,
                        R.drawable.back_btn_bg
                    ))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding!!.btnStreetView.compoundDrawableTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.ThemeColor))
                        binding!!.btnStreetView.setTextColor(getColor(R.color.ThemeColor))
                    }
                    youtubeVideoStreetViewFragment!!.view!!.visibility=View.GONE
                    UtilsFunctionClass.setImageInGlideFromString(
                        this,
                        dataModel!!.wonderImg,
                        binding!!.progressMainBg,
                        binding!!.wonderBg
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
           binding!!.wonderBg.visibility=View.GONE
           youtubeVideoStreetViewFragment!!.view!!.visibility=View.VISIBLE
           youTubeCalling(dataModel!!.wonder360Url)
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
            binding!!.wonderBg.visibility=View.GONE
            youtubeVideoStreetViewFragment!!.view!!.visibility=View.VISIBLE
            youTubeCalling(dataModel!!.wonderWebCamUrl)
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
            binding!!.wonderBg.visibility=View.VISIBLE
            youtubeVideoStreetViewFragment!!.view!!.visibility=View.GONE
            UtilsFunctionClass.setImageInGlideFromString(
                this,
                dataModel!!.wonderImg,
                binding!!.progressMainBg,
                binding!!.wonderBg
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
            fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
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


    override fun onDestroy() {
        networkStateReceiver!!.removeListener(this)
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
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
}