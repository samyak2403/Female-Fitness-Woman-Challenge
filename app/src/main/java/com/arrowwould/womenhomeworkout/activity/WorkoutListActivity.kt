package com.arrowwould.womenhomeworkout.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.arrowwould.womenhomeworkout.MyInterstitialAds_abc
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.adapter.DayExListAdapter
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.arrowwould.womenhomeworkout.utils.ApplovinAds
import com.utillity.db.DataHelper
import com.utillity.objects.CommonString
import com.utillity.pojo.DayExTableClass
import com.utillity.pojo.PlanTableClass
import kotlinx.android.synthetic.main.activity_workout_list.*
import java.io.Serializable


class WorkoutListActivity : BaseActivity(), CallbackListener ,
    MyInterstitialAds_abc.OnInterstitialAdListnear {

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    private var CDInstrialAds: MyInterstitialAds_abc? = null
    private lateinit var planTableClass: PlanTableClass
    private lateinit var dbHelper: DataHelper
    private lateinit var arrDayExTableClass: ArrayList<DayExTableClass>
    private var currentMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list)

        ApplovinAds.loadInterstitialAd(this)

        CDInstrialAds = MyInterstitialAds_abc(this, this)


        dbHelper = DataHelper(this)

        try {
            planTableClass = intent.getSerializableExtra(CommonString.extra_plan_table_class) as PlanTableClass
        } catch (e: Exception) {
            e.printStackTrace()
        }

        init()
        initAction()

        setSmallNativeAdd()


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

//                toolbar.setTitleMargin(0,0,0,0)

//                flexible_example_collapsing.setPadding(0,0,0,0)
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                window.statusBarColor = resources.getColor(R.color.colorTheme)

                /*val layoutParams: CollapsingToolbarLayout.LayoutParams = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
                layoutParams.height = 100
                toolbar.layoutParams = layoutParams*/



                isShow = true
                Log.e("TAG", "onCreate:::::222222 ")
            } else if (isShow) {
                /*status bar transparant*/
                llOption.visibility = View.VISIBLE
//                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                isShow = false
                Log.e("TAG", "onCreate:::::33333 ")
            }
        })
    }

    fun Activity.transparentStatusAndNavigation(systemUiScrim: Int = Color.parseColor("#40000000")) {
        var systemUiVisibility = 0
        // Use a dark scrim by default since light status is API 23+
        var statusBarColor = systemUiScrim
        //  Use a dark scrim by default since light nav bar is API 27+
//        var navigationBarColor = systemUiScrim
        val winParams = window.attributes


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            navigationBarColor = Color.TRANSPARENT
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            systemUiVisibility = systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.decorView.systemUiVisibility = systemUiVisibility
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            winParams.flags = winParams.flags and
                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION).inv()
            window.statusBarColor = statusBarColor
//            window.navigationBarColor = navigationBarColor
        }

        window.attributes = winParams
    }

    private fun init() {

        Glide.with(this).load("///android_asset/" + planTableClass.planImage).centerCrop().into(ivCategory)

        displayMode(false)

        try {
            txtTitle.text = planTableClass.planName
            tvExCount.text = arrDayExTableClass.size.toString()
            tvKcal.text = planTableClass.planKcal
            tvMinutes.text = planTableClass.planMinutes
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (planTableClass.planLvl) {
            CommonString.PlanLvlBeginner -> {
                ivExLevel.setImageResource(R.drawable.ic_beginner_level)
            }
            CommonString.PlanLvlIntermediate -> {
                ivExLevel.setImageResource(R.drawable.ic_intermediate_level)
            }
            CommonString.PlanLvlAdvanced -> {
                ivExLevel.setImageResource(R.drawable.ic_advanced_level)
            }
            else -> {
                ivExLevel.setImageResource(0)
            }
        }

    }

    private fun initAction() {

        ibvBack.setOnClickListener { onBackPressed() }



        btnStart.setOnClickListener {
            try {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra(CommonString.extra_exercise_list, arrDayExTableClass as Serializable)
                startActivity(intent)
                interAd()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finish()
        }

        btnSave.setOnClickListener {
            displayMode(false)
        }

    }


    private fun displayMode(isEdited: Boolean) {

        try {
            currentMode = isEdited

            if (isEdited) {
                btnStart.visibility = View.GONE
                btnSave.visibility = View.VISIBLE
            } else {
                btnStart.visibility = View.VISIBLE
                btnSave.visibility = View.GONE
            }

            if (planTableClass.planDays == CommonString.PlanDaysYes) {
                val dayId = intent.getStringExtra(CommonString.extra_day_id)
                if (dayId != null) {
                    arrDayExTableClass = dbHelper.getDayExList(dayId)
                }
            } else {
                arrDayExTableClass = dbHelper.getSingleDayExList(planTableClass.planId)
            }

            rvExerciseList.adapter = DayExListAdapter(this, arrDayExTableClass, isEdited)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == CommonString.reqCodeExEdit && resultCode == RESULT_OK) {
                displayMode(currentMode)
            } else if (requestCode == CommonString.reqCodeExEditTime && resultCode == RESULT_OK) {
                displayMode(currentMode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }


     fun interAd() {
        if (sessionManager!!.getBooleanValue(Constant.Adshow)) {
            CDInstrialAds!!.showAds()
        } else {
            onAdFail()
        }
    }


    override fun onAdClosed() {
        finish()
    }

    override fun onAdFail() {
        finish()
    }

}
