package com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.callbacks.NearByCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.nearByModule.models.NearByModel


class NearByAdapter(var mContext: Context,var list:ArrayList<NearByModel>,var callback: NearByCallbacks):RecyclerView.Adapter<NearByAdapter.NearByViewHolder>() {
    inner class NearByViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout?=null
        var img: ImageView?=null
        var txt: TextView?=null
        init {
            btn=itemView.findViewById(R.id.nearByItemBg)
            img=itemView.findViewById(R.id.nearByItemIcon)
            txt=itemView.findViewById(R.id.nearByItemTxt)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByViewHolder {
        val v= LayoutInflater.from(mContext).inflate(R.layout.single_nearby_cat_item_design,parent,false)
        return NearByViewHolder(v)
    }
    override fun onBindViewHolder(holder: NearByViewHolder, position: Int) {
        val model=list.get(position)
        holder.txt!!.text=model.nearByCatName
        try {
            Glide.with(mContext).load(model.nearByCatIcon).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.btn!!.setOnClickListener {
            callback.onNearByCategoryClick(model)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

}