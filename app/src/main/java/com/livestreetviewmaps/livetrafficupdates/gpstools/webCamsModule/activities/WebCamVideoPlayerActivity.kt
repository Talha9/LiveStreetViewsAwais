package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.NetworkStateReceiver
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs.InternetDialog
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityWebCamVideoPlayerBinding

class WebCamVideoPlayerActivity : AppCompatActivity(),
    NetworkStateReceiver.NetworkStateReceiverListener {
    var binding: ActivityWebCamVideoPlayerBinding? = null
    private var networkStateReceiver: NetworkStateReceiver? = null
    var internetDialog: InternetDialog? = null
    var mIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebCamVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        mIntent = getIntent()

        initializers()

    }


    private fun initializers() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        internetDialog = InternetDialog(this)
        binding!!.WebCamView.settings.javaScriptEnabled = true
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

    override fun onResume() {
        super.onResume()
        if (mIntent != null) {
            if (intent.getStringExtra("video_url") != null) {
                try {
                    val str = intent.getStringExtra("video_url")
                    if (str != null) {
                        binding!!.WebCamView.loadUrl(str)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
}