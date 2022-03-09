package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.HikingSavedCallback
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HikingSavedAdapter(var mContext: Context,var list:ArrayList<HikingTable>,var callback:HikingSavedCallback):
    RecyclerView.Adapter<HikingSavedAdapter.HikingSavedViewHolder>() {


   inner class HikingSavedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
       var img:ImageView?=null
       var progress:ProgressBar?=null
       var distanceTxt:TextView?=null
       var durationTxt:TextView?=null
       var dateTxt:TextView?=null
       var closeBtn:ImageView?=null
       var btn:ConstraintLayout?=null
       init {
           img=itemView.findViewById(R.id.hikingSavedeImg)
           progress=itemView.findViewById(R.id.hikingSavedProgress)
           distanceTxt=itemView.findViewById(R.id.hikikngSavedDistanceTxt)
           durationTxt=itemView.findViewById(R.id.hikikngSavedDurationTxt)
           dateTxt=itemView.findViewById(R.id.hikikngSavedDateTxt)
           btn=itemView.findViewById(R.id.hikingSavedBtn)
           closeBtn=itemView.findViewById(R.id.hikingSavedCloseBtn)

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HikingSavedViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_hiking_saved_design,parent,false)
        return HikingSavedViewHolder(v)
    }

    override fun onBindViewHolder(holder: HikingSavedViewHolder, position: Int) {
        val model=list[position]
        when(model.activityName){
            "Go Walking"->{
                UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(R.drawable.walking_icon)!!,holder.progress!!,holder.img!!)
            }
            "Go Hiking"->{
                UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(R.drawable.mountain_hiking)!!,holder.progress!!,holder.img!!)
            }
            "Go Running"->{
                UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(R.drawable.running_icon)!!,holder.progress!!,holder.img!!)
            }
            "Go Cycling"->{
                UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(R.drawable.bicycle_icon)!!,holder.progress!!,holder.img!!)
            }
            "Go Other Activity"->{
                UtilsFunctionClass.setImageInGlideFromDrawable(mContext,mContext.getDrawable(R.drawable.activity_icon)!!,holder.progress!!,holder.img!!)
            }
        }
        holder.closeBtn!!.setOnClickListener {
            callback.onDeleteHikingBtnClick(model,position)
        }
        holder.durationTxt!!.text=model.totalDuration
        holder.distanceTxt!!.text= DecimalFormat("#.#").format(model.totalDistance) + " Km."
        holder.dateTxt!!.text= model.currentDate
        holder.btn!!.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}