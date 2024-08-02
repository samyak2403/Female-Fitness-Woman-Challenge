package com.arrowwould.womenhomeworkout.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.ads.MobileAds
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.arrowwould.womenhomeworkout.AppOpenManager
import com.arrowwould.womenhomeworkout.BuildConfig
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.SessionManager
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.arrowwould.womenhomeworkout.utils.ApplovinAds
import com.utillity.db.DataHelper
import com.utillity.objects.LocalDB

class SplashActivity : BaseActivity(), CallbackListener {

    var context: Context? = null

    var newPackageName: String = BuildConfig.APPLICATION_ID
    var SHARED_KEY = "ONBOARDING_CONDITION"
    var editor: SharedPreferences.Editor? = null
    //var intent: Intent? = null
    override var sessionManager: SessionManager? = null
    var f213i = 0
    override var TAG = "SPLASH_ACTIVITY"
    var sharedPreferences: SharedPreferences? = null

    private val f212gm: String? = null
    private val f214sp: SharedPreferences? = null

    private var appOpenManager: AppOpenManager? = null
    private var countDownTimer: CountDownTimer? = null
    private var adsLoaderPbar: ProgressBar? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ApplovinAds.initialize(this)
        ApplovinAds.loadInterstitialAd(this);
        sessionManager = SessionManager(this)
        DataHelper(this).checkDBExist()

        context = this


//        val intent = Intent(this, SetupPlanActivity::class.java)
//        startActivity(intent)
        successCall()
        if (isNetworkConnected()) {
            initRemoteConfig()
        }


        adsLoaderPbar = findViewById(R.id.adsloader)

        MobileAds.initialize(
            this@SplashActivity
        ) { }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo


        if (isNetworkConnected()) {
            appOpenManager = AppOpenManager(this@SplashActivity)
            appOpenManager!!.fetchAd(resources.getString(R.string.app_open_ad_id))
            countDownTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (AppOpenManager.adsisLoaded() === true) {
                        adsLoaderPbar!!.setVisibility(View.GONE)
                        appOpenManager!!.showAdIfAvailable()
                        countDownTimer!!.cancel()
                        Log.d("mmmm", "ads is show")
                    }
                }

                override fun onFinish() {
//                    txtTitle.setText("done!");
                    if (AppOpenManager.adsisLoaded() !== true) {
                        intentToHomeScreen()
                        adsLoaderPbar!!.setVisibility(View.GONE)
                    }
                }
            }.start()
        } else {

            Handler().postDelayed({
                intentToHomeScreen()
                adsLoaderPbar!!.setVisibility(View.GONE)
            }, 3000)

            openInternetDialog(this)
        }




    }

    private fun successCall() {
        if (isNetworkConnected()) {
            //startNextActivity()
        } else {
            openInternetDialog(this)
        }
    }

    private fun startNextActivity() {
        if (LocalDB.getIsFirstTime(this)) {

            Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this, SetupPlanActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                finish()

            }, 1500)

        } else {

            Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this, SetupPlanActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                finish()

            }, 1500)

        }

    }


    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {
        successCall()
    }


    private fun initRemoteConfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    sessionManager!!.saveBooleanValue(
                        Constant.Adshow,
                        mFirebaseRemoteConfig.getBoolean(Constant.Adshow)
                    )
                    sessionManager!!.saveStringValue(
                        Constant.interstitialAdId,
                        mFirebaseRemoteConfig.getString(Constant.interstitialAdId)
                    )
                    sessionManager!!.saveStringValue(
                        Constant.nativeAdId,
                        mFirebaseRemoteConfig.getString(Constant.nativeAdId)
                    )
                    sessionManager!!.saveStringValue(
                        Constant.bannerAdId,
                        mFirebaseRemoteConfig.getString(Constant.bannerAdId)
                    )
                    Log.d(
                        TAG,
                        "onCreate: isAdShow " + sessionManager!!.getBooleanValue(Constant.Adshow)
                    )
                    Log.d(
                        TAG,
                        "onCreate: interstitial_ad_key" + sessionManager!!.getStringValue(Constant.interstitialAdId)
                    )
                    Log.d(
                        TAG,
                        "onCreate: native_ad_key " + sessionManager!!.getStringValue(Constant.nativeAdId)
                    )
                    Log.d(
                        TAG,
                        "onCreate: banner_key " + sessionManager!!.getStringValue(Constant.bannerAdId)
                    )
                    Log.d(
                        TAG,
                        "onCreate: appOpen_key " + sessionManager!!.getStringValue(Constant.bannerAdId)
                    )
                    val handler = Handler()
                    handler.postDelayed({
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        sessionManager!!.getBooleanValue(Constant.isBoarding)
                    }, 200)
                } else {
                    Log.d(TAG, "onComplete: fetch failed")
                    Toast.makeText(
                        this@SplashActivity, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun intentToHomeScreen() {
        Handler().postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                )
            }
        }, 20)
    }

    fun stopCountdown() {
        countDownTimer?.cancel()
        Log.d("mmmm", "stop countdown")
    }

}