package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.webCamApi.Webcam
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCallback
import gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads.LiveStreetViewMyAppNativeAds

class WebCamAdapter(var mContext: Context, var list: ArrayList<Webcam>?, var callback:WebCamCallback):
    RecyclerView.Adapter<WebCamAdapter.WebCamViewHolder>() {
    var typeAds = 0
    var typePost = 1
    var empty = 2

    inner class WebCamViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var img:ImageView?=null
        var addressTxt:TextView?=null
        var parentView:ConstraintLayout?=null
        var progress:ProgressBar?=null
        init {
            img=itemView.findViewById(R.id.webCamImg)
            addressTxt=itemView.findViewById(R.id.webCamTxt)
            parentView=itemView.findViewById(R.id.parentLayout)
            progress=itemView.findViewById(R.id.webCamProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebCamViewHolder {

        var v: View? = null
        if (viewType == typePost) {
            v=LayoutInflater.from(mContext).inflate(R.layout.single_web_cam_item_design,parent,false)
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

        return WebCamViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: WebCamViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 6 == 0) return
        val newPosition = position - (position / 6 + 1)
        val model= list?.get(newPosition)
        val bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bonce)
        holder.parentView!!.startAnimation(bounceAnim)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model!!.image.current.preview,holder.progress!!,holder.img!!)
        holder.addressTxt!!.text= model.title

        holder.img!!.setOnClickListener {
            callback.onThumbnailClick(model)
        }


    }

    override fun getItemCount(): Int {
        val itemCount = list!!.size
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
}