package com.livestreetviewmaps.livetrafficupdates.gpstools.fcm

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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import java.io.IOException

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class LiveStreetViewFcmMessaging : FirebaseMessagingService() {
    private var titleLiveStreetView: String? = null
    private var bodyLiveStreetView: String? = null
    private var descriptionLiveStreetView: String? = null
    private var iconLiveStreetView: String? = null
    private var bitmapLiveStreetView: Bitmap? = null
    private var bitIconLiveStreetView: Bitmap? = null
    private val CHANNEL_ID_MY_FCM_LiveStreetView: String = "id_channel"
    private val CHANNEL_NAME_MY_FCM_LiveStreetView: String = "name_channel"
    private val CHANNEL_DESCRIPTION_MY_FCM_LiveStreetView: String = "description_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.i("onMessageReceived", "MessageReceived:")
        try {
            titleLiveStreetView = remoteMessage!!.data["title"]
            bodyLiveStreetView = remoteMessage.data["app_url"]
            descriptionLiveStreetView = remoteMessage.data["short_desc"]
            iconLiveStreetView = remoteMessage.data["icon"]
            bitIconLiveStreetView = getBitmapImageFromRemoteUrlLiveStreetView(iconLiveStreetView!!)
            bitmapLiveStreetView =
                getBitmapImageFromRemoteUrlLiveStreetView(remoteMessage.data["feature"]!!)
        } catch (e: Exception) {
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mNotificationManagerLiveStreetView =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importanceLiveStreetView = NotificationManager.IMPORTANCE_HIGH
            val mChannelLiveStreetView =
                NotificationChannel(
                    CHANNEL_ID_MY_FCM_LiveStreetView,
                    CHANNEL_NAME_MY_FCM_LiveStreetView,
                    importanceLiveStreetView
                )
            mChannelLiveStreetView.description = CHANNEL_DESCRIPTION_MY_FCM_LiveStreetView
            mChannelLiveStreetView.enableLights(true)
            mChannelLiveStreetView.lightColor = Color.BLUE
            mChannelLiveStreetView.enableVibration(true)
            mChannelLiveStreetView.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            try {
                mNotificationManagerLiveStreetView.createNotificationChannel(mChannelLiveStreetView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fcmNotificationLiveStreetView()
    }

    private fun fcmNotificationLiveStreetView() {
        val bigViewLiveStreetView =
            RemoteViews(packageName, R.layout.live_street_view_fcm_notification_large)
        val smallViewLiveStreetView =
            RemoteViews(packageName, R.layout.live_street_view_fcm_notification_small)
        bigViewLiveStreetView.setTextViewText(R.id.text, descriptionLiveStreetView)
        smallViewLiveStreetView.setTextViewText(R.id.text, descriptionLiveStreetView)
        bigViewLiveStreetView.setTextViewText(R.id.titlePlaceName, titleLiveStreetView)
        smallViewLiveStreetView.setTextViewText(R.id.titlePlaceName, titleLiveStreetView)
        smallViewLiveStreetView.setImageViewBitmap(R.id.image_app, bitIconLiveStreetView)
        bigViewLiveStreetView.setImageViewBitmap(R.id.image_app, bitIconLiveStreetView)
        bigViewLiveStreetView.setImageViewBitmap(R.id.image_pic, bitmapLiveStreetView)
        val notificationCompLiveStreetView =
            NotificationCompat.Builder(this, CHANNEL_ID_MY_FCM_LiveStreetView)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setCustomBigContentView(bigViewLiveStreetView)
                .setCustomContentView(smallViewLiveStreetView)
        val resultIntentLiveStreetView = Intent(Intent.ACTION_VIEW, Uri.parse(bodyLiveStreetView))
        resultIntentLiveStreetView.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        resultIntentLiveStreetView.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntLiveStreetView =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(
                    this,
                    0,
                    resultIntentLiveStreetView,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    0,
                    resultIntentLiveStreetView,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        notificationCompLiveStreetView.setContentIntent(pendingIntLiveStreetView)
        val notificaitonManagerLiveStreetView =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificaitonManagerLiveStreetView.notify(1, notificationCompLiveStreetView.build())
    }

    private fun getBitmapImageFromRemoteUrlLiveStreetView(imageUrl: String): Bitmap? {
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
}
