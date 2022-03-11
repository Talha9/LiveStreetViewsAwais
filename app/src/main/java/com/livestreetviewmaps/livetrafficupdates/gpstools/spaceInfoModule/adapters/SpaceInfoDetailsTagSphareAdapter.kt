package com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.spaceInfoModule.models.SpaceInfoMainModel
import com.moxun.tagcloudlib.view.TagsAdapter

class SpaceInfoDetailsTagSphareAdapter(var mContext:Context,var model: SpaceInfoMainModel):TagsAdapter() {
    override fun getCount(): Int {
        return 1
    }

    override fun getView(context: Context?, position: Int, parent: ViewGroup?): View {
        val v= LayoutInflater.from(mContext).inflate(R.layout.single_tag_planet_design,parent,false)
        v.findViewById<ImageView>(R.id.planetImg).setImageDrawable(mContext.getDrawable(model.planetImg))
        return v
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getPopularity(position: Int): Int {
        return position % 3
    }

    override fun onThemeColorChanged(view: View?, themeColor: Int) {

    }
}