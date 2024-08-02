package com.arrowwould.womenhomeworkout

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.ads.*
import com.arrowwould.womenhomeworkout.common.Constant


object ConstantAd {

    var adView: AdView? = null
    var adRequest: AdRequest? = null
    var TAG = "baseActivity"
    var sessionManager: SessionManager? = null

    fun setBannerAdd(adContainer: View , context: Context) {
        sessionManager = SessionManager(context)
        adView = AdView(context)
        adRequest = AdRequest.Builder().build()
        if (sessionManager!!.getBooleanValue(Constant.Adshow)) {
            adView!!.setAdSize(AdSize.BANNER)
            adView!!.adUnitId = sessionManager!!.getStringValue(Constant.bannerAdId)
            (adContainer as RelativeLayout).addView(adView)
            adView!!.loadAd(adRequest!!)
        }
    }


}
