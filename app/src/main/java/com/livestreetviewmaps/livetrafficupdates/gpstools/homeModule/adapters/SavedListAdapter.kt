package com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.FavouritesTable
import com.livestreetviewmaps.livetrafficupdates.gpstools.homeModule.callbacks.FavouriteSavedCallback

class SavedListAdapter(
    var mContext: Context,
    var list: ArrayList<FavouritesTable>,
    var callback: FavouriteSavedCallback
) :
    RecyclerView.Adapter<SavedListAdapter.SavedListViewHolder>() {

    inner class SavedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img: ImageView? = null
        var deleteBtn: ImageView? = null
        var txt: TextView? = null

        init {

            img = itemView.findViewById(R.id.favItemImg)
            deleteBtn = itemView.findViewById(R.id.deleteStreetView)
            txt = itemView.findViewById(R.id.favItemTxt)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListViewHolder {
        val v = LayoutInflater.from(mContext)
            .inflate(R.layout.single_saved_favourite_item, parent, false)
        return SavedListViewHolder(v)
    }

    override fun onBindViewHolder(holder: SavedListViewHolder, position: Int) {
        val model = list[position]
        try {
            Glide.with(mContext).load(model.streetViewLink).into(holder.img!!)
        } catch (e: Exception) {
        }
        holder.txt!!.text = model.streetViewName
        holder.txt!!.setOnClickListener {
            callback.onSavedFavouriteClick(model)
        }
        holder.deleteBtn!!.setOnClickListener {
            callback.onSavedFavouriteDeleteClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateRecycleList(dataList: ArrayList<FavouritesTable>) {
        list = dataList
        notifyDataSetChanged()
    }


}