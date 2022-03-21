package com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.Toast
import com.livestreetviewmaps.livetrafficupdates.gpstools.ageCalculatorModule.callbacks.CalenderCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.CalenderDialogBinding

class CalenderDialog(var mContext: Context, var callback: CalenderCallback) : Dialog(mContext) {
    lateinit var binding: CalenderDialogBinding
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalenderDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        setContentView(binding.root)

        onClickListeners()

    }

    private fun onClickListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.calenderData.setOnDateChangedListener(object :DatePicker.OnDateChangedListener{
                override fun onDateChanged(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    mDay = dayOfMonth
                    mMonth = month + 1
                    mYear = year
                }

            })
        }else{
            Toast.makeText(mContext, "Your device version is low", Toast.LENGTH_LONG).show()
        }

        binding.okTxt.setOnClickListener {
            Log.d("okTxtTAG", "onClickListeners: "+mDay+","+mMonth+","+mYear)
            if (mDay != 0 && mMonth != 0 && mYear != 0) {
                callback.onOkPressed(mYear, mMonth, mDay)
                try {
                    dismiss()
                } catch (e: Exception) {
                }
            } else {
                Toast.makeText(mContext, "Please Select Date!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}