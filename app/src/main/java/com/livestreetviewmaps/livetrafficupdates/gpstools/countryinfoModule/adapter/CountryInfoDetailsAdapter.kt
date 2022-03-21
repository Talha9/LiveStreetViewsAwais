package com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.helpers.CountryInfoDetailsHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.models.CountryInfoDetailsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.countryinfoModule.models.CountryInfoDetailsParamsModel
import java.text.DecimalFormat

class CountryInfoDetailsAdapter(var mContext:Context,var list:ArrayList<String>):
    RecyclerView.Adapter<CountryInfoDetailsAdapter.CountryinfoDetailsViewHolder>() {
    var paramList=ArrayList<CountryInfoDetailsParamsModel>()
    inner class CountryinfoDetailsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var name:TextView?=null

        init {
            name=itemView.findViewById(R.id.countryInfoDetailNameTxt)
            paramList=CountryInfoDetailsHelper.fillParamsNameList()
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryinfoDetailsViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_country_info_detail_item_design,parent,false)
        return CountryinfoDetailsViewHolder(v)
    }

    override fun onBindViewHolder(holder: CountryinfoDetailsViewHolder, position: Int) {
        val model=list[position]
        val paramModel=paramList[position]
            holder.name!!.text=paramModel.paramName+" : "+model +" "+paramModel.paramUnit.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}