package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks.WondersCallback
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds

class WonderAdapter(
    var mContext: Context,
    var list: ArrayList<WondersModel>,
    var callback: WondersCallback
) :
    RecyclerView.Adapter<WonderAdapter.WonderViewHolder>() {
    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class WonderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout? = null
        var img: ImageView? = null
        var txt: TextView? = null
        var progress: ProgressBar? = null


        init {
            btn = itemView.findViewById(R.id.wonderBtn)
            img = itemView.findViewById(R.id.wonderImg)
            txt = itemView.findViewById(R.id.wonderTxt)
            progress = itemView.findViewById(R.id.wonderProgress)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WonderViewHolder {


        var v: View? = null
        if (viewType == typePost) {
            v =
                LayoutInflater.from(mContext)
                    .inflate(R.layout.single_wonder_item_design, parent, false)
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
        return WonderViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: WonderViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 5 == 0) return
        val newPosition = position - (position / 5 + 1)
        val model = list.get(newPosition)

        UtilsFunctionClass.setImageInGlideFromString(
            mContext,
            model.wonderImg,
            holder.progress!!,
            holder.img!!
        )
        holder.txt!!.text = model.wonderName
        holder.btn!!.setOnClickListener {
            callback.onWonderClick(model)
        }

    }

    override fun getItemCount(): Int {
        val itemCount = list.size
        return itemCount + (itemCount / 4 + 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            empty
        } else {
            if (position % 5 == 0) {
                typeAds
            } else {
                typePost
            }
        }
    }
}