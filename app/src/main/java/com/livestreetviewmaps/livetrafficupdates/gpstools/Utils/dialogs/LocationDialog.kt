package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.callbacks.LocationDialogCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.LocationEnableDialogBinding

class LocationDialog(var mContext: Context,var callback:LocationDialogCallback):
Dialog(mContext){
    var binding:LocationEnableDialogBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LocationEnableDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding!!.root)


        binding!!.animationViewGPSEnable.setAnimation(R.raw.location_anim)

        binding!!.btn1.setOnClickListener {
            callback.onEnabledGPS()
        }

    }

}