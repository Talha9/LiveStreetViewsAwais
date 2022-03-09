package com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.InternetDialogBinding


class InternetDialog(var mContext:Context):Dialog(mContext) {
    var binding:InternetDialogBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= InternetDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding!!.root)

        binding!!.animationViewInternetEnable.setAnimation(R.raw.internet_anim)
    }
}