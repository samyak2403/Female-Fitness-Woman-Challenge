package com.arrowwould.womenhomeworkout.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.adapter.WorkoutCategoryAdapter
import com.google.android.gms.ads.AdView
import com.arrowwould.womenhomeworkout.ConstantAd
import com.utillity.db.DataHelper
import com.utillity.objects.CommonString
import com.utillity.pojo.PlanTableClass
import kotlinx.android.synthetic.main.fragment_my_training.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoutinesFragments : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var rootView: View
    private lateinit var mContext: Context
    private lateinit var dbHelper: DataHelper
    private lateinit var adView: AdView
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout


    private var arrPlanList = ArrayList<PlanTableClass>()

    private lateinit var rvRoutine: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_routines, container, false)
        mContext = rootView.context
        dbHelper = DataHelper(mContext)

        init()
        initAction()

        return rootView
    }

    private fun init() {
        rvRoutine = rootView.findViewById(R.id.rvRoutine)


        setRoutinePlan()

        ConstantAd.setBannerAdd(rootView.adMobView,requireContext())

    }

    private fun initAction() {

    }

    private fun setRoutinePlan() {
        arrPlanList = ArrayList()

        arrPlanList = dbHelper.getPlanList(CommonString.PlanTypeRoutines)
        rvRoutine.adapter = WorkoutCategoryAdapter(mContext, arrPlanList)

    }

}
