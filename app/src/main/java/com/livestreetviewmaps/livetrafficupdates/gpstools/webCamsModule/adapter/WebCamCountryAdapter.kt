package com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.adapter

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.callbacks.WebCamCountryClickModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.webCamsModule.models.WebCamCountryModel

class WebCamCountryAdapter(
    var mContext: Context,
    var list: ArrayList<WebCamCountryModel>,
    var callback: WebCamCountryClickModel
) :
    RecyclerView.Adapter<WebCamCountryAdapter.WebCamCountryViewHolder>() {
    inner class WebCamCountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt: TextView? = null
        var img: ImageView? = null

        init {
            txt = itemView.findViewById(R.id.countryName)
            img = itemView.findViewById(R.id.countryFlag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebCamCountryViewHolder {
        val v = LayoutInflater.from(mContext)
            .inflate(R.layout.single_time_zone_country_item, parent, false)
        return WebCamCountryViewHolder(v)
    }

    override fun onBindViewHolder(holder: WebCamCountryViewHolder, position: Int) {
        val model = list[position]

        try {
            holder.txt!!.text = model.country
        } catch (e: Exception) {
        }

        try {
            val flage = "https://flagpedia.net/data/flags/normal/${model.code.toLowerCase()}.png"
            Log.i("flagezzz", ": " + flage)
            Glide.with(mContext).load(flage).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.itemView.setOnClickListener {
            callback.onCountryClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}