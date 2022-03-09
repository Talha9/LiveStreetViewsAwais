package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.adapters

import android.content.Context
import android.graphics.Color
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
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks.onHomeItemClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.models.HomeMainSubCategoryModel

class HomeAdapter(
    var mContext: Context,
    var modelList: ArrayList<HomeMainSubCategoryModel>,
    var callback: onHomeItemClickCallback
) : RecyclerView.Adapter<HomeAdapter.HomeViewHodler>() {
    inner class HomeViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bg: ConstraintLayout? = null
        var txt: TextView? = null
        var icon: ImageView? = null

        init {
            bg = itemView.findViewById(R.id.homeItemBg)
            txt = itemView.findViewById(R.id.homeItemTxt)
            icon = itemView.findViewById(R.id.homeItemIcon)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAdapter.HomeViewHodler {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_home_item_design,parent,false)
        return HomeViewHodler(v)
    }

    override fun onBindViewHolder(
        holder: HomeAdapter.HomeViewHodler,
        position: Int
    ) {
        val model=modelList.get(position)
        holder.bg!!.background=mContext.getDrawable(model.itemBackground)
        holder.txt!!.text=model.itemSubCategoryName
        Log.d("onBindViewHolderTAG", "onBindViewHolder: "+model.itemColor)
        holder.txt!!.setTextColor(ContextCompat.getColor(mContext,model.itemColor))
        try {
            Glide.with(mContext).load(model.itemDrawable).into(holder.icon!!)
        } catch (e: Exception) {
        }
        holder.bg!!.setOnClickListener {
            callback.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
      return modelList.size
    }


}