package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityHikingMainBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.fragments.WorkoutHomeFragment
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.fragments.WorkoutSavedFragment
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.activities.HomeActivity

class HikingMainActivity : AppCompatActivity() {
    var binding: ActivityHikingMainBinding? = null
    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHikingMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initializers()
        onClickListeners()


    }

    private fun initializers() {
        binding!!.hikingHomeBtn.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.navigation_item_click_design
            )
        )
        binding!!.hikingSaveBtn.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.btn_background_theme_color
            )
        )
        binding!!.hikingHomeBtn.setTextColor(getColor(R.color.ThemeColor))
        binding!!.hikingSaveBtn.setTextColor(getColor(R.color.white))
        fragmentChecker("home")
    }


    private fun onClickListeners() {
        binding!!.hikingHomeBtn.setOnClickListener {
            it.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.navigation_item_click_design
                )
            )
            binding!!.hikingSaveBtn.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.btn_background_theme_color
                )
            )
            binding!!.hikingHomeBtn.setTextColor(getColor(R.color.ThemeColor))
            binding!!.hikingSaveBtn.setTextColor(getColor(R.color.white))
            fragmentChecker("home")
        }
        binding!!.hikingSaveBtn.setOnClickListener {
            it.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.navigation_item_click_design
                )
            )
            binding!!.hikingHomeBtn.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.btn_background_theme_color
                )
            )
            binding!!.hikingSaveBtn.setTextColor(getColor(R.color.ThemeColor))
            binding!!.hikingHomeBtn.setTextColor(getColor(R.color.white))
            fragmentChecker("save")
        }
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }


    }


    private fun fragmentChecker(whichFragment: String) {
        when (whichFragment) {
            "home" -> {
                fragment = WorkoutHomeFragment()
                loadFragment(fragment!!)

            }
            "save" -> {
                fragment = WorkoutSavedFragment()
                loadFragment(fragment!!)

            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        try {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_down
            )
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()


        } catch (e: Exception) {
        }
    }

    override fun onBackPressed() {
        finish()
    }
}