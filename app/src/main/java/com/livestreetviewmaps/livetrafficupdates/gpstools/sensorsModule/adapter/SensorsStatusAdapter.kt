package com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.sensorsModule.models.SensorsStatusModel

class SensorsStatusAdapter(var mContext: Context, var list: ArrayList<SensorsStatusModel>) :
    RecyclerView.Adapter<SensorsStatusAdapter.SensorsStatusViewHolder>() {


    inner class SensorsStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemValue: TextView? = null
        var itemName: TextView? = null

        init {
            itemValue = itemView.findViewById(R.id.StatusItem)
            itemName = itemView.findViewById(R.id.StatusItemName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorsStatusViewHolder {
        val v = LayoutInflater.from(mContext)
            .inflate(R.layout.single_sensor_status_item_style, parent, false)
        return SensorsStatusViewHolder(v)
    }

    override fun onBindViewHolder(holder: SensorsStatusViewHolder, position: Int) {
        val model = list[position]
        holder.itemName!!.text = model.name
        holder.itemValue!!.text = model.value
    }

    override fun getItemCount(): Int {
        return list.size
    }
}