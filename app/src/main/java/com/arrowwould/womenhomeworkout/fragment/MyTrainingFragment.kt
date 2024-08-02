package com.arrowwould.womenhomeworkout.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.WeeklyGoalSetActivity
import com.arrowwould.womenhomeworkout.adapter.WeekDayReportAdapter
import com.arrowwould.womenhomeworkout.adapter.WorkoutCategoryAdapter
import com.arrowwould.womenhomeworkout.ConstantAd
import com.utillity.db.DataHelper
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.objects.LocalDB
import com.utillity.pojo.PlanTableClass
import kotlinx.android.synthetic.main.fragment_my_training.*
import kotlinx.android.synthetic.main.fragment_my_training.view.*
import kotlin.math.roundToInt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyTrainingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        setCategoryData()
        setupWeekTopData()
    }

    private lateinit var rootView: View
    private lateinit var mContext: Context
    private lateinit var dbHelper: DataHelper
    private lateinit var rvWorkoutCategory: RecyclerView
    private lateinit var rcyHistoryWeek: RecyclerView

    private lateinit var txtTotalWorkouts: TextView
    private lateinit var txtTotalKcal: TextView
    private lateinit var txtTotalMinutes: TextView
    private lateinit var txtWeekStatus: TextView

    private lateinit var rlReport: RelativeLayout
    private lateinit var llReport: LinearLayout
    private lateinit var llHistory: LinearLayout
    var adapter: WorkoutCategoryAdapter? = null

    private var arrPlanList = ArrayList<PlanTableClass>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_my_training, container, false)
        mContext = rootView.context

        dbHelper = DataHelper(mContext)

        init()
        initAction()

        return rootView
    }

    private fun init() {
        rvWorkoutCategory = rootView.findViewById(R.id.rvWorkoutCategory)
        rcyHistoryWeek = rootView.findViewById(R.id.rcyHistoryWeek)

        txtTotalWorkouts = rootView.findViewById(R.id.txtTotalWorkouts)
        txtTotalKcal = rootView.findViewById(R.id.txtTotalKcal)
        txtTotalMinutes = rootView.findViewById(R.id.txtTotalMinutes)
        txtWeekStatus = rootView.findViewById(R.id.txtWeekStatus)

        rlReport = rootView.findViewById(R.id.rlReport)

        llReport = rootView.findViewById(R.id.llReport)
        llHistory = rootView.findViewById(R.id.llHistory)

//        CommonConstantAd.loadBannerAd(context!!, adView)


//        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
//            CommonConstantAd.loadBannerGoogleAd(requireContext(), llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_TYPE_AD)
//        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
//            CommonConstantAd.loadFbAdFacebook(requireContext(), llAdViewFacebook, Constant.FB_BANNER, Constant.FB_BANNER_TYPE_AD)
//        }

        ConstantAd.setBannerAdd(rootView.adMobView,requireContext())


//        CommonConstantAd.loadBannerGoogleAd(context!!,llAdView,Constant.GOOGLE_BANNER,"Banner")

    }

    private fun initAction() {
        txtWeekStatus.setOnClickListener {
            val intent = Intent(mContext, WeeklyGoalSetActivity::class.java)
            mContext.startActivity(intent)
            (mContext as AppCompatActivity).overridePendingTransition(R.anim.slide_up, R.anim.none)
        }
    }

    private fun setCategoryData() {
        arrPlanList = ArrayList()

        arrPlanList = dbHelper.getPlanList(CommonString.PlanTypeWorkout)

        if (adapter == null)
            adapter = WorkoutCategoryAdapter(mContext, arrPlanList)
        else
            adapter!!.addAll(arrPlanList)
        rvWorkoutCategory.adapter = adapter



    }

    private fun setupWeekTopData() {

        try {
            txtTotalWorkouts.text = dbHelper.getHistoryTotalWorkout().toString()
            txtTotalKcal.text = dbHelper.getHistoryTotalKCal().toInt().toString()
            txtTotalMinutes.text = ((dbHelper.getHistoryTotalMinutes() / 60).toDouble()).roundToInt().toString()

            val arrCurrentWeek = CommonUtility.getCurrentWeek()
            var completedWeekDay = 0
            for (pos in 0 until arrCurrentWeek.size) {
                if (dbHelper.isHistoryAvailable(CommonUtility.convertFullDateToDate(arrCurrentWeek[pos]))) {
                    completedWeekDay++
                }
            }

            val weekDayGoal = LocalDB.getWeekGoalDay(mContext)

            txtWeekStatus.text = HtmlCompat.fromHtml(
                "<font color='${ContextCompat.getColor(mContext, R.color.colorTheme)}'>$completedWeekDay</font>/$weekDayGoal",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            rcyHistoryWeek.adapter = WeekDayReportAdapter(mContext, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
