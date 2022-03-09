package com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.callbacks.DesertsCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.desertsModule.models.DesertsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass

class DesertsAdapter(var mContext: Context, var list:ArrayList<DesertsModel>, var callback:DesertsCallback):RecyclerView.Adapter<DesertsAdapter.DesertViewHolder>(){
    inner class DesertViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var btn: ConstraintLayout?=null
        var img: ImageView?=null
        var txt: TextView?=null
        var progress: ProgressBar?=null
        init {
            btn=itemView.findViewById(R.id.desertBtn)
            img=itemView.findViewById(R.id.desertImg)
            txt=itemView.findViewById(R.id.desertTxt)
            progress=itemView.findViewById(R.id.desertProgress)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesertViewHolder {
        val v= LayoutInflater.from(mContext).inflate(R.layout.single_deserts_item_design,parent,false)
        return DesertViewHolder(v)
    }

    override fun onBindViewHolder(holder: DesertViewHolder, position: Int) {
        val model=list.get(position)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.desertsImg,holder.progress!!,holder.img!!)
        holder.txt!!.text=model.desertsName
        holder.btn!!.setOnClickListener {
            callback.onDesertClick(model)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

}