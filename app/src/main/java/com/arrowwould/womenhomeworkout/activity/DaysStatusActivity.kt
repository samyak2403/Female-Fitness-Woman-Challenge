package com.arrowwould.womenhomeworkout.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.adapter.WeeklyDayStatusAdapter
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.google.android.material.appbar.AppBarLayout
import com.utillity.db.DataHelper
import com.utillity.objects.CommonConstantAd
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.pojo.PlanTableClass
import com.utillity.pojo.pWeeklyDayData
import kotlinx.android.synthetic.main.activity_days_status.*
import kotlinx.android.synthetic.main.activity_days_status.flexible_example_appbar
import kotlinx.android.synthetic.main.activity_days_status.ivCategory

class DaysStatusActivity : BaseActivity(), CallbackListener{

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
        init()
        initAction()
    }

    private var arrWeeklyDayStatus = ArrayList<pWeeklyDayData>()
    lateinit var dbHelper: DataHelper
    private lateinit var planTableClass: PlanTableClass
    private lateinit var llOption: RelativeLayout
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout
    private lateinit var txtTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_status)


        setSmallNativeAdd()

        dbHelper = DataHelper(this)
        llOption = findViewById(R.id.llOption)
        llAdView = findViewById(R.id.llAdView)
        llAdViewFacebook = findViewById(R.id.llAdViewFacebook)
        txtTitle = findViewById(R.id.txtTitle)

        try {
            planTableClass = intent.getSerializableExtra(CommonString.extra_plan_table_class) as PlanTableClass
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e("TAG", "onCreate:::PlaText:::  ${planTableClass.planName}" )
        txtTitle.text = planTableClass.planName

        var isShow = true
        var scrollRange = -1
        flexible_example_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
                Log.e("TAG", "onCreate::::1111 ")
            }
            if (scrollRange + verticalOffset == 0) {
                /*status bar colortheme*/
                llOption.visibility = View.GONE
                isShow = true
                Log.e("TAG", "onCreate:::::222222 ")
            } else if (isShow) {
                /*status bar transparant*/
                llOption.visibility = View.VISIBLE
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                isShow = false
                Log.e("TAG", "onCreate:::::33333 ")
            }
        })

        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            CommonConstantAd.loadBannerGoogleAd(this, llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_TYPE_AD)
        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            CommonConstantAd.loadFbAdFacebook(this, llAdViewFacebook, Constant.FB_BANNER, Constant.FB_BANNER_TYPE_AD)
        }

    }

    private fun init() {

        try {

            arrWeeklyDayStatus = dbHelper.getWorkoutWeeklyData(planTableClass.planName)

            Glide.with(this).load("///android_asset/" + planTableClass.planImage).centerCrop().into(ivCategory)

            rcyDaysName.adapter = WeeklyDayStatusAdapter(this, arrWeeklyDayStatus, planTableClass)

            CommonUtility.setDayProgressData(this, planTableClass.planId, txtDayLeft, txtDayPer, pbDay)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initAction() {

        imgBack.setOnClickListener { finish() }

        imgMore.setOnClickListener {
        }

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
