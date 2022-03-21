package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.callbacks.RestCountriesMainCallbacks
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds

class CountriesInfoMainAdapter(
    var mContext: Context,
    var list: ArrayList<CountriesModel>,
    var callback: RestCountriesMainCallbacks
) :
    RecyclerView.Adapter<CountriesInfoMainAdapter.CountriesInfoMainViewModel>() {
    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class CountriesInfoMainViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryFlgImg: ImageView? = null
        var countryTxt: TextView? = null

        init {
            countryFlgImg = itemView.findViewById(R.id.countryFlgImg)
            countryTxt = itemView.findViewById(R.id.countryTxt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesInfoMainViewModel {
        var v: View? = null
        if (viewType == typePost) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.single_country_info_main_item_design, parent, false)
        } else if (viewType == typeAds) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.live_streat_view_nav_native_layout_native_ads, parent, false)
            LiveStreetViewMyAppNativeAds.loadLiveStreetViewAdmobNativeAdPriority(
                mContext as Activity,
                v as FrameLayout
            )
        } else if (viewType == empty) {
            v = LayoutInflater.from(mContext).inflate(R.layout.empty_layout, parent, false)
        }


        return CountriesInfoMainViewModel(v!!)
    }

    override fun onBindViewHolder(holder: CountriesInfoMainViewModel, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 6 == 0) return
        val newPosition = position - (position / 6 + 1)
        val model = list[newPosition]
        try {
            val flage =
                "https://flagpedia.net/data/flags/normal/${model.alpha2Code.toLowerCase()}.png"
            Log.i("flagezzz", ": " + flage)
            Glide.with(mContext).load(flage).into(holder.countryFlgImg!!)
        } catch (e: Exception) {
        }
        holder.countryTxt!!.text = model.name
        holder.countryTxt!!.setOnClickListener {
            callback.onCountryClick(model)
        }

    }

    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 5 + 1)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 6 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }


    fun setCountryInfoList(countryInfoListFiltered: ArrayList<CountriesModel>) {
        list = countryInfoListFiltered
        notifyDataSetChanged()
    }
}