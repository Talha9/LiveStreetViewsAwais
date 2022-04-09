package com.livestreetviewmaps.livetrafficupdates.gpstools.splashModule

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivitySplashBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds.addModelToFirebase
import java.util.*
import kotlin.collections.ArrayList

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val myHandler = Handler()
    private var isFromPlay = true
    private val TAG = "SplashLog:"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isFromPlay = isFromPlayStore(this)
         try {
             FirebaseApp.initializeApp(this)
         } catch (e: Exception) {
         }
         try {
             FirebaseMessaging.getInstance()
                 .subscribeToTopic("com.livestreetviewmaps.livetrafficupdates.gpstools")
         } catch (e: Exception) {
         }


        if (isFromPlay) {
            Log.d(TAG, "isFromPlay: True ")
            LiveStreetViewMyAppAds.preReLoadAds(this)
        } else {
            Log.d(TAG, "isFromPlay: False ")
        }

        binding.animationSplashScreen.setAnimation(R.raw.splash_anim)


    }

    override fun onResume() {
        super.onResume()
        myHandler.postDelayed({
            moveToNext()
        }, 7000)
    }

    override fun onPause() {
        myHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }


    private fun moveToNext() {
        if (isFromPlay) {
            moveToMain()
        } else {
            Toast.makeText(
                this,
                "Please install this application from play store.",
                Toast.LENGTH_SHORT
            )
                .show()
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=Thrilling+Flame+Studio")
                    )
                )
                finish()
                finishAffinity()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun moveToMain() {
        val intent =
            Intent(this, PrivicyActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun isFromPlayStore(context: Context): Boolean {
        val validInstallers: List<String> =
            ArrayList(Arrays.asList("com.android.vending", "com.google.android.feedback"))
        val installer: String? =
            context.packageManager.getInstallerPackageName(context.packageName).toString()
        Log.d("verifyInstallerId", "Installed from : $installer")
        return installer != null && validInstallers.contains(installer)
    }
}