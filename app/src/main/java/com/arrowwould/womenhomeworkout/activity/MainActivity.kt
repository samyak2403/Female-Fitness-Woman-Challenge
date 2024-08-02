package com.arrowwould.womenhomeworkout.activity

import android.content.Context
import android.content.IntentSender
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.utils.Utils
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.adapter.ViewPagerAdapter
import com.arrowwould.womenhomeworkout.fragment.MyTrainingFragment
import com.arrowwould.womenhomeworkout.fragment.ProfileFragment
import com.arrowwould.womenhomeworkout.fragment.ReportFragment
import com.arrowwould.womenhomeworkout.fragment.RoutinesFragments
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), CallbackListener {

    var appUpdateManager: AppUpdateManager? = null
    var update: Context? = null
    lateinit var activity : MainActivity
    private var utils: Utils? = null

    private lateinit var vPagerAdapter: ViewPagerAdapter
    private var selectedPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initAction()
        subScribeToFirebaseTopic()

        update = also { activity = it }


        appUpdateManager = AppUpdateManagerFactory.create(update as MainActivity)


    }

    override fun onBackPressed() {
        openExitConfirm()
//        super.onBackPressed()
    }

    private fun init() {
        setupViewPager()
    }

    private fun initAction() {

        vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> onClick(llMyTrainingPlan)
                    1 -> onClick(llRoutines)
                    2 -> onClick(llReports)
                    3 -> onClick(llMines)
                }
            }
        })

    }

    private fun subScribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("female_fitness_topic")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("subScribeFirebaseTopic", ": Fail")
                } else {
                    Log.e("subScribeFirebaseTopic", ": Success")
                }
            }
    }

    private fun setupViewPager() {
        vPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        vPagerAdapter.addFrag(MyTrainingFragment(), getString(R.string.my_training_plan))
        vPagerAdapter.addFrag(RoutinesFragments(), getString(R.string.routines))
        vPagerAdapter.addFrag(ReportFragment(), getString(R.string.report))
        vPagerAdapter.addFrag(ProfileFragment(), getString(R.string.mine))

        vPager.adapter = vPagerAdapter
        vPager.offscreenPageLimit = vPagerAdapter.count
        vPager.currentItem = selectedPage

        onClick(llMyTrainingPlan)
    }



    fun onClick(view: View) {
        try {

            iconDeselect(ivMyTrainingPlan)
            iconDeselect(ivRoutines)
            iconDeselect(ivReports)
            iconDeselect(ivMines)

            textDeselect(tvMyTrainingPlan)
            textDeselect(tvRoutines)
            textDeselect(tvReports)
            textDeselect(tvMines)

            when (view.id) {
                R.id.llMyTrainingPlan, R.id.ivMyTrainingPlan, R.id.tvMyTrainingPlan -> {

                    iconSelect(ivMyTrainingPlan)
                    textSelect(tvMyTrainingPlan)
                    selectedPage = 0
                }
                R.id.llRoutines, R.id.ivRoutines, R.id.tvRoutines -> {
                    iconSelect(ivRoutines)
                    textSelect(tvRoutines)
                    selectedPage = 1
                }
                R.id.llReports, R.id.ivReports, R.id.tvReports -> {
                    iconSelect(ivReports)
                    textSelect(tvReports)
                    selectedPage = 2
                }
                R.id.llMines, R.id.ivMines, R.id.tvMines -> {
                    iconSelect(ivMines)
                    textSelect(tvMines)
                    selectedPage = 3
                }
            }

            vPager.currentItem = selectedPage
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun iconSelect(imageView: AppCompatImageView) {
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.blue_theme_light), android.graphics.PorterDuff.Mode.SRC_IN)

    }

    private fun iconDeselect(imageView: AppCompatImageView) {
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.green_theme), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun textSelect(textView: AppCompatTextView) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.blue_theme_light))
    }

    private fun textDeselect(textView: AppCompatTextView) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.green_theme))
    }

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }

    override fun onSuccess() {
    }

    override fun onCancel() {
    }

    override fun onRetry() {
    }


    fun UpdateApp() {
        try {
            val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    try {
                        appUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, activity, 101
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }.addOnFailureListener { e: java.lang.Exception -> e.printStackTrace() }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        appUpdateManager!!.registerListener(listener)
    }

    var listener =
        InstallStateUpdatedListener { installState: InstallState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popUp()
            }
        }


    private fun popUp() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), "App Update Almost Done", Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            "Reload"
        ) { appUpdateManager!!.completeUpdate() }
        snackbar.setTextColor(Color.parseColor("#FF0000"))
        snackbar.show()
    }

}
