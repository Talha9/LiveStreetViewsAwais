package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks.onCarouselImageClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel

class DataAdapter (var mContext:Context,private var list : List<StreetViewModel>,var callback:onCarouselImageClickCallback): RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.sImage)
        val progress : ProgressBar = itemView.findViewById(R.id.streetProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent,false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model=list.get(position)
        UtilsFunctionClass.setImageInGlideFromString(mContext,model.imageLink,holder.progress,holder.image)
        holder.image.setOnClickListener {
            callback.onImageClick(model)
        }
    }
    fun updateData(list: List<StreetViewModel>) {
        this.list = list
        notifyDataSetChanged()
    }
}