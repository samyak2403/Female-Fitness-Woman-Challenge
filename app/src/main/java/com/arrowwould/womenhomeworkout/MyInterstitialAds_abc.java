package com.arrowwould.womenhomeworkout;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.arrowwould.womenhomeworkout.common.Constant;


public class MyInterstitialAds_abc {

    private static final String TAG = "interad";
    OnInterstitialAdListnear interAdClickListner;
    private Context context;
    private InterstitialAd mInterstitialAd;

    SessionManager sessionManager;


    public MyInterstitialAds_abc(Context context, OnInterstitialAdListnear interAdClickListner) {
        this.context = context;
        this.interAdClickListner = interAdClickListner;
        sessionManager = new SessionManager(context);
        initAds();
    }

    private void initAds() {

        if (sessionManager.getBooleanValue(Constant.Adshow)){

            if (sessionManager.getStringValue(Constant.interstitialAdId) != null) {

                String google = sessionManager.getStringValue(Constant.interstitialAdId);


                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, (google != null) ? google : "", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("TAG", "onAdFailedToLoad: google 1 " + loadAdError.getMessage());

                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                interAdClickListner.onAdClosed();
                            }


                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });

                    }

                });


                // mInterstitialAd.setAdUnitId(sessionManager.getAdmobInt());

            }
        } else {
            Log.d("TAG", "load intr: ad nathi thy");
        }


    }


    public void showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        } else {
            interAdClickListner.onAdFail();
        }
    }

    public interface OnInterstitialAdListnear {

        void onAdClosed();

        void onAdFail();
    }
}

