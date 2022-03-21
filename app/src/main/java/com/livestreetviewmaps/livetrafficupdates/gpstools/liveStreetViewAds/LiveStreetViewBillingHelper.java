package com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds;

import android.content.Context;
import android.content.SharedPreferences;

public class LiveStreetViewBillingHelper {
    private SharedPreferences liveStreetViewBillingPreferences;

    public LiveStreetViewBillingHelper(Context content) {
        liveStreetViewBillingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE);
    }

    public boolean isNotAdPurchased() {
        return !liveStreetViewBillingPreferences.getBoolean("ads_purchase", false);
    }
    public void setLiveStreetViewBillingPreferences(boolean status) {
        liveStreetViewBillingPreferences.edit().putBoolean("ads_purchase", status).apply();
    }

}
