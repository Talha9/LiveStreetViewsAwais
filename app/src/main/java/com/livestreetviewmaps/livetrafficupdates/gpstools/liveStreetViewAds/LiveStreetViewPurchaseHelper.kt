package com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*


class LiveStreetViewPurchaseHelper(private val activityContext: Context) :
    PurchasesUpdatedListener {

    private lateinit var googleBillingLiveStreetViewClient: BillingClient
    private val TAG = "BillingLogger:"
    private val listAvailLiveStreetViewPurchases = ArrayList<SkuDetails>()
    private lateinit var billingLiveStreetViewPreferences: SharedPreferences

    init {
        initMyBillingClientLiveStreetView()
    }

    private fun initMyBillingClientLiveStreetView() {
        billingLiveStreetViewPreferences =
            activityContext.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE)
        googleBillingLiveStreetViewClient = BillingClient
            .newBuilder(activityContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        googleBillingLiveStreetViewClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google Billing is Connected")
                    fetchLiveStreetViewAllInAppsFromConsole() /*available on console*/
                    fetchLiveStreetViewPurchasedInAppsFromConsole()  /*user has purchased*/
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Google Billing is  Disconnected")
            }
        })
    }

    private fun fetchLiveStreetViewAllInAppsFromConsole() {
        val skuListToQuery = ArrayList<String>()
        skuListToQuery.add("ads_purchase")
        skuListToQuery.add("all_premium_modules")
        skuListToQuery.add("both_remove_ads_modules")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuListToQuery).setType(BillingClient.SkuType.INAPP)
        googleBillingLiveStreetViewClient.querySkuDetailsAsync(
            params.build(),
            object : SkuDetailsResponseListener {
                override fun onSkuDetailsResponse(
                    result: BillingResult,
                    skuDetails: MutableList<SkuDetails>?
                ) {
                    //Log.i(TAG, "onSkuResponse ${result?.responseCode}")
                    if (skuDetails != null) {
                        for (skuDetail in skuDetails) {
                            listAvailLiveStreetViewPurchases.add(skuDetail)
                            Log.i(TAG, skuDetail.toString())
                        }
                    } else {
                        Log.i(TAG, "No skus for this application")
                    }
                }

            })
    }


/*    fun fetchAIGpsNavPurchasedInAppsFromConsole() {
        val purchaseResults: Purchase.PurchasesResult =
            googleBillingAIGpsNavClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (purchaseResults.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val listPurchased = purchaseResults.purchasesList
            if (listPurchased != null && listPurchased.size > 0) {
                for (singlePurchase in listPurchased) {
                    if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        Log.d(TAG, "Product Purchased: ${singlePurchase.sku}")
                        billingAIGpsNavPreferences.edit().putBoolean(singlePurchase.sku, true).apply()
                    } else {
                        billingAIGpsNavPreferences.edit().putBoolean(singlePurchase.sku, false).apply()
                        Log.d(TAG, "Product Not Purchased: ${singlePurchase.sku}")
                    }
                }
            } else {
                Log.d(TAG, "Array List Purchase Null$listPurchased")
            }
        } else {
            Log.d(TAG, "Billing Checker Failed 1: ${purchaseResults.billingResult.responseCode}")
        }

    }*/


    fun fetchLiveStreetViewPurchasedInAppsFromConsole() {

        googleBillingLiveStreetViewClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,object :PurchasesResponseListener{
            override fun onQueryPurchasesResponse(billingResult: BillingResult, listPurchased: MutableList<Purchase>) {

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (listPurchased.size > 0) {
                        for (singlePurchase in listPurchased) {
                            if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                Log.d(TAG, "Product Purchased: ${singlePurchase.skus[0]}")
                                billingLiveStreetViewPreferences.edit().putBoolean(singlePurchase.skus[0], true).apply()
                            } else {
                                billingLiveStreetViewPreferences.edit().putBoolean(singlePurchase.skus[0], false).apply()
                                Log.d(TAG, "Product Not Purchased: ${singlePurchase.skus[0]}")
                            }
                        }
                    } else {
                        Log.d(TAG, "Array List Purchase Null$listPurchased")
                    }
                } else {
                    Log.d(TAG, "Billing Checker Failed 1: ${billingResult.responseCode}")
                }

            }

        })

    }

    fun purchaseAIGpsNavAdsPackage() {
        Log.d(TAG, "Going to purchase ads_purchase")
        if (listAvailLiveStreetViewPurchases.size > 0) {
            try {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(listAvailLiveStreetViewPurchases[0])
                    .build()
                val responseCode =
                    googleBillingLiveStreetViewClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    fun purchaseAIGpsNavPremiumModules() {
        Log.d(TAG, "Going to purchase ads_purchase")
        if (listAvailLiveStreetViewPurchases.size > 0) {
            try {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(listAvailLiveStreetViewPurchases[1])
                    .build()
                val responseCode =
                    googleBillingLiveStreetViewClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }
    fun purchaseAIGpsNavBothAdsModules() {
        Log.d(TAG, "Going to purchase ads_purchase")
        if (listAvailLiveStreetViewPurchases.size > 0) {
            try {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(listAvailLiveStreetViewPurchases[2])
                    .build()
                val responseCode =
                    googleBillingLiveStreetViewClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.d(TAG, "onPurchases Successfully Purchased : " + purchase.skus[0])
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "Google Billing Cancelled")
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "Google Billing Purchased Already")
            Toast.makeText(
                activityContext,
                "You have already purchased this item",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Log.d(TAG, "Google billing other error " + billingResult.responseCode)
        }
    }

    private val purchaseAcknowledgedListener: AcknowledgePurchaseResponseListener = object
        : AcknowledgePurchaseResponseListener {
        override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
            Log.d(TAG, "Success Acknowledged : ${p0.responseCode}  :${p0.debugMessage}")
            fetchLiveStreetViewPurchasedInAppsFromConsole()
        }

    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                Log.d(TAG, "Process acknowledging: ${purchase.skus[0]}")
                val acknowledgeParamaters = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                googleBillingLiveStreetViewClient.acknowledgePurchase(
                    acknowledgeParamaters,
                    purchaseAcknowledgedListener
                )
                /*Now update preferences..either restart app so that query will
                execute or here make preferences true for ads*/

                /*here only one product so call preferences in acknowledged.*/

                /*or after acknowledged call query Method*/
            }
        }
    }
}