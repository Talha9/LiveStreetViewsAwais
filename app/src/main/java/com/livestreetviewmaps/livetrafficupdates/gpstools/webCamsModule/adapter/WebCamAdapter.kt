package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamsModel

class WebCamAdapter(var mContext: Context, var list:ArrayList<WebCamsModel>, var callback:WebCamCallback):
    RecyclerView.Adapter<WebCamAdapter.WebCamViewHolder>() {


    inner class WebCamViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var img:ImageView?=null
        var navigateBtn:CardView?=null
        var addressTxt:TextView?=null
        var addressSubTxt:TextView?=null
        var shareBtn:CardView?=null
        var mapViewBtn:CardView?=null
        var parentView:ConstraintLayout?=null
        var progress:ProgressBar?=null
        init {
            img=itemView.findViewById(R.id.webCamImg)
            navigateBtn=itemView.findViewById(R.id.webCamNavigationBtn)
            addressTxt=itemView.findViewById(R.id.webCamTxt)
            addressSubTxt=itemView.findViewById(R.id.webCamSubTxt)
            shareBtn=itemView.findViewById(R.id.webCamShareBtn)
            parentView=itemView.findViewById(R.id.parentLayout)
            mapViewBtn=itemView.findViewById(R.id.webCamMapViewBtn)
            progress=itemView.findViewById(R.id.webCamProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebCamViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_web_cam_item_design,parent,false)
        return WebCamViewHolder(v)
    }

    override fun onBindViewHolder(holder: WebCamViewHolder, position: Int) {
        val model=list[position]
        val bounceAnim = AnimationUtils.loadAnimation(mContext, R.anim.bonce)
        holder.parentView!!.startAnimation(bounceAnim)
        UtilsFunctionClass.setImageInGlideFromString(mContext,"http://img.youtube.com/vi/"+model.webCamUrl+"/0.jpg",holder.progress!!,holder.img!!)
        holder.addressTxt!!.text=model.placeName
        holder.addressSubTxt!!.text=model.cityName+","+model.countryName
        holder.shareBtn!!.setOnClickListener {
            callback.onShareWebCamClick(model)
        }
        holder.navigateBtn!!.setOnClickListener {
            callback.onNavigateWebCamClick(model)
        }
        holder.mapViewBtn!!.setOnClickListener {
            callback.onMapViewClick(model)
        }
        holder.img!!.setOnClickListener {
            callback.onThumbnailClick(model)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}