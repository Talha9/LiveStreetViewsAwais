package com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.adapters

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
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.callbacks.FuelCountryCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.models.FuelCalculatorModel

class FuelCountryAdapter(
    var mContext: Context,
    var callback: FuelCountryCallback,
    var list: ArrayList<FuelCalculatorModel>
) : RecyclerView.Adapter<FuelCountryAdapter.FuelCountryViewHolder>() {

    inner class FuelCountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt: TextView? = null
        var price: TextView? = null
        var img: ImageView? = null
        var btn: ConstraintLayout? = null

        init {

            txt = itemView.findViewById(R.id.fuelDialogCountryName)
            price = itemView.findViewById(R.id.fuelDialogCountryPrice)
            img = itemView.findViewById(R.id.fuelDialogCountryFlag)
            btn = itemView.findViewById(R.id.fuelDialogSelectCountry)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelCountryViewHolder {
        val v =
            LayoutInflater.from(mContext).inflate(R.layout.single_fuel_country_item, parent, false)
        return FuelCountryViewHolder(v)
    }

    override fun onBindViewHolder(holder: FuelCountryViewHolder, position: Int) {
        val model = list[position]
        try {
            Glide.with(mContext).load(model.flagLink).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.txt!!.text = model.country_name
        holder.btn!!.setOnClickListener {
            callback.onCountryItemClick(model)
        }
        holder.price!!.text=model.rate

    }

    override fun getItemCount(): Int {
        return list.size

    }
}