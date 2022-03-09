package com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.callbacks.OceansCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.oceansModule.models.OceansModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.Model.WondersModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.wondersModule.callbacks.WondersCallback

class OceansAdapter(var mContext: Context, var list: ArrayList<OceansModel>, var callback: OceansCallback):
    RecyclerView.Adapter<OceansAdapter.OceansViewHolder>() {

    inner class OceansViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var btn:ConstraintLayout?=null
        var img:ImageView?=null
        var txt:TextView?=null
        var progress:ProgressBar?=null
        init {
            btn=itemView.findViewById(R.id.oceanBtn)
            img=itemView.findViewById(R.id.oceanImg)
            txt=itemView.findViewById(R.id.oceanTxt)
            progress=itemView.findViewById(R.id.oceanProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OceansViewHolder {
        val v=LayoutInflater.from(mContext).inflate(R.layout.single_oceans_item_design,parent,false)
        return OceansViewHolder(v)
    }

    override fun onBindViewHolder(holder: OceansViewHolder, position: Int) {
        val model=list.get(position)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.oceansImg,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.oceansName
        holder.btn!!.setOnClickListener {
            callback.onOceansClick(model)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}