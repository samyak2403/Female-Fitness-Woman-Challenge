package com.arrowwould.womenhomeworkout.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.adapter.HistoryTitleAdapter
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.compactcalender.CompactCalendarView
import com.utillity.compactcalender.Event
import com.utillity.db.DataHelper
import com.utillity.objects.CommonConstantAd
import com.utillity.objects.CommonUtility
import kotlinx.android.synthetic.main.activity_history.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : BaseActivity(), CallbackListener {

    override fun onBackPressed() {
        super.onBackPressed()

//        startActivity(Intent(this, ReportActivity::class.java))

    }
    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    private lateinit var context: Context
    private lateinit var dbHelper: DataHelper
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout
    private val dateFormatForMonth = SimpleDateFormat("MMM - yyyy", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DataHelper(this)
        context = this

        init()

        initAction()
    }

    private fun init() {
//        CommonConstantAd.loadBannerAd(this, adView)
        llAdView = findViewById(R.id.llAdView)
        llAdViewFacebook = findViewById(R.id.llAdViewFacebook)

//        CommonConstantAd.loadBannerGoogleAd(this, llAdView,Constant.GOOGLE_BANNER,"Banner")


        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            CommonConstantAd.loadBannerGoogleAd(context!!,llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_TYPE_AD)
        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            CommonConstantAd.loadFbAdFacebook(context!!,llAdViewFacebook, Constant.FB_BANNER, Constant.FB_BANNER_TYPE_AD)
        }


        compactCalendarSetup()


        val arrHistoryData = dbHelper.getWeekDayOfHistory()
        rcyHistoryList.adapter = HistoryTitleAdapter(this, arrHistoryData)

    }

    private fun initAction() {
        imgBack.setOnClickListener { onBackPressed() }

        imgbtnMonthPrev.setOnClickListener {
            CompatCalenderView.scrollLeft()
        }

        imgbtnMonthNext.setOnClickListener {
            CompatCalenderView.scrollRight()
        }
    }

    private fun compactCalendarSetup() {

        CompatCalenderView.removeAllEvents()
        CompatCalenderView.shouldScrollMonth(false)

        tvMonthYear.text = dateFormatForMonth.format(Calendar.getInstance().time)

        val arrCompleteExerciseDt: ArrayList<String> = dbHelper.getCompleteExerciseDate()

        for (i in 0 until arrCompleteExerciseDt.size) {
            addEvents(CommonUtility.getFullDateStringToMilliSecond(arrCompleteExerciseDt[i]))
        }

        CompatCalenderView.setListener(object : CompactCalendarView.CompactCalendarViewListener {

            override fun onDayClick(dateClicked: Date?) {

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                tvMonthYear.text = dateFormatForMonth.format(firstDayOfNewMonth!!)
            }
        })

    }

    private fun addEvents(timeInMillis: Long) {
        val currentCalender = Calendar.getInstance(Locale.ENGLISH)
        currentCalender.time = Date()

        CompatCalenderView.addEvent(Event(Color.argb(255, 255, 112, 145), timeInMillis, "Event at " + Date(timeInMillis)))

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
