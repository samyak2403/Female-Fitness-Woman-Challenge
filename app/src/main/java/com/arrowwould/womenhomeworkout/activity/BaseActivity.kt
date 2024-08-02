package com.arrowwould.womenhomeworkout.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.arrowwould.womenhomeworkout.SessionManager
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.common.LocaleManager
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener

open class BaseActivity : AppCompatActivity() {

    var adView: AdView? = null
    var adRequest: AdRequest? = null
    open var TAG = "baseActivity"
    open var sessionManager: SessionManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        adRequest = AdRequest.Builder().build()
        adView = AdView(this)
        sessionManager = SessionManager(this)
        MobileAds.initialize(
            this
        ) { }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }

    private fun isOnline(): Boolean {
        var outcome = false
        try {
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.allNetworkInfo
                for (tempNetworkInfo in networkInfo) {
                    if (tempNetworkInfo.isConnected) {
                        outcome = true
                        break
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return outcome
    }

    private lateinit var alertDialogExit: AlertDialog.Builder

    @SuppressLint("WrongConstant")
    fun openExitConfirm() {
        alertDialogExit = AlertDialog.Builder(this)
        alertDialogExit.setTitle(resources.getString(R.string.app_name))
        alertDialogExit.setMessage("Are you sure to exit ?")
        alertDialogExit.setCancelable(false)
        alertDialogExit.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogExit.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            finishAffinity()
        }
        val alert = alertDialogExit.create()
        alert.show()
        val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        nbutton.setTextColor(Color.BLACK)
        val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        pbutton.setTextColor(Color.BLACK)
    }


    fun openInternetDialog(callbackListener: CallbackListener) {
        if (!isOnline()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("No internet Connection")
            builder.setCancelable(false)
            builder.setMessage("Please turn on internet connection to continue")
            builder.setNegativeButton("Retry") { dialog, _ ->
                dialog!!.dismiss()
                openInternetDialog(callbackListener)
                callbackListener.onRetry()

            }
            builder.setPositiveButton("Close") { dialog, _ ->
                dialog!!.dismiss()
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(homeIntent)
                finishAffinity()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }


    open fun showBigNativeAdView(nativeAd: NativeAd) {
        // Set the media view.
        val frameLayout = findViewById<FrameLayout>(R.id.fl_native)
        val adView = layoutInflater
            .inflate(R.layout.custom_native_big, null) as NativeAdView


        // Set other ad assets.
        adView.mediaView = adView.findViewById<View>(R.id.native_app_media) as MediaView
        adView.headlineView = adView.findViewById(R.id.native_ad_title)
        adView.bodyView = adView.findViewById(R.id.native_ad_body)
        adView.callToActionView = adView.findViewById(R.id.nativeSponsoredTextView)
        adView.iconView = adView.findViewById(R.id.native_ad_icon_image)
        adView.mediaView!!.mediaContent = nativeAd.mediaContent


        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView?)!!.text = nativeAd.headline


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView?)!!.setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            Log.d(TAG, "showCustomNativeSmall: buttonText " + nativeAd.callToAction)
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as TextView?)!!.text = nativeAd.callToAction
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
        frameLayout.removeAllViews()
        frameLayout.addView(adView)
    }

    open fun setBigNativeAdd() {
        if (sessionManager?.getBooleanValue(Constant.Adshow) == true) {
            val adLoader =
                AdLoader.Builder(this, sessionManager!!.getStringValue(Constant.nativeAdId))
                    .forNativeAd { nativeAd ->
                        Log.d(TAG, "onNativeAdLoaded: ")
                        // Show the ad.
                        showBigNativeAdView(nativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: ")
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder() // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build()
                    )
                    .build()
            adRequest?.let { adLoader.loadAd(it) }
        }
    }

    open fun setSmallNativeAdd() {
        if (sessionManager?.getBooleanValue(Constant.Adshow) == true) {
            val adLoader =
                AdLoader.Builder(this, sessionManager!!.getStringValue(Constant.nativeAdId))
                    .forNativeAd { nativeAd ->
                        Log.d(TAG, "onNativeAdLoaded: ")
                        // Show the ad.
                        showCustomNativeSmall(nativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: ")
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder() // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build()
                    )
                    .build()
            adRequest?.let { adLoader.loadAd(it) }
        }
    }


    open fun showCustomNativeSmall(nativeAd: NativeAd) {
        // Set the media view.
        val frameLayout = findViewById<FrameLayout>(R.id.fl_native)
        val adView = layoutInflater
            .inflate(R.layout.small_native_ad_layout, null) as NativeAdView


        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.native_ad_title)
        adView.bodyView = adView.findViewById(R.id.native_ad_body)
        adView.callToActionView = adView.findViewById(R.id.nativeSponsoredTextView)
        adView.iconView = adView.findViewById(R.id.native_ad_main_image)


        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView?)!!.text = nativeAd.headline


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView?)!!.setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            Log.d(TAG, "showCustomNativeSmall: buttonText " + nativeAd.callToAction)
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as TextView?)!!.text = nativeAd.callToAction
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
        frameLayout.removeAllViews()
        frameLayout.addView(adView)
    }

    open fun setBannerAdd(adContainer: View) {
        if (sessionManager!!.getBooleanValue(Constant.Adshow)) {
            adView!!.setAdSize(AdSize.BANNER)
            adView!!.adUnitId = sessionManager!!.getStringValue(Constant.bannerAdId)
            (adContainer as RelativeLayout).addView(adView)
            adView!!.loadAd(adRequest!!)
        }
    }

}