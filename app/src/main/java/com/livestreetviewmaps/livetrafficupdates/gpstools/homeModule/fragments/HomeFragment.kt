package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.activities.DesertsMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.FragmentHomeBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.earthMapModule.EarthMapMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.activities.HikingMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.adapters.DataAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.adapters.HomeAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks.onCarouselImageClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks.onHomeItemClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.helpers.HomeItemsHelpers
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models.HomeMainCategoryModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models.HomeMainSubCategoryModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.activities.HoroscopeMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.myLocationModule.activities.MyLocationMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.navigationModule.activities.NavigationMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.activities.NearbyMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.activities.OceansMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.activities.RiversMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.activities.SpaceInfoMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.stepCounterModule.activities.StepCounterMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.activities.StreetViewActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.helpers.StreetViewItemsHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.activities.TallestPeaksMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.activities.WebCamsMainActivity
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.activities.WondersMainActivity

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    var mContext: Context? = null
    var adapter: HomeAdapter? = null
    var subList: ArrayList<HomeMainSubCategoryModel>? = null
    var mainList: ArrayList<HomeMainCategoryModel>? = null
    var streetList: ArrayList<StreetViewModel>? = null
    var manager: GridLayoutManager? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mContext = context
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        SetUpBottomNavigationBar()
        listFillers()
        Initializers()
        OnClickListeners()
        setUpCarousel()
        return binding!!.root
    }

    private fun setUpCarousel() {

        val adapter = DataAdapter(mContext!!, streetList!!, object : onCarouselImageClickCallback {
            override fun onImageClick(model: StreetViewModel) {
                val intent = Intent(mContext, StreetViewActivity::class.java)
                Log.d(
                    "ModelLogCheckTAG",
                    "onItemClick: " + model.viewName + "," + model.countryName + "," + model.cityName + "," + model.imageLink + "," + model.details
                )
                intent.putExtra("streetModel", model)
                startActivity(intent)
            }

        })
        binding!!.streetViewRecView.adapter = adapter
        binding!!.streetViewRecView.set3DItem(true)
        binding!!.streetViewRecView.setAlpha(true)

        val carouselLayoutManager = binding!!.streetViewRecView.getCarouselLayoutManager()
        carouselLayoutManager.scrollToPosition(10)
    }

    private fun listFillers() {
        mainList = HomeItemsHelpers.FillHomeitemsList()
        streetList = StreetViewItemsHelper.fillStreetCityViews()
    }

    private fun setUpHomeAdapter(list: ArrayList<HomeMainSubCategoryModel>, s: String) {
        adapter = HomeAdapter(mContext!!, list, object : onHomeItemClickCallback {
            override fun onItemClick(pos: Int) {
                Log.d("setUpHomeAdapterTAG", "onItemClick: "+s)
                when (s) {
                    "home"->{
                        when (pos) {
                            0 -> {
                                val intent = Intent(mContext, StreetViewActivity::class.java)
                                streetList!!.shuffle()
                                val m = streetList!!.get(2)
                                Log.d(
                                    "ModelLogCheckTAG",
                                    "onItemClick: " + m.viewName + "," + m.countryName + "," + m.cityName + "," + m.imageLink + "," + m.details
                                )
                                constants.bottomIndex=3
                                intent.putExtra("streetModel", m)
                                startActivity(intent)
                            }
                            1 -> {
                                val intent = Intent(mContext, NearbyMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            2 -> {
                                val intent = Intent(mContext, WondersMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            3 -> {
                                val intent = Intent(mContext, OceansMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            4 -> {
                                val intent = Intent(mContext, TallestPeaksMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            5 -> {
                                val intent = Intent(mContext, DesertsMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            6 -> {
                                val intent = Intent(mContext, RiversMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                            7 -> {
                                val intent = Intent(mContext, WebCamsMainActivity::class.java)
                                constants.bottomIndex=3
                                startActivity(intent)
                            }
                        }
                    }
                    "navigation"->{
                        when (pos) {
                                0 -> {
                                val intent = Intent(mContext, NavigationMainActivity::class.java)
                                    constants.bottomIndex=2
                                startActivity(intent)
                            }
                            1 -> {
                                val intent = Intent(mContext, MyLocationMainActivity::class.java)
                                constants.bottomIndex=2
                                startActivity(intent)
                            }
                            2 -> {
                                val intent = Intent(mContext, EarthMapMainActivity::class.java)
                                constants.bottomIndex=2
                                startActivity(intent)
                            }
                            3 -> {
                                val intent = Intent(mContext, HoroscopeMainActivity::class.java)
                                constants.bottomIndex=2
                                startActivity(intent)
                            }
                            4 -> {
                                val intent = Intent(mContext, HikingMainActivity::class.java)
                                constants.bottomIndex=2
                                startActivity(intent)
                            }
                            5 -> {
                                val intent = Intent(mContext, SpaceInfoMainActivity::class.java)
                                constants.bottomIndex=2
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        })
        binding!!.homeRecView.adapter = adapter
    }

    private fun Initializers() {
            manager = GridLayoutManager(mContext, 2)
            binding!!.homeRecView.layoutManager = manager
            subList = mainList!!.get(2).list
            setUpHomeAdapter(subList!!, "home")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun OnClickListeners() {
        binding!!.bottom.bottomNav.setOnClickMenuListener {
            when (it.id) {
                1 -> {
                    binding!!.bottom.bottomNav.circleColor =
                        getColor(mContext!!, R.color.toolsThemeColor)
                    subList = mainList!!.get(0).list
                    setUpHomeAdapter(subList!!,"tools")
                    binding!!.bottom.linearLayoutCompatTools.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.toolsThemeColor
                        )
                    )
                    binding!!.bottom.linearLayoutCompatHome.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatNavigation.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatSettings.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatWishlist.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                }
                2 -> {
                    binding!!.bottom.bottomNav.circleColor =
                        getColor(mContext!!, R.color.navigationThemeColor)
                    subList = mainList!!.get(1).list
                    setUpHomeAdapter(subList!!, "navigation")
                    binding!!.bottom.linearLayoutCompatNavigation.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.navigationThemeColor
                        )
                    )
                    binding!!.bottom.linearLayoutCompatHome.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatTools.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatSettings.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatWishlist.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                }
                3 -> {
                    binding!!.bottom.bottomNav.circleColor =
                        getColor(mContext!!, R.color.homeThemeColor)
                    subList = mainList!!.get(2).list
                    setUpHomeAdapter(subList!!, "home")
                    binding!!.bottom.linearLayoutCompatHome.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.homeThemeColor
                        )
                    )
                    binding!!.bottom.linearLayoutCompatTools.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatNavigation.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatSettings.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatWishlist.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                }
                4 -> {
                    binding!!.bottom.bottomNav.circleColor =
                        getColor(mContext!!, R.color.savedThemeColor)
                    binding!!.bottom.linearLayoutCompatWishlist.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.savedThemeColor
                        )
                    )
                    binding!!.bottom.linearLayoutCompatHome.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatNavigation.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatSettings.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatTools.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                }
                5 -> {
                    binding!!.bottom.bottomNav.circleColor =
                        getColor(mContext!!, R.color.settingsThemeColor)
                    binding!!.bottom.linearLayoutCompatSettings.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.settingsThemeColor
                        )
                    )
                    binding!!.bottom.linearLayoutCompatHome.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatNavigation.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatTools.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                    binding!!.bottom.linearLayoutCompatWishlist.setTextColor(
                        getColor(
                            mContext!!,
                            R.color.white
                        )
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun SetUpBottomNavigationBar() {
        binding!!.bottom.bottomNav.add(
            MeowBottomNavigation.Model(
                1,
                R.drawable.earth_globe
            )
        )
        binding!!.bottom.bottomNav.add(
            MeowBottomNavigation.Model(
                2,
                R.drawable.navigation_icon_1
            )
        )
        binding!!.bottom.bottomNav.add(
            MeowBottomNavigation.Model(
                3,
                R.drawable.home_page
            )
        )
        binding!!.bottom.bottomNav.add(
            MeowBottomNavigation.Model(
                4,
                R.drawable.favourite_saved
            )
        )
        binding!!.bottom.bottomNav.add(
            MeowBottomNavigation.Model(
                5,
                R.drawable.setting_icon
            )
        )
        binding!!.bottom.bottomNav.show(constants.bottomIndex)
        binding!!.bottom.bottomNav.circleColor = getColor(mContext!!, R.color.homeThemeColor)
    }


}