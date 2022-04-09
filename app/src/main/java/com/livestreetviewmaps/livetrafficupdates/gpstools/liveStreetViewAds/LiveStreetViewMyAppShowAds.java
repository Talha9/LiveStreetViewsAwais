package com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class LiveStreetViewMyAppShowAds {

    //for simple backPressed
    public static void mediationBackPressedSimpleLiveStreetView(final Activity context, final InterstitialAd mInterstitialAd/*, final MoPubInterstitial moPubInterstitial*/) {
        if (mInterstitialAd != null && LiveStreetViewMyAppAds.shouldGoForAds) {
            mInterstitialAd.show(context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    LiveStreetViewMyAppAds.admobInterstitialAd = null;
                    LiveStreetViewMyAppAds.preReLoadAds(context);
                    LiveStreetViewMyAppAds.setHandlerForAd();
                    context.finish();

                }
            });
        } else {
            LiveStreetViewMyAppAds.preReLoadAds(context);
            context.finish();
        }

    }



    public static void meidationForClickSimpleLiveStreetView(final Context context, final InterstitialAd mInterstitialAd/*, final MoPubInterstitial moPubInterstitial*/, final Intent intent) {
        if (mInterstitialAd != null && LiveStreetViewMyAppAds.shouldGoForAds) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    LiveStreetViewMyAppAds.admobInterstitialAd = null;
                    LiveStreetViewMyAppAds.preReLoadAds(context);
                    LiveStreetViewMyAppAds.setHandlerForAd();
                    context.startActivity(intent);

                }
            });
        } else {
            LiveStreetViewMyAppAds.preReLoadAds(context);
            context.startActivity(intent);

        }

    }

    public static void meidationForClickFinishLiveStreetView(final Activity context, final InterstitialAd mInterstitialAd/*, final MoPubInterstitial moPubInterstitial*/, final Intent intent) {
        if (mInterstitialAd != null && LiveStreetViewMyAppAds.shouldGoForAds) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    LiveStreetViewMyAppAds.admobInterstitialAd = null;
                    LiveStreetViewMyAppAds.preReLoadAds(context);
                    LiveStreetViewMyAppAds.setHandlerForAd();
                    /*context.startActivity(intent);*/
                    context.finish();

                }
            });
        } else {
            LiveStreetViewMyAppAds.preReLoadAds(context);
          /*  context.startActivity(intent);*/
            context.finish();
        }

    }

    public static void meidationForClickFragmentLiveStreetView(final Context context, final InterstitialAd mInterstitialAd) {
        if (mInterstitialAd != null && LiveStreetViewMyAppAds.shouldGoForAds) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    LiveStreetViewMyAppAds.admobInterstitialAd = null;
                    LiveStreetViewMyAppAds.preReLoadAds(context);
                    LiveStreetViewMyAppAds.setHandlerForAd();

                }
            });
        } else {
            LiveStreetViewMyAppAds.preReLoadAds(context);
        }

    }


}
