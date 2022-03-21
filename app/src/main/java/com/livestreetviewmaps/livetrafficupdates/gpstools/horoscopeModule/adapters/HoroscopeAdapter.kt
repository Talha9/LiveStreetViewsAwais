package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.callbacks.HoroscopeCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models.HoroscopeMainModel
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds

class HoroscopeAdapter(
    var mContext: Context,
    var list: ArrayList<HoroscopeMainModel>,
    var callback: HoroscopeCallbacks
) :
    RecyclerView.Adapter<HoroscopeAdapter.HoroscopeViewHolder>() {

    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class HoroscopeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt: TextView? = null
        var btn: ConstraintLayout? = null
        var img: ImageView? = null

        init {
            txt = itemView.findViewById(R.id.horoscopeItemTxt)
            btn = itemView.findViewById(R.id.horoscopeItemBg)
            img = itemView.findViewById(R.id.horoscopeItemIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {

        var v: View? = null
        if (viewType == typePost) {
            v = LayoutInflater.from(mContext)
                .inflate(R.layout.single_horoscope_item_design, parent, false)
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

        return HoroscopeViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 7 == 0) return
        val newPosition = position - (position / 7 + 1)
        val model = list[newPosition]
        holder.txt!!.text = model.horoscopeName
        try {
            Glide.with(mContext).load(model.horoscopeIcon).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.btn!!.setOnClickListener {
            callback.onHoroscopeClick(model)
        }
    }

    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 6 + 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 7 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }
}