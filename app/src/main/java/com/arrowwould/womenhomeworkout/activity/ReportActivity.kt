package com.arrowwould.womenhomeworkout.activity

import android.os.Bundle
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.fragment.ReportFragment
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : BaseActivity(), CallbackListener {

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        initAction()
        setReportFragment()

    }

    private fun initAction(){
        imgBack.setOnClickListener { onBackPressed() }
    }

    private fun setReportFragment(){
        try {
            val fragment = supportFragmentManager.beginTransaction()
            fragment.add(R.id.llContainer, ReportFragment(), getString(R.string.my_training_plan))
            fragment.disallowAddToBackStack()
            fragment.commit()
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

}
