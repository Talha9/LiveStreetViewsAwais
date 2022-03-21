package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.restCountriesApi.CountriesModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.callbacks.onStreetViewClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel

class StreetViewAdapter(
    var mContext:Context,
    var list:ArrayList<StreetViewModel>,
    var callback:onStreetViewClickCallback
):RecyclerView.Adapter<StreetViewAdapter.StreetViewHolder>(){
    public var btnCheck=true
    class StreetViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var img:ImageView?=null
        var street:TextView?=null
        var progress:ProgressBar?=null
        init {
            img=itemView.findViewById(R.id.streetViewImg)
            street=itemView.findViewById(R.id.streetTxt)
            progress=itemView.findViewById(R.id.streetViewProgress)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreetViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_street_view_design,parent,false)
        return StreetViewHolder(v)
    }
    override fun onBindViewHolder(holder: StreetViewHolder, position: Int) {
        val model=list.get(position)
        if(btnCheck){
            holder.street!!.text=model.cityName+","+model.countryName
            UtilsFunctionClass.setImageInGlideFromString(mContext,model.imageLink,holder.progress!!,holder.img!!)
        }else{
            holder.street!!.text=model.countryName
            UtilsFunctionClass.setImageInGlideFromString(mContext,model.imageLink,holder.progress!!,holder.img!!)
        }
        holder.img!!.setOnClickListener {
            callback.onClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun changeAdapteroList(arrayList: ArrayList<StreetViewModel>) {
        list=arrayList
        notifyDataSetChanged()

    }


}