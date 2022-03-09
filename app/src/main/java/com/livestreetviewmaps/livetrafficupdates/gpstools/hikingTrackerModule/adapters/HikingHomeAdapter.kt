package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.HikingHomeCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.models.HikingHomeModel

class HikingHomeAdapter(
    var mContext: Context,
    var list: ArrayList<HikingHomeModel>,
    var callback: HikingHomeCallback
) : RecyclerView.Adapter<HikingHomeAdapter.HikingHomeViewHolder>() {


    inner class HikingHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var img: ImageView? = null
        var btn: ConstraintLayout? = null
        var progress: ProgressBar? = null

        init {
            name = itemView.findViewById(R.id.hikikngHomeTxt)
            img = itemView.findViewById(R.id.hikingHomeImg)
            btn = itemView.findViewById(R.id.hikingHomeBtn)
            progress = itemView.findViewById(R.id.hikingHomeProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HikingHomeViewHolder {
        val v =
            LayoutInflater.from(mContext).inflate(R.layout.single_hiking_home_design, parent, false)
        return HikingHomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HikingHomeViewHolder, position: Int) {
        val model=list.get(position)
        holder.name!!.text=model.workoutName
        UtilsFunctionClass.setImageInGlideFromDrawable(mContext, getDrawable(mContext,model.workoutImg)!!, holder.progress!!, holder.img!!)
        holder.btn!!.setOnClickListener {
            callback.onWorkoutClick(model,position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}