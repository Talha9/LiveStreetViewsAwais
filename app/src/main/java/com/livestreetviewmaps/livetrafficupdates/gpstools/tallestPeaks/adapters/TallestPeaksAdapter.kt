package com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.adapters

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.callbacks.TallestPeaksCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.model.TallestPeakModel
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds

class TallestPeaksAdapter(var mContext: Context, var list:ArrayList<TallestPeakModel>, var callback:TallestPeaksCallback)
    : RecyclerView.Adapter<TallestPeaksAdapter.TallestPeaksViewHolder>() {
    var typeAds = 0
    var typePost = 1
    var empty = 2

   inner class TallestPeaksViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
       var btn: ConstraintLayout?=null
       var img: ImageView?=null
       var txt: TextView?=null
       var progress: ProgressBar?=null
       init {
           btn=itemView.findViewById(R.id.tallestPeaksBtn)
           img=itemView.findViewById(R.id.tallestPeaksImg)
           txt=itemView.findViewById(R.id.tallestPeaksTxt)
           progress=itemView.findViewById(R.id.tallestPeaksProgress)
       }


   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallestPeaksViewHolder {
        var v: View? = null
        if (viewType == typePost) {
            v= LayoutInflater.from(mContext).inflate(R.layout.single_tallest_peaks_item_design,parent,false)
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

        return TallestPeaksViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: TallestPeaksViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 5 == 0) return
        val newPosition = position - (position / 5 + 1)
        val model=list.get(newPosition)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.tallestImg,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.tallestPeakName
        holder.btn!!.setOnClickListener {
            callback.onTallestPeakClick(model)
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