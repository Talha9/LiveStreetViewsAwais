package  gps.navigation.weather.nearby.streetview.liveearthmap.gpsnavigation.Ads

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.livestreetviewmaps.livetrafficupdates.gpstools.R
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewBillingHelper
import com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds.LiveStreetViewMyAppAds

class LiveStreetViewMyAppNativeAds {

    companion object {
        fun loadLiveStreetViewAdmobNativeAdPriority(
            mContext: Activity,
            frameLayout: FrameLayout
        ) {
            val inflater = LayoutInflater.from(mContext)
            val builder = AdLoader.Builder(
                mContext,
                LiveStreetViewMyAppAds.native_admob_inApp
            )
            var admobNativeAd: NativeAd? = null
            builder.forNativeAd { unifiedNativeAd ->
                if (admobNativeAd != null) {
                    admobNativeAd!!.destroy()
                }
                try {
                    admobNativeAd = unifiedNativeAd
                    val adView =  //actually card view of fb ad
                        inflater.inflate(
                            R.layout.live_streat_view_native_admob_native_custom_layout,
                            null
                        ) as NativeAdView
                    populateUnifiedAIGpsNavNativeAdView(unifiedNativeAd, adView)
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true/*startVideoAdsMuted.isChecked()*/)
                .build()

            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()

            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : com.google.android.gms.ads.AdListener() {

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                }
            }).build()

            val billingHelper =
                LiveStreetViewBillingHelper(
                    mContext
                )
            if (billingHelper.isNotAdPurchased) {
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        private fun populateUnifiedAIGpsNavNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

            val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
            adView.mediaView = mediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)
            adView.storeView = adView.findViewById(R.id.ad_store)
            adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
            (adView.headlineView as TextView).text = nativeAd.headline
            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.INVISIBLE
            } else {
                adView.bodyView.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                adView.callToActionView.visibility = View.INVISIBLE
            } else {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                adView.iconView.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable
                )
                adView.iconView.visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                adView.priceView.visibility = View.INVISIBLE
            } else {
                adView.priceView.visibility = View.VISIBLE
                (adView.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                adView.storeView.visibility = View.INVISIBLE
            } else {
                adView.storeView.visibility = View.VISIBLE
                (adView.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                adView.advertiserView.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
            val vc = nativeAd.mediaContent.videoController

            if (vc.hasVideoContent()) {
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                }
            } else {
            }
        }

    }

}