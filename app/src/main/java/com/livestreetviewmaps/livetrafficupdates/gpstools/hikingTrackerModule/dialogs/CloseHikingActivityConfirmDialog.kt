package com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.db.models.HikingTable
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.CloseHikingActivityConfirmDialogBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.hikingTrackerModule.callbacks.CloseHikingActivityCallback

class CloseHikingActivityConfirmDialog(var mContext:Context,var callback:CloseHikingActivityCallback):Dialog(mContext) {
    lateinit var binding:CloseHikingActivityConfirmDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= CloseHikingActivityConfirmDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)

        binding.btn1.setOnClickListener {
            callback.onYesClick()
        }
        binding.btn2.setOnClickListener {
           callback.onNoClick()
        }
    }
}