package com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livestreetviewmaps.livetrafficupdates.gpstools.R;
import com.livestreetviewmaps.livetrafficupdates.gpstools.Utils.MyAppClass;




public class LiveStreetViewMyAppAds {

    public static String appid_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_app_id);
    public static String interstitial_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_interstitial_ad_id);
    public static String banner_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_banner_ad_id);
    public static String native_admob_inApp = MyAppClass.Companion.getStr(R.string.admob_native_ad_id);

    public static String mapbox_access_token_1 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_1);
    public static String mapbox_access_token_2 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_2);
    public static String mapbox_access_token_3 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_3);
    public static String mapbox_access_token_4 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_4);
    public static String mapbox_access_token_5 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_5);
    public static String mapbox_access_token_6 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_6);
    public static String mapbox_access_token_7 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_7);
    public static String mapbox_access_token_8 = MyAppClass.Companion.getStr(R.string.mapbox_access_token_8);


    public static boolean shouldShowAdmob = true;
    public static boolean haveGotSnapshot = false;

    public static boolean ctr_control = false;

    public static boolean canReLoadedAdMob = true;

    public static InterstitialAd admobInterstitialAd;
    public static boolean shouldGoForAds = true;

    public static long next_ads_time = 20000;
    public static double current_counter = 1;

    private static DatabaseReference adsDatabaseReferenceStreeView = FirebaseDatabase.getInstance().getReference("LiveStreetViewSpace");
    private static final Handler myHandler = new Handler();

    public static void setHandlerForAd() {
        shouldGoForAds = false;
        Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeStart: " + shouldGoForAds);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldGoForAds = true;
                Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeComplete: " + shouldGoForAds);
            }
        }, next_ads_time);
    }


    public static void addModelToFirebase(Context mContext) {
        adsDatabaseReferenceStreeView.child("RelesAds").setValue(new LiveStreetViewMyAdModel(
                 mContext.getString(R.string.admob_banner_ad_id)
                , mContext.getString(R.string.admob_interstitial_ad_id)
                , mContext.getString(R.string.admob_native_ad_id)
                , shouldShowAdmob
                , next_ads_time
                , current_counter
        ));

    }

    private static void loadLiveEarthMapAdMobBanner(final LinearLayout adContainer, final AdView mAdView, final Context context) {



        try {
            mAdView.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("ConstantAdsLoadAds", "Banner AdMob Loaded");
                try {
                  //  adContainer.removeAllViews();
                    adContainer.addView(mAdView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.destroy();
                Log.d("ConstantAdsLoadAds", "loadAdError : " + loadAdError.getMessage() +" : "+loadAdError.getResponseInfo().getResponseId());
                //MakerMyAppAds.loadMoPubBannerAd(adContainer, context);
            }
        });
    }

    //only fb
    public static void loadEarthMapBannerForMainMediation(final LinearLayout adContainer, final AdView mAdView, final Context context) {
        LiveStreetViewMyAppAds.loadLiveEarthMapAdMobBanner(adContainer, mAdView, context);

    }


    public static void preLoadAds(final Context context) {

        LiveStreetViewBillingHelper billingHelper = new LiveStreetViewBillingHelper(context);
        if (billingHelper.isNotAdPurchased()) {

            //admobeload
            if (admobInterstitialAd == null) {
                canReLoadedAdMob = false;
                InterstitialAd.load(context, LiveStreetViewMyAppAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob loaded");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob Faild");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = null;
                    }
                });

            } else {
                Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            }

        }
    }

    public static void preReLoadAds(final Context context) {
        LiveStreetViewBillingHelper billingHelper = new LiveStreetViewBillingHelper(context);
        if (billingHelper.isNotAdPurchased()) {
            //admobeload
            if (admobInterstitialAd != null) {
                Log.d("ConstantAdsLoadAds", "admobe ReAlReady loaded");
            } else {
                Log.d("ConstantAdsLoadAds", "canReLoadedAdMob " + canReLoadedAdMob);
                if (canReLoadedAdMob) {
                    canReLoadedAdMob = false;
                    InterstitialAd.load(context, LiveStreetViewMyAppAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            Log.d("ConstantAdsLoadAds", "Admob Reloaded");
                            canReLoadedAdMob = true;
                            admobInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.d("ConstantAdsLoadAds", "Admob ReFaild");
                            canReLoadedAdMob = true;
                            admobInterstitialAd = null;
                        }
                    });

                } else {
                    Log.d("ConstantAdsLoadAds", "Admob last ad request is in pending");
                }
            }

        }
    }



}

