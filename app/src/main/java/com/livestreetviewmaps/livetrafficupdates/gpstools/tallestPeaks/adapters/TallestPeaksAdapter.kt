package com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.adapters

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.callbacks.TallestPeaksCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.tallestPeaks.model.TallestPeakModel

class TallestPeaksAdapter(var mContext: Context, var list:ArrayList<TallestPeakModel>, var callback:TallestPeaksCallback)
    : RecyclerView.Adapter<TallestPeaksAdapter.TallestPeaksViewHolder>() {


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
        val v= LayoutInflater.from(mContext).inflate(R.layout.single_tallest_peaks_item_design,parent,false)
        return TallestPeaksViewHolder(v)
    }

    override fun onBindViewHolder(holder: TallestPeaksViewHolder, position: Int) {
        val model=list.get(position)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.tallestImg,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.tallestPeakName
        holder.btn!!.setOnClickListener {
            callback.onTallestPeakClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}