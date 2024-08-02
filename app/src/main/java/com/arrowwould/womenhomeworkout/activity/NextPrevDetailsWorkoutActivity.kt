package com.arrowwould.womenhomeworkout.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.common.AppControl
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.objects.CommonConstantAd
import com.utillity.objects.CommonString
import com.utillity.objects.LocalDB
import com.utillity.objects.Utils
import com.utillity.pojo.DayExTableClass
import kotlinx.android.synthetic.main.activity_next_prev_details_workout.*
import java.util.*
import kotlin.collections.ArrayList

class NextPrevDetailsWorkoutActivity : BaseActivity(), CallbackListener {

    private var mWakeLock: PowerManager.WakeLock? = null

    private var timeCountDown = 0
    private var timer: Timer? = null
    private var workoutPos: Int = 0
    private var pWorkoutList = ArrayList<DayExTableClass>()
    private lateinit var mContext: Context
    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_prev_details_workout)

        mContext = this

        if(LocalDB.getKeepScreen(mContext)){
            val pm =  getSystemService(Context.POWER_SERVICE) as PowerManager
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag")
            mWakeLock!!.acquire()
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        try {
            workoutPos = intent.getIntExtra(CommonString.extra_workout_list_pos, 0)
            pWorkoutList = intent.getSerializableExtra(CommonString.extra_exercise_list) as ArrayList<DayExTableClass>
        } catch (e: Exception) {
            e.printStackTrace()
        }

        defaultSetup()
        setAction()
        startWorkoutTimer()

        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            CommonConstantAd.loadBannerGoogleAd(this,llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_RECTANGLE_BANNER_TYPE_AD)
        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            CommonConstantAd.loadFbAdFacebookMediumRectangle(this,llAdViewFacebook, Constant.FB_BANNER_MEDIUM_RECTANGLE)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            if(LocalDB.getKeepScreen(mContext)) {
                mWakeLock!!.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }



    private fun defaultSetup() {
//        CommonConstantAd.loadBannerAd(mContext, adView)


        try {
            progressBar.max = LocalDB.getRestTime(this)

            tvRestTime.text = "${mContext.getString(R.string.lbl_rest)} ${LocalDB.getRestTime(this)} ${mContext.getString(R.string.lbl_rest)}"

            val restString = "Take a rest Next ${pWorkoutList[workoutPos].exTime} ${pWorkoutList[workoutPos].exName
                .toLowerCase(Locale.getDefault()).replace("ups", "up's")}"

            AppControl.speechText(this, restString)

            if (pWorkoutList[workoutPos].exTime != CommonString.workout_type_second) {
                txtWorkoutTime.text = resources.getString(R.string.x).plus(pWorkoutList[workoutPos].exTime)
            } else {
                txtWorkoutTime.text = pWorkoutList[workoutPos].exTime
            }

            txtWorkoutName.text = pWorkoutList[workoutPos].exName
            txtSteps.text = (workoutPos + 1).toString().plus(" / ").plus(pWorkoutList.size)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            viewfliperWorkout.removeAllViews()
            val listImg: ArrayList<String> = Utils.getAssetItems(mContext, pWorkoutList[workoutPos].exPath)

            for (i in 0 until listImg.size) {
                val imgview = ImageView(mContext)
                Glide.with(mContext).load(listImg[i]).into(imgview)
                imgview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                viewfliperWorkout.addView(imgview)
            }

            viewfliperWorkout.isAutoStart = true
            viewfliperWorkout.flipInterval = mContext.resources.getInteger(R.integer.viewfliper_animation)
            viewfliperWorkout.startFlipping()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setAction() {
        btnSkip.setOnClickListener {
            try {
                timer?.cancel()
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startWorkoutTimer() {
        timeCountDown = 0

        val restTime = LocalDB.getRestTime(this)

        val handler = Handler()
        timer = Timer(false)

        val timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        timeCountDown++
                        txtCountDown.text = timeCountDown.toString()
                        progressBar.progress = timeCountDown
                        if (timeCountDown == restTime) {
                            finish()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        timer?.schedule(timerTask, 1000, 1000)

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
