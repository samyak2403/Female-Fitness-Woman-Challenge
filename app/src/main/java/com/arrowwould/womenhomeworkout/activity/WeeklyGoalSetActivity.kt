package com.arrowwould.womenhomeworkout.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.objects.CommonDialog
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.objects.LocalDB
import kotlinx.android.synthetic.main.activity_weekly_goal_set.*

class WeeklyGoalSetActivity : BaseActivity(), CallbackListener {

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.slide_down)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_goal_set)

        init()
        initAction()

    }

    private fun init() {
        try {
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_setting_sec_arrow)
            drawable?.let { myDrawable ->
                DrawableCompat.setTint(myDrawable, ContextCompat.getColor(this, R.color.colorWhite))
                edWeekDays.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, myDrawable, null)
                edFirstDayOfWeek.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, myDrawable, null)
            }

            edWeekDays.setText(LocalDB.getWeekGoalDay(this).toString())
            edFirstDayOfWeek.setText(CommonUtility.getFirstWeekDayNameByDayNo(LocalDB.getFirstDayOfWeek(this)))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAction() {

        imgBack.setOnClickListener { onBackPressed() }

        btnSave.setOnClickListener {

            try {
                LocalDB.setWeekGoalDay(this, edWeekDays.text.toString().toInt())
                LocalDB.setFirstDayOfWeek(this, CommonUtility.getFirstWeekDayNoByDayName(edFirstDayOfWeek.text.toString()))

                if (intent.hasExtra(CommonString.extra_is_from)) {
                    if (intent.getStringExtra(CommonString.extra_is_from) == CommonString.DEF_SETUP_PLAN_SCREEN) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        LocalDB.setIsFirstTime(this, false)
                        finishAffinity()
                    }
                } else {
                    onBackPressed()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        edWeekDays.setOnClickListener { CommonDialog.setWeekDayGoalDialog(this, edWeekDays) }

        edFirstDayOfWeek.setOnClickListener { CommonDialog.setFirstWeekDayDialog(this, edFirstDayOfWeek) }

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
