package com.livestreetviewmaps.livetrafficupdates.gpstools.splashModule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityPrivicyBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.activities.HomeActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds
import kotlin.random.Random

class PrivicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivicyBinding
    private var sharedLiveStreetViewPreferences: SharedPreferences? = null
    private var listOfKeys = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        onClickListeners()
        fillMyList()
    }

    private fun initialization() {
        sharedLiveStreetViewPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val isSceondTimet = sharedLiveStreetViewPreferences!!.getBoolean("isSceondTime", false)
        if (isSceondTimet) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun onClickListeners() {

        binding.privacyMoveToMainBtn.setOnClickListener {
            if (binding.privacyCheck.isChecked) {
                sharedLiveStreetViewPreferences!!.edit()
                    .putBoolean("isSceondTime", true).apply()
                startActivity(Intent(this, HomeActivity::class.java))
                sharedLiveStreetViewPreferences!!.edit()
                    .putString("MAPBOX_LiveStreetView_APP_KEY", getKeyFromCounterCheck()).apply()
                updateCounterVarFromFirebase()
                finish()
            } else {
                Toast.makeText(this, "Please Checked box!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.readMoreBtn.setOnClickListener {
            val uri =
                Uri.parse("https://thrillingframestudio.blogspot.com/2021/10/policy-thrilling-flame-studio-built.html") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun fillMyList() {
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_1)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_2)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_3)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_4)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_5)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_6)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_7)
        listOfKeys.add(LiveStreetViewMyAppAds.mapbox_access_token_8)
    }

    @SuppressLint("LongLogTag")
    private fun getKeyFromCounterCheck(): String? {
        var myKey: String? = null
        when (LiveStreetViewMyAppAds.current_counter) {
            1.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_1
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 1.0)
            }
            2.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_2
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 2.0)
            }
            3.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_3
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 3.0)
            }
            4.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_4
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 4.0)
            }
            5.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_5
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 5.0)
            }
            6.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_6
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 6.0)
            }
            7.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_7
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 7.0)
            }
            8.0 -> {
                myKey = LiveStreetViewMyAppAds.mapbox_access_token_8
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + 8.0)
            }
            else -> {
                myKey = listOfKeys.get(Random.nextInt(listOfKeys.size))
                Log.d("getKeyFromCounterCheckTAG", "getKeyFromCounterCheck: " + myKey)

            }
        }
        return myKey
    }

    fun updateCounterVarFromFirebase() {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("GlobeQuize")
        if (LiveStreetViewMyAppAds.haveGotSnapshot) {
            if (LiveStreetViewMyAppAds.current_counter >= 30.0) {
                LiveStreetViewMyAppAds.current_counter = 1.0
            } else {
                LiveStreetViewMyAppAds.current_counter++
            }
            LiveStreetViewMyAppAds.haveGotSnapshot = false
            databaseReference.child("ADS_IDS")
                .child("current_counter").setValue(
                    LiveStreetViewMyAppAds.current_counter
                )
        }
    }


    override fun onBackPressed() {
        finish()
        finishAffinity()
    }

}