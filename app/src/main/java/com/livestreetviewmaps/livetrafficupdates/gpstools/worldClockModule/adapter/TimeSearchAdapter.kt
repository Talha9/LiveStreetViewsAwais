package com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.callbacks.TimeSeachCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.worldClockModule.models.TimeZoneModel

class TimeSearchAdapter(
    var mContext: Context,
    var list: ArrayList<TimeZoneModel>,
    var callback: TimeSeachCallback
) :
    RecyclerView.Adapter<TimeSearchAdapter.TimeSearchViewHolder>() {


    inner class TimeSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt: TextView? = null
        var img: ImageView? = null

        init {
            txt = itemView.findViewById(R.id.countryName)
            img = itemView.findViewById(R.id.countryFlag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSearchViewHolder {
        val v = LayoutInflater.from(mContext)
            .inflate(R.layout.single_time_zone_country_item, parent, false)
        return TimeSearchViewHolder(v)
    }

    override fun onBindViewHolder(holder: TimeSearchViewHolder, position: Int) {
        val model = list[position]

        try {
            holder.txt!!.text = model.timezone
        } catch (e: Exception) {
        }

        try {
            val flage = "https://flagpedia.net/data/flags/normal/${model.country_code}.png"
            Log.i("flagezzz", ": " + flage)
            Glide.with(mContext).load(flage).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.itemView.setOnClickListener {
            callback.onTimeZoneClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setNewTimeZoneCountryList(filteredTimeZoneCountryList: ArrayList<TimeZoneModel>) {
        list = filteredTimeZoneCountryList
        notifyDataSetChanged()
    }
}