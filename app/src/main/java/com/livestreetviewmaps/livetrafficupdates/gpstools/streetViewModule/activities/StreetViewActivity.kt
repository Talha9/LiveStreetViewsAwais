package com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.UtilsFunctionClass
import com.livestreetviewmaps.livetrafficupdates.gpstools.databinding.ActivityStreetViewBinding
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.adapter.StreetViewAdapter
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.callbacks.onStreetViewClickCallback
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.helpers.StreetViewItemsHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.models.StreetViewModel
import android.os.Environment
import android.util.Log
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import androidx.core.content.FileProvider

import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.constants
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.database.MyStreetsModel
import com.livestreetviewmaps.livetrafficupdates.gpstools.streetViewModule.database.StreetViewDatabaseInstance
import java.io.File


class StreetViewActivity : AppCompatActivity() {
    var binding: ActivityStreetViewBinding? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    var isSearching = false
    var currentImageModel:StreetViewModel?=null
    var currentImagePath:Uri?=null
    lateinit var downloadManager:DownloadManager
    private var lastDownload = -1L
    var manager: LinearLayoutManager? = null
    var listCity: ArrayList<StreetViewModel>? = null
    var listCountry: ArrayList<StreetViewModel>? = null
    var list: ArrayList<StreetViewModel>? = null
    var adapter: StreetViewAdapter? = null
    var database:StreetViewDatabaseInstance?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (intent.getParcelableExtra<StreetViewModel>("streetModel") != null) {
            try {
                val model = intent.getParcelableExtra<StreetViewModel>("streetModel")

                if (model != null) {
                    UtilsFunctionClass.setImageInGlideFromString(
                        this@StreetViewActivity,
                        model.imageLink,
                        binding!!.progressMainBg,
                        binding!!.StreetViewBg
                    )
                    Log.d("ModelLogCheckTAG", "onItemClick: "+ model.viewName+","+model.countryName+","+model.cityName+","+model.imageLink+","+model.details)
                    currentImageModel=model
                    binding!!.StreetViewNameTxt.text = currentImageModel!!.viewName
                    binding!!.StreetViewAddressTxt.text = currentImageModel!!.details + "\n" + currentImageModel!!.cityName + "," + currentImageModel!!.countryName
                }
            } catch (e: Exception) {
            }
        }

        listFiller()
        initializer()
        setUpAdapter()
        onClickListeners()
        setUpHeaderBar()
        setUpBottomSheet()

    }

    private fun initializer() {
       downloadManager =getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        registerReceiver(onNotificationClick, IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED))
        database=StreetViewDatabaseInstance.getDatabaseInstance(this)
    }

    private fun listFiller() {
        listCity = StreetViewItemsHelper.fillStreetCityViews()
        listCountry = StreetViewItemsHelper.fillStreetCountryViews()
        list = listCity
    }

    private fun setUpAdapter() {
        manager = LinearLayoutManager(this)
        binding!!.bSheet.bottmSheetRecView.layoutManager = manager
        adapter = StreetViewAdapter(this, list!!, object : onStreetViewClickCallback {
            override fun onClick(model: StreetViewModel) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                UtilsFunctionClass.setImageInGlideFromString(
                    this@StreetViewActivity,
                    model.imageLink,
                    binding!!.progressMainBg,
                    binding!!.StreetViewBg
                )
                currentImageModel=model
                binding!!.StreetViewNameTxt.text = currentImageModel!!.viewName
                if (adapter!!.btnCheck) {
                    binding!!.StreetViewAddressTxt.text =
                        currentImageModel!!.details + "\n" + currentImageModel!!.cityName + "," + currentImageModel!!.countryName
                } else {
                    binding!!.StreetViewAddressTxt.text = currentImageModel!!.details + "\n" + currentImageModel!!.countryName
                }
            }
        })
        binding!!.bSheet.bottmSheetRecView.adapter = adapter
    }

    private fun onClickListeners() {
        binding!!.bottmSheetBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding!!.bSheet.citiesBtn.setOnClickListener {
            binding!!.bSheet.citiesBtn.background =
                getDrawable(R.drawable.rounded_shape_theme_color)
            binding!!.bSheet.countriesBtn.background = getDrawable(R.drawable.rounded_shape_white)
            binding!!.bSheet.countriesBtn.setTextColor(resources.getColor(R.color.ThemeColor))
            binding!!.bSheet.citiesBtn.setTextColor(resources.getColor(R.color.white))
            list = listCity
            adapter!!.btnCheck = true
            adapter!!.notifyDataSetChanged()
        }

        binding!!.downloadBtn.setOnClickListener {
            if (constants.storageModule) {
                constants.startDownloading=1
                binding!!.progressMainBg.visibility=View.VISIBLE
                startDownload(currentImageModel!!.imageLink,currentImageModel!!.viewName)
                queryStatus()
            }else{
                UtilsFunctionClass.askForPermissions(this)
            }
        }

        binding!!.shareBtn.setOnClickListener {
            if (constants.storageModule) {
                constants.startDownloading=2
                binding!!.progressMainBg.visibility=View.VISIBLE
                shareImage(currentImageModel!!.imageLink,currentImageModel!!.viewName)
            }else{
                UtilsFunctionClass.askForPermissions(this)
            }
        }
        binding!!.favouriteBtn.setOnClickListener {
            if (constants.storageModule) {
                constants.startDownloading=3
                binding!!.progressMainBg.visibility=View.VISIBLE
                startDownload(currentImageModel!!.imageLink,currentImageModel!!.viewName)
            }else{
                UtilsFunctionClass.askForPermissions(this)
            }
        }

        binding!!.bSheet.countriesBtn.setOnClickListener {
            binding!!.bSheet.countriesBtn.background =
                getDrawable(R.drawable.rounded_shape_theme_color)
            binding!!.bSheet.citiesBtn.background = getDrawable(R.drawable.rounded_shape_white)
            binding!!.bSheet.countriesBtn.setTextColor(resources.getColor(R.color.white))
            binding!!.bSheet.citiesBtn.setTextColor(resources.getColor(R.color.ThemeColor))
            list = listCountry
            adapter!!.btnCheck = false
            adapter!!.notifyDataSetChanged()
        }

    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.bSheet.bottomSheetDrawer)
    }


    private fun setUpHeaderBar() {
        binding!!.header.headerBarBackBtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.header.headerBarTitleTxt.text = "Street View"
    }

    fun startDownload(Download_Str: String, currentImg: String) {
       val fileUri=Uri.parse(Download_Str)
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .mkdirs()
        lastDownload = downloadManager.enqueue(
            DownloadManager.Request(fileUri)
                .setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or
                            DownloadManager.Request.NETWORK_MOBILE
                )
                .setAllowedOverRoaming(false)
                .setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,currentImg+".png"
                )
        )

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            currentImg+".png"
        )
        currentImagePath=FileProvider.getUriForFile(
            this@StreetViewActivity,
            getApplicationContext()
                .getPackageName() + ".provider", file)
    }

    @SuppressLint("Range")
    fun queryStatus() {
        val c: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(lastDownload))
        if (c == null) {
            Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show()
        } else {
            c.moveToFirst()
            Log.d(
                "checkDownloadingStatus", "COLUMN_ID: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID))
            )
            Log.d(
                "checkDownloadingStatus", "COLUMN_BYTES_DOWNLOADED_SO_FAR: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            )
            Log.d(
                "checkDownloadingStatus", "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP))
            )
            Log.d(
                "checkDownloadingStatus", "COLUMN_LOCAL_URI: " +
                        c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            )
            Log.d(
                "checkDownloadingStatus", "COLUMN_STATUS: " +
                        c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            )
            Log.d(
                "checkDownloadingStatus", "COLUMN_REASON: " +
                        c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON))
            )
            statusMessage(c)
        }
    }

    @SuppressLint("Range")
    private fun statusMessage(c: Cursor): String {
        var msg = "???"
        msg =
            when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_FAILED -> "Download failed!"
                DownloadManager.STATUS_PAUSED -> "Download paused!"
                DownloadManager.STATUS_PENDING -> "Download pending!"
                DownloadManager.STATUS_RUNNING -> "Download in progress!"
                DownloadManager.STATUS_SUCCESSFUL -> "Download complete!"
                else -> "Download is nowhere in sight"
            }
        return msg
    }

    var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent?) {
            if(constants.startDownloading==2){
                binding!!.progressMainBg.visibility=View.GONE
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "Image/*"
                intent.putExtra(Intent.EXTRA_STREAM, currentImagePath)
                startActivity(Intent.createChooser(intent, "Share Image"))
            }
            else if(constants.startDownloading==3){
                if (currentImageModel!!.cityName!=null && currentImageModel!!.countryName!=null && currentImageModel!!.details!=null
                    && currentImageModel!!.viewName!=null && currentImagePath!=null) {
                    val status=database!!.mStreetViewDao!!.insertFavouriteStreet(
                        MyStreetsModel(
                            currentImageModel!!.cityName,
                            currentImageModel!!.countryName,
                            currentImageModel!!.viewName,
                            currentImagePath.toString()
                        )
                    )
                    if(status>0){
                        binding!!.progressMainBg.visibility=View.GONE
                        Toast.makeText(this@StreetViewActivity,"Saved in Favourites!",Toast.LENGTH_SHORT).show()
                    }else{
                        binding!!.progressMainBg.visibility=View.GONE
                        Toast.makeText(this@StreetViewActivity,"Not Saved!",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                binding!!.progressMainBg.visibility=View.GONE
                Toast.makeText(this@StreetViewActivity,"Download Completed",Toast.LENGTH_SHORT).show()
            }
            Log.d("onCompleteTAG", "onReceive: "+"Completed")
        }
    }

    var onNotificationClick: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent?) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onComplete)
        unregisterReceiver(onNotificationClick)
    }

    fun shareImage(currentLink: String, currentImg: String) {
        Log.d("shareImageTAG", "shareImage: "+currentImg+","+currentLink)
        startDownload(currentLink, currentImg)
        queryStatus()

    }



}