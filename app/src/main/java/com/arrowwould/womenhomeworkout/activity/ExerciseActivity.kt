package com.arrowwould.womenhomeworkout.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.common.AppControl
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.utillity.db.DataHelper
import com.utillity.interfaces.AdsCallback
import com.utillity.objects.*
import com.utillity.pojo.DayExTableClass
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*

class ExerciseActivity : BaseActivity(), AdsCallback, CallbackListener {

    private var mWakeLock: PowerManager.WakeLock? = null

    override fun onPause() {
        super.onPause()
        if (!boolIsReadyToGo) {
            pauseWorkOutTime()
        }
    }

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
        try {
            if (!boolIsReadyToGo) {
                if (!flagGotoVideo) {

                    val startExercise =
                        "To the exercise ${arrDayExTableClass[viewPagerWorkout.currentItem].exTime} ${arrDayExTableClass[viewPagerWorkout.currentItem].exName.toLowerCase(
                            Locale.getDefault()
                        ).replace("ups", "up's")}"

                    AppControl.speechText(this, startExercise)

                    mySoundUtil.playSound(0)
                }

                resumeWorkOutTime()
                flagGotoVideo = false
                flagTimerPause = false
//                flagTimerPause = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if(LocalDB.getKeepScreen(mContext)) {
                mWakeLock!!.release()
            }
            if (timerTask != null) {
                timerTask!!.cancel()
            }
            if (timer != null) {
                timer!!.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        try {
            confirmToExitDialog()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseWorkOutTime() {
        running = false
        currentTime = System.currentTimeMillis() - exStartTime
    }

    private fun resumeWorkOutTime() {
        running = true
        exStartTime = System.currentTimeMillis() - currentTime
    }

    private lateinit var arrDayExTableClass: ArrayList<DayExTableClass>
    private lateinit var mContext: Context
    private lateinit var mySoundUtil: MySoundUtil
    private var exStartTime: Long = 0
    private var running = false
    private var currentTime: Long = 0
    private var timeCountDown = 0

    private var flagTimerPause: Boolean = false
    private var flagGotoVideo: Boolean = false
    private var timer: Timer? = null

    private var boolIsReadyToGo = true
    private lateinit var rvWorkoutIndicatorAdapter: WorkIndicatorAdapter

    @SuppressLint("InvalidWakeLockTag")
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        mContext = this
        mySoundUtil = MySoundUtil(this)



        if(LocalDB.getKeepScreen(mContext)){
            val pm =  getSystemService(Context.POWER_SERVICE) as PowerManager
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag")
            mWakeLock!!.acquire()
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        try {
            arrDayExTableClass = intent.getSerializableExtra(CommonString.extra_exercise_list) as ArrayList<DayExTableClass>
        } catch (e: Exception) {
            e.printStackTrace()
        }

        exStartTime = System.currentTimeMillis()

        readyToGoSetup()

        init()
        initAction()

        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE
            && Constant.STATUS_ENABLE_DISABLE == Constant.ENABLE) {
            CommonConstantAd.googlebeforloadAd(this,
                Constant.GOOGLE_INTERSTITIAL)

        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK
            && Constant.STATUS_ENABLE_DISABLE == Constant.ENABLE) {
            CommonConstantAd.facebookbeforeloadFullAd(this,
                Constant.FB_INTERSTITIAL)
        }

       /* Constant.AD_GOOGLE -> {
            CommonConstantAd.googlebeforloadAd(this,
                Constant.GOOGLE_INTERSTITIAL)
        }
        Constant.AD_FACEBOOK -> {
            CommonConstantAd.facebookbeforeloadFullAd(this,
                Constant.FB_INTERSTITIAL)
        }*/
    }

    private fun init() {
        try {
            rvWorkoutIndicatorAdapter = WorkIndicatorAdapter()
            val layoutManager = FlexboxLayoutManager(mContext)
            layoutManager.flexWrap = FlexWrap.NOWRAP
            rvWorkoutStatus.layoutManager = layoutManager
            rvWorkoutStatus.adapter = rvWorkoutIndicatorAdapter

            val doWorkOutPgrAdpt = DoWorkoutPagerAdapter()
            viewPagerWorkout.adapter = doWorkOutPgrAdpt
//            viewPagerWorkout.currentItem = 0
            viewPagerWorkout.currentItem = LocalDB.getLastUnCompletedExPos(this,arrDayExTableClass[0].planId.toInt(),arrDayExTableClass[0].dayId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAction() {

        if (LocalDB.getSoundMute(this)) {
            imgSound.setImageResource(R.drawable.ic_sound_off)
        } else {
            imgSound.setImageResource(R.drawable.ic_sound_on)
        }


        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgbtnNext.setOnClickListener {
            workoutCompleted(viewPagerWorkout.currentItem + 1)
        }

        imgbtnPrev.setOnClickListener {
            try {
                if (viewPagerWorkout.currentItem != 0) {
                    workoutCompleted(viewPagerWorkout.currentItem - 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        imgbtnDone.setOnClickListener {
            workoutCompleted(viewPagerWorkout.currentItem + 1)
        }

        imgbtnPause.setOnClickListener {
            showWorkoutDetails()
        }

        imgInfo.setOnClickListener {
            showWorkoutDetails()
        }

        imgVideo.setOnClickListener {
            try {
                val strVideoLink = arrDayExTableClass[viewPagerWorkout.currentItem].exVideo
                if (strVideoLink != "") {
                    flagGotoVideo = true
                    flagTimerPause = true
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(strVideoLink)))
                } else {
                    Toast.makeText(this, getString(R.string.error_video_not_exist), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        imgSound.setOnClickListener {
            soundOptionDialog(this)
        }

        viewPagerWorkout.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(pos: Int) {
                workoutSetup(pos)
                try {
                    AppControl.speechText(
                        this@ExerciseActivity,
                        arrDayExTableClass[pos].exName.toLowerCase(Locale.getDefault()).replace("ups", "up's")
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }

    fun soundOptionDialog(context: Context) {

        var boolOtherClick = false
        var boolMuteClick = false
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(com.utillity.R.layout.dl_sound_option)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val swtMute = dialog.findViewById(com.utillity.R.id.swtMute) as Switch
        val swtVoiceGuide = dialog.findViewById(com.utillity.R.id.swtVoiceGuide) as Switch
        val swtCoachTips = dialog.findViewById(com.utillity.R.id.swtCoachTips) as Switch
        val btnOk = dialog.findViewById(com.utillity.R.id.btnOk) as TextView

        swtVoiceGuide.isChecked = LocalDB.getVoiceGuide(context)

        swtCoachTips.isChecked = LocalDB.getCoachTips(context)

        if (LocalDB.getSoundMute(context)) {
            swtMute.isChecked = true
            swtCoachTips.isChecked = false
            swtVoiceGuide.isChecked = false
        }

        swtMute.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolOtherClick) {
                        boolMuteClick = true
                        if (isChecked) {
                            LocalDB.setSoundMute(context, true)
                            swtVoiceGuide.isChecked = false
                            swtCoachTips.isChecked = false
                            imgSound.setImageResource(R.drawable.ic_sound_off)

                        } else {
                            LocalDB.setSoundMute(context, false)
                            swtVoiceGuide.isChecked = LocalDB.getVoiceGuide(context)
                            swtCoachTips.isChecked = LocalDB.getCoachTips(context)
                            imgSound.setImageResource(R.drawable.ic_sound_on)
                        }
                        boolMuteClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        swtVoiceGuide.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolMuteClick) {
                        boolOtherClick = true
                        if (isChecked) {
                            swtMute.isChecked = false
                            LocalDB.setSoundMute(context, false)
                            LocalDB.setVoiceGuide(context, true)
                        } else {
                            LocalDB.setVoiceGuide(context, false)
                        }
                        boolOtherClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        swtCoachTips.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolMuteClick) {
                        boolOtherClick = true
                        if (isChecked) {
                            swtMute.isChecked = false
                            LocalDB.setSoundMute(context, false)
                            LocalDB.setCoachTips(context, true)
                        } else {
                            LocalDB.setCoachTips(context, false)
                        }
                        boolOtherClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        btnOk.setOnClickListener {
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    private fun readyToGoSetup() {
        try {
            txtWorkoutTitle.text =
                arrDayExTableClass[0].exName

            llReadyToGo.visibility = View.VISIBLE
            rltBottomReadyToGo.visibility = View.VISIBLE
            txtTimer.visibility = View.GONE
            rltBottomControl.visibility = View.GONE

            countDownReadyToGo()

            val readyToGoText =
                "Ready to go start with ${arrDayExTableClass[viewPagerWorkout.currentItem].exName.toLowerCase(Locale.getDefault()).replace(
                    "ups",
                    "up's"
                )}"

            AppControl.speechText(this, readyToGoText)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        btnSkip.setOnClickListener {
            startExercise()
        }

    }

    private fun workoutSetup(pos: Int) {
        try {
            val pworkDetails: DayExTableClass = arrDayExTableClass[pos]
            if (timer != null) {
                timer?.cancel()
            }

            if (pworkDetails.exUnit == CommonString.workout_type_second) {
                rltStepTypeWorkOut.visibility = View.GONE
                rltTimeTypeWorkOut.visibility = View.VISIBLE
                startWorkoutTimer(pworkDetails.exTime.substring(pworkDetails.exTime.indexOf(":") + 1).toInt())
            } else {
                rltStepTypeWorkOut.visibility = View.VISIBLE
                rltTimeTypeWorkOut.visibility = View.GONE
            }

            rvWorkoutIndicatorAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var timerTask: TimerTask? = null
    private fun startWorkoutTimer(totalTime: Int) {
        timeCountDown = 0
        txtTimeCountDown.text = "".plus(timeCountDown.toString()).plus(" / ").plus(totalTime.toString())

        pbExTimeStatus.max = totalTime
        pbExTimeStatus.progress = 0


        val handler = Handler()
        timer = Timer(false)

        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        if (!flagTimerPause) {
                            timeCountDown++
                            txtTimeCountDown.text = "".plus(timeCountDown.toString()).plus(" / ").plus(totalTime.toString())

                            pbExTimeStatus.progress = timeCountDown

                            if (timeCountDown == totalTime) {
                                timer?.cancel()
                                workoutCompleted(viewPagerWorkout.currentItem + 1)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        timer?.schedule(timerTask, 1000, 1000)

    }

    private fun countDownReadyToGo() {

        var timeCountDown = 0

        val readyToGoTime = LocalDB.getCountDownTime(this)
        progressBar.max = readyToGoTime

        val handler = Handler()
        timer = Timer(false)

        val timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    try {
                        timeCountDown++
                        txtCountDown.text = timeCountDown.toString()
                        progressBar.progress = timeCountDown

                        if (timeCountDown == readyToGoTime) {
                            startExercise()
                        } else if ((readyToGoTime - timeCountDown) < 4) {
                            AppControl.speechText(mContext, (readyToGoTime - timeCountDown).toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        timer?.schedule(timerTask, 1000, 1000)

    }

    private fun start() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    setTime()
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 1000)
        exStartTime = System.currentTimeMillis()
        running = true
    }

    private fun setTime() {
        try {
            if (getElapsedTimeMin() == 0L) {
                val str = "0" + getElapsedTimeMin() + ":" + CommonUtility.getString2DFormat((getElapsedTimeSecs().toInt()))
                txtTimer.text = str
            } else {
                if (getElapsedTimeMin() + "".length > 1) {
                    val str = "" + getElapsedTimeMin() + ":" + CommonUtility.getString2DFormat((getElapsedTimeSecs().toInt()))
                    txtTimer.text = str
                } else {
                    val str = "0" + getElapsedTimeMin() + ":" + CommonUtility.getString2DFormat((getElapsedTimeSecs().toInt()))
                    txtTimer.text = str
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startExercise() {
        boolIsReadyToGo = false

        llReadyToGo.visibility = View.GONE
        rltBottomReadyToGo.visibility = View.GONE
        txtTimer.visibility = View.VISIBLE
        rltBottomControl.visibility = View.VISIBLE

        try {
            if (timer != null) {
                timer!!.cancel()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        init()
        start()

        workoutSetup(0)

        onResume()

    }

    var timeTotal: String = ""

    private fun workoutCompleted(pos: Int) {
        try {
            val dbHelper = DataHelper(this)
            dbHelper.updateCompleteExByDayExId(arrDayExTableClass[viewPagerWorkout.currentItem].dayExId)

            if (viewPagerWorkout.currentItem == (arrDayExTableClass.size - 1)) {
//                nextActivityStart()
                timeTotal = txtTimer.text.toString()
                when (Constant.AD_TYPE_FB_GOOGLE) {
                    Constant.AD_GOOGLE -> {
                        CommonConstantAd.showInterstitialAdsGoogle(this,this)
                    }
                    Constant.AD_FACEBOOK -> {
                        CommonConstantAd.showInterstitialAdsFacebook(this)
                    }
                    else -> {
                        nextActivityStart()
                    }
                }
            } else {
                mySoundUtil.playSound(1)
                flagTimerPause = true
                val intent = Intent(mContext, NextPrevDetailsWorkoutActivity::class.java)
                intent.putExtra(CommonString.extra_workout_list_pos, pos)
                intent.putExtra(CommonString.extra_exercise_list, arrDayExTableClass)
                startActivity(intent)
                viewPagerWorkout.currentItem = pos
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun nextActivityStart() {
        finish()
        val intent = Intent(mContext, CompletedActivity::class.java)
        intent.putExtra(CommonString.extra_exercise_list, arrDayExTableClass)
//        intent.putExtra("Duration", txtTimer.text)
        intent.putExtra("Duration", timeTotal)
        startActivity(intent)
        pauseWorkOutTime()
    }


    private fun showWorkoutDetails() {
        flagTimerPause = true
        try {
            val intent = Intent(mContext, WorkoutListInfoActivity::class.java)
            intent.putExtra(CommonString.extra_workout_details_type, CommonString.val_is_workout)
            intent.putExtra(CommonString.extra_exercise_list, arrDayExTableClass)
            intent.putExtra(CommonString.extra_workout_list_pos, viewPagerWorkout.currentItem)
            mContext.startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.none)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class WorkIndicatorAdapter : RecyclerView.Adapter<WorkIndicatorAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.row_of_ex_indicator, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            try {
                if (viewPagerWorkout.currentItem > position) {
//                    holder.viewIndicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTheme))
                    holder.viewIndicator.setBackgroundResource(R.drawable.view_line_select)
                } else {
//                    holder.viewIndicator.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray_))
                    holder.viewIndicator.setBackgroundResource(R.drawable.view_line)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun getItemCount(): Int {
            return arrDayExTableClass.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            internal var viewIndicator: View = itemView.findViewById(R.id.viewIndicator) as View
        }
    }

    inner class DoWorkoutPagerAdapter : androidx.viewpager.widget.PagerAdapter() {

        override fun isViewFromObject(convertView: View, anyObject: Any): Boolean {
            return convertView === anyObject as RelativeLayout
        }

        override fun getCount(): Int {
            return arrDayExTableClass.size
        }

        private fun getItem(pos: Int): DayExTableClass {
            return arrDayExTableClass[pos]
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val item = getItem(position)
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.row_start_workout, container, false)
            val txtWorkoutTitle: TextView = itemView.findViewById(R.id.txtWorkoutTitle)
            val txtWorkoutDetails: TextView = itemView.findViewById(R.id.txtWorkoutDetails)
            val viewfliperWorkout: ViewFlipper = itemView.findViewById(R.id.viewfliperWorkout)

            try {
                if (boolIsReadyToGo) {
                    txtWorkoutTitle.text = ""
                    txtWorkoutDetails.text = ""
                } else {
                    if (item.exUnit != CommonString.workout_type_second) {
                        txtWorkoutTitle.text = "X ".plus(item.exTime)
                    } else {
                        txtWorkoutTitle.text = item.exTime+" s"
                    }

                    txtWorkoutDetails.text = item.exName
                }

                viewfliperWorkout.removeAllViews()
                val listImg = Utils.getAssetItems(mContext, Utils.ReplaceSpacialCharacters(item.exPath))

                for (i in 0 until listImg.size) {
                    val imgview = ImageView(mContext)
                    Glide.with(mContext).load(listImg[i]).into(imgview)
                    imgview.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

                    viewfliperWorkout.addView(imgview)
                }

                viewfliperWorkout.isAutoStart = true
                viewfliperWorkout.flipInterval = mContext.resources.getInteger(R.integer.viewfliper_animation)
                viewfliperWorkout.startFlipping()

                container.addView(itemView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as RelativeLayout)
        }
    }

    private fun getElapsedTimeSecs(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - exStartTime) / 1000 % 60
        }
        return elapsed
    }

    private fun getElapsedTimeMin(): Long {
        var elapsed: Long = 0
        if (running) {
            elapsed = (System.currentTimeMillis() - exStartTime) / 1000 / 60 % 60
        }
        return elapsed
    }

    private fun confirmToExitDialog() {
        flagTimerPause = true
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_exercise_exit)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val imgbtnClose = dialog.findViewById(R.id.imgbtnClose) as ImageButton
        val btnQuite = dialog.findViewById(R.id.btnQuite) as Button
        val btnContinue = dialog.findViewById(R.id.btnContinue) as Button
        //val tvComebackMessage = dialog.findViewById(R.id.tvComebackMessage) as TextView

        imgbtnClose.setOnClickListener {
            try {
                flagTimerPause = false
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnQuite.setOnClickListener {
            saveData(true)
            try {
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnContinue.setOnClickListener {
            try {
                flagTimerPause = false
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.setOnCancelListener {
            flagTimerPause = false
        }

        dialog.show()
    }

    override fun onStop() {
        Log.e("TAG", "onStop::::::::: ")
        saveData(false)
        super.onStop()
    }

    private fun saveData(isCkeck: Boolean) {

        try {
            val calValue = CommonString.secDurationCal * CommonUtility.timeToSecond(txtTimer.text.toString())

            val dbHelper = DataHelper(this)

                /*if (isCkeck) {
                    dbHelper.addHistory(
                        arrDayExTableClass[0].planId,
                        dbHelper.getPlanNameByPlanId(arrDayExTableClass[0].planId),
                        CommonUtility.getCurrentTimeStamp(),
                        CommonUtility.timeToSecond(txtTimer.text.toString()).toString(),
                        CommonUtility.getStringFormat(calValue),
                        arrDayExTableClass.size.toString(),
                        LocalDB.getLastInputWeight(this).toString(),
                        LocalDB.getLastInputFoot(this).toString(),
                        LocalDB.getLastInputInch(this).toString(),
                        "0",
                        dbHelper.getPlanDayNameByDayId(arrDayExTableClass[0].dayId)
                    )
                }*/

            LocalDB.setLastUnCompletedExPos(this, arrDayExTableClass[0].planId.toInt(), arrDayExTableClass[0].dayId, viewPagerWorkout.currentItem)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isCkeck) {
            finish()
        }
    }

    override fun adLoadingFailed() {
        nextActivityStart()
    }

    override fun adClose() {
        nextActivityStart()
    }

    override fun startNextScreen() {
        nextActivityStart()
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
