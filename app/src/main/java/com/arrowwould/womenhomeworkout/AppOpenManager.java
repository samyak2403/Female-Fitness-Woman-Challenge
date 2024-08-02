package com.arrowwould.womenhomeworkout;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.arrowwould.womenhomeworkout.activity.SplashActivity;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "mmmm";
    private static String AD_UNIT_ID = "ca-app-pub-9893051924658947/6493796243";
    private AppOpenAd appOpenAd = null;
    public static boolean isShowingAd = false,adLoaded = false;

    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final SplashActivity myApplication;

    /** Constructor */
    public AppOpenManager(SplashActivity myApplication) {
        this.myApplication = myApplication;
    }

    /** Request an ad */
    public void fetchAd(String appOpen_Ads_id) {
        AD_UNIT_ID = appOpen_Ads_id;

        isShowingAd = false;
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
//                        if (isShowingAd == false){
//                            showAdIfAvailable();
//                        }
                        adLoaded = true;
                        Log.d("mmmm","Load Success");
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("mmmm","Load fail");
//                        fetchAd();
                        ((SplashActivity)myApplication).intentToHomeScreen();
                        ((SplashActivity)myApplication).stopCountdown();
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);

    }


    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = true;
                            adLoaded = false;
                            ((SplashActivity)myApplication).intentToHomeScreen();
//                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(myApplication);

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd(AD_UNIT_ID);
        }
    }


    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    public boolean isAdAvailable() {
        return appOpenAd != null;
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public static boolean adsisLoaded(){
        return adLoaded;
    }

    public static boolean adsisShowed(){
        return isShowingAd;
    }

}