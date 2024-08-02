package com.arrowwould.womenhomeworkout.activity

import android.content.Intent
import android.os.Bundle
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.objects.CommonString
import com.utillity.objects.LocalDB
import kotlinx.android.synthetic.main.activity_setup_plan.*

class SetupPlanActivity : BaseActivity(), CallbackListener {


    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }



    private var strPlanType = CommonString.PlanAbs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_plan)

        init()
        initAction()

    }

    private fun init() {

    }

    private fun initAction() {

        rbAbs.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LocalDB.setSetupPlan(this, CommonString.PlanAbs)
                strPlanType = CommonString.PlanAbs
                rbButt.isChecked = false
                rbThigh.isChecked = false
            }
        }

        rbButt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LocalDB.setSetupPlan(this, CommonString.PlanButt)
                strPlanType = CommonString.PlanButt
                rbAbs.isChecked = false
                rbThigh.isChecked = false
            }
        }

        rbThigh.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LocalDB.setSetupPlan(this, CommonString.PlanThigh)
                strPlanType = CommonString.PlanThigh
                rbButt.isChecked = false
                rbAbs.isChecked = false
            }
        }

        btnNext.setOnClickListener {
            val intent = Intent(this, WeeklyGoalSetActivity::class.java)
            intent.putExtra(CommonString.extra_is_from, CommonString.DEF_SETUP_PLAN_SCREEN)
            startActivity(intent)
        }

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
