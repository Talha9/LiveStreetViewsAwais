package com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.callbacks.RiversCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.riversModule.models.RiversModel


class RiversAdapter(var mContext: Context,var list:ArrayList<RiversModel>,var callback:RiversCallback):RecyclerView.Adapter<RiversAdapter.RiversViewHolder>() {
    inner class RiversViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout?=null
        var img: ImageView?=null
        var txt: TextView?=null
        var progress: ProgressBar?=null
        init {
            btn=itemView.findViewById(R.id.riverBtn)
            img=itemView.findViewById(R.id.riverImg)
            txt=itemView.findViewById(R.id.riverTxt)
            progress=itemView.findViewById(R.id.riverProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiversViewHolder {
        val v= LayoutInflater.from(mContext).inflate(R.layout.single_river_item_design,parent,false)
        return RiversViewHolder(v)    }

    override fun onBindViewHolder(holder: RiversViewHolder, position: Int) {
        val model=list.get(position)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.riverImg,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.riverName
        holder.btn!!.setOnClickListener {
            callback.onRiverClick(model)
        }    }

    override fun getItemCount(): Int {
        return list.size
    }

}