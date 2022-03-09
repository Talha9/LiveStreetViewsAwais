package com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.adapters

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks.WondersCallback

class WonderAdapter(
    var mContext: Context,
    var list: ArrayList<WondersModel>,
    var callback: WondersCallback
) :
    RecyclerView.Adapter<WonderAdapter.WonderViewHolder>() {

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
        val v =
            LayoutInflater.from(mContext).inflate(R.layout.single_wonder_item_design, parent, false)
        return WonderViewHolder(v)
    }

    override fun onBindViewHolder(holder: WonderViewHolder, position: Int) {
        val model = list.get(position)

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
        return list.size
    }
}