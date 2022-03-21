package com.earthmap.location.tracker.shareaddress.routefinder.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
/*

class LiveStreetViewFcmMessaging : FirebaseMessagingService() {
    private var titleGlobeEarthMap: String? = null
    private var bodyGlobeEarthMap: String? = null
    private var descriptionGlobeEarthMap: String? = null
    private var iconGlobeEarthMap: String? = null
    private var bitmapGlobeEarthMap: Bitmap? = null
    private var bitIconGlobeEarthMap: Bitmap? = null
    private val CHANNEL_ID_MY_FCM_GlobeEarthMap: String = "id_channel"
    private val CHANNEL_NAME_MY_FCM_GlobeEarthMap: String = "name_channel"
    private val CHANNEL_DESCRIPTION_MY_FCM_GlobeEarthMap: String = "description_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i("onMessageReceived","MessageReceived:")
        try {
            titleGlobeEarthMap = remoteMessage!!.data["title"]
            bodyGlobeEarthMap = remoteMessage.data["app_url"]
            descriptionGlobeEarthMap = remoteMessage.data["short_desc"]
            iconGlobeEarthMap = remoteMessage.data["icon"]
            bitIconGlobeEarthMap = getBitmapImageFromRemoteUrlGlobeEarthMap(iconGlobeEarthMap!!)
            bitmapGlobeEarthMap = getBitmapImageFromRemoteUrlGlobeEarthMap(remoteMessage.data["feature"]!!)
        } catch (e: Exception) {
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mNotificationManagerGlobeEarthMap =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importanceGlobeEarthMap = NotificationManager.IMPORTANCE_HIGH
            val mChannelGlobeEarthMap =
                NotificationChannel(
                    CHANNEL_ID_MY_FCM_GlobeEarthMap,
                    CHANNEL_NAME_MY_FCM_GlobeEarthMap,
                    importanceGlobeEarthMap
                )
            mChannelGlobeEarthMap.description = CHANNEL_DESCRIPTION_MY_FCM_GlobeEarthMap
            mChannelGlobeEarthMap.enableLights(true)
            mChannelGlobeEarthMap.lightColor = Color.BLUE
            mChannelGlobeEarthMap.enableVibration(true)
            mChannelGlobeEarthMap.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            try {
                mNotificationManagerGlobeEarthMap.createNotificationChannel(mChannelGlobeEarthMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fcmNotificationGlobeEarthMap()
    }

    private fun fcmNotificationGlobeEarthMap() {
        val bigViewGlobeEarthMap = RemoteViews(packageName, R.layout.live_globe_fcm_notification_large)
        val smallViewGlobeEarthMap = RemoteViews(packageName, R.layout.live_globe_fcm_notification_small)
        bigViewGlobeEarthMap.setTextViewText(R.id.text, descriptionGlobeEarthMap)
        smallViewGlobeEarthMap.setTextViewText(R.id.text, descriptionGlobeEarthMap)
        bigViewGlobeEarthMap.setTextViewText(R.id.titlePlaceName, titleGlobeEarthMap)
        smallViewGlobeEarthMap.setTextViewText(R.id.titlePlaceName, titleGlobeEarthMap)
        smallViewGlobeEarthMap.setImageViewBitmap(R.id.image_app, bitIconGlobeEarthMap)
        bigViewGlobeEarthMap.setImageViewBitmap(R.id.image_app, bitIconGlobeEarthMap)
        bigViewGlobeEarthMap.setImageViewBitmap(R.id.image_pic, bitmapGlobeEarthMap)
        val notificationCompGlobeEarthMap =
            NotificationCompat.Builder(this, CHANNEL_ID_MY_FCM_GlobeEarthMap)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setCustomBigContentView(bigViewGlobeEarthMap).setCustomContentView(smallViewGlobeEarthMap)
        val resultIntentGlobeEarthMap = Intent(Intent.ACTION_VIEW, Uri.parse(bodyGlobeEarthMap))
        resultIntentGlobeEarthMap.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        resultIntentGlobeEarthMap.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntGlobeEarthMap =
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            PendingIntent.getActivity(
                this,
                0,
                resultIntentGlobeEarthMap,
                PendingIntent.FLAG_IMMUTABLE
            )
        }else{
            PendingIntent.getActivity(
                this,
                0,
                resultIntentGlobeEarthMap,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        notificationCompGlobeEarthMap.setContentIntent(pendingIntGlobeEarthMap)
        val notificaitonManagerGlobeEarthMap =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificaitonManagerGlobeEarthMap.notify(1, notificationCompGlobeEarthMap.build())
    }

    private fun getBitmapImageFromRemoteUrlGlobeEarthMap(imageUrl: String): Bitmap? {
        try {
            val urlGlobeEarthMap = URL(imageUrl)
            val connectionGlobeEarthMap = urlGlobeEarthMap.openConnection() as HttpURLConnection
            connectionGlobeEarthMap.doInput = true
            connectionGlobeEarthMap.connect()
            var inputGlobeEarthMap: InputStream? = null
            try {
                inputGlobeEarthMap = connectionGlobeEarthMap.inputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return BitmapFactory.decodeStream(inputGlobeEarthMap)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }
}*/
