package com.livestreetviewmaps.livetrafficupdates.gpstools.liveStreetViewAds;

public class LiveStreetViewMyAdModel {
    public String banner_admob_inApp;
    public String interstitial_admob_inApp;
    public String native_admob_inApp;
    public boolean should_show_admob;
    public double next_ads_time;
    public double current_counter;

    public LiveStreetViewMyAdModel() {
    }

    public LiveStreetViewMyAdModel(String banner_admob_inApp, String interstitial_admob_inApp, String native_admob_inApp, boolean should_show_admob, double next_ads_time, double current_counter) {
        this.banner_admob_inApp = banner_admob_inApp;
        this.interstitial_admob_inApp = interstitial_admob_inApp;
        this.native_admob_inApp = native_admob_inApp;
        this.should_show_admob = should_show_admob;
        this.next_ads_time = next_ads_time;
        this.current_counter = current_counter;
    }

    public String getBanner_admob_inApp() {
        return banner_admob_inApp;
    }

    public void setBanner_admob_inApp(String banner_admob_inApp) {
        this.banner_admob_inApp = banner_admob_inApp;
    }

    public String getInterstitial_admob_inApp() {
        return interstitial_admob_inApp;
    }

    public void setInterstitial_admob_inApp(String interstitial_admob_inApp) {
        this.interstitial_admob_inApp = interstitial_admob_inApp;
    }

    public String getNative_admob_inApp() {
        return native_admob_inApp;
    }

    public void setNative_admob_inApp(String native_admob_inApp) {
        this.native_admob_inApp = native_admob_inApp;
    }

    public boolean isShould_show_admob() {
        return should_show_admob;
    }

    public void setShould_show_admob(boolean should_show_admob) {
        this.should_show_admob = should_show_admob;
    }

    public double getNext_ads_time() {
        return next_ads_time;
    }

    public void setNext_ads_time(double next_ads_time) {
        this.next_ads_time = next_ads_time;
    }

    public double getCurrent_counter() {
        return current_counter;
    }

    public void setCurrent_counter(double current_counter) {
        this.current_counter = current_counter;
    }
}
