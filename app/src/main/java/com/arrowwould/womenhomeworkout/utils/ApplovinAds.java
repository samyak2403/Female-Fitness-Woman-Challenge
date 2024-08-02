package com.arrowwould.womenhomeworkout.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.arrowwould.womenhomeworkout.R;


import java.util.concurrent.TimeUnit;

public class ApplovinAds {


    private static MaxInterstitialAd interstitialAd;
    private static int retryAttempt;
    private static MaxNativeAdLoader nativeAdLoader;
    private static MaxAd  nativeAd;

    public static void initialize(@NonNull Context context) {

        AppLovinSdk.getInstance( context ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {


                // AppLovin SDK is initialized, start loading ads
            }
        } );


    }


    public static void loadInterstitialAd(@NonNull Activity activity) {

        if (interstitialAd == null) {

                interstitialAd = new MaxInterstitialAd(activity.getString(R.string.maxInter), activity);
                interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

                        // Reset retry attempt
                        retryAttempt = 0;
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
// Interstitial ad is hidden. Pre-load the next ad
                        interstitialAd.loadAd();
                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
// Interstitial ad failed to load
                        // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                        retryAttempt++;
                        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                interstitialAd.loadAd();
                            }
                        }, delayMillis);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        // Interstitial ad failed to display. We recommend loading the next ad
                        interstitialAd.loadAd();
                    }
                });

                // Load the first ad
                interstitialAd.loadAd();

            }

        }


    public static void showInterstitialAd(@NonNull Activity activity) {
        if (interstitialAd.isReady()){
            interstitialAd.showAd();
        }else {
            loadInterstitialAd(activity);
        }

    }


}
