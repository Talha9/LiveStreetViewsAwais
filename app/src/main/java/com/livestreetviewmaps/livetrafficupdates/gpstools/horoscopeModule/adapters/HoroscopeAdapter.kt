package com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.callbacks.HoroscopeCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.horoscopeModule.models.HoroscopeMainModel

class HoroscopeAdapter(var mContext:Context, var list:ArrayList<HoroscopeMainModel>, var callback:HoroscopeCallbacks):
    RecyclerView.Adapter<HoroscopeAdapter.HoroscopeViewHolder>() {
    inner class HoroscopeViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var txt:TextView?=null
        var btn:ConstraintLayout?=null
        var img:ImageView?=null
        init {
            txt=itemView.findViewById(R.id.horoscopeItemTxt)
            btn=itemView.findViewById(R.id.horoscopeItemBg)
            img=itemView.findViewById(R.id.horoscopeItemIcon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_horoscope_item_design,parent,false)
        return HoroscopeViewHolder(v)
    }

    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val model=list[position]
        holder.txt!!.text=model.horoscopeName
        try {
            Glide.with(mContext).load(model.horoscopeIcon).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.btn!!.setOnClickListener {
            callback.onHoroscopeClick(model)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}