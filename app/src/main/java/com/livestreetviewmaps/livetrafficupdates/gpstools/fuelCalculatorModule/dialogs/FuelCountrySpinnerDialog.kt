package com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.FuelCountrySpinnerDialogBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.adapters.FuelCountryAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.callbacks.FuelCountryCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.callbacks.FuelCountrySpinnerCallbacks
import com.livestreetviewmaps.livetrafficupdates.gpstools.fuelCalculatorModule.models.FuelCalculatorModel

class FuelCountrySpinnerDialog(var mContext:Context,var callback:FuelCountrySpinnerCallbacks,var list:ArrayList<FuelCalculatorModel>):Dialog(mContext) {
    lateinit var binding:FuelCountrySpinnerDialogBinding
    var adapter:FuelCountryAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FuelCountrySpinnerDialogBinding.inflate(layoutInflater)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
        onClickListeners()
        setUpAdapter()

    }

    private fun setUpAdapter() {
        binding.countrySpinnerRecView.layoutManager=LinearLayoutManager(mContext)
        adapter= FuelCountryAdapter(mContext,object :FuelCountryCallback{
            override fun onCountryItemClick(model: FuelCalculatorModel) {
                callback.onSpinnerItemClicked(model)
                dismiss()
            }

        },list)
        binding.countrySpinnerRecView.adapter=adapter

    }

    private fun onClickListeners() {
        binding.dialogCloseBtn.setOnClickListener {
            try {
                dismiss()
            } catch (e: Exception) {
            }
        }

    }
}