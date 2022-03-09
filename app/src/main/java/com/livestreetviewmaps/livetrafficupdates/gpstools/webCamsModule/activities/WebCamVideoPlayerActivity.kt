package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.content.DialogInterface
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamVideoPlayerBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamVideoLinkModel

class WebCamVideoPlayerActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityWebCamVideoPlayerBinding? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var dataModel: WebCamVideoLinkModel? = null
    var youtubeVideoStreetViewFragment: YouTubePlayerFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebCamVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initializers()
        if (intent.getParcelableExtra<WebCamVideoLinkModel>("video_url") != null) {
            try {
                val model = intent.getParcelableExtra<WebCamVideoLinkModel>("video_url")
                if (model != null) {
                    dataModel = model
                    Log.d(
                        "ModelLogCheckTAG",
                        "onItemClick: " + model.placeName + "," + model.url
                    )

                    binding!!.header.headerBarTitleTxt.text = model.placeName
                    youTubeCalling(model.url)
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
    }

    private fun initializers() {
        youtubeVideoStreetViewFragment =
            fragmentManager.findFragmentById(R.id.youtubeFragmentWebCams) as YouTubePlayerFragment
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
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