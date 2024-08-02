package com.arrowwould.womenhomeworkout.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.objects.CommonConstantAd
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.objects.Utils
import com.utillity.pojo.DayExTableClass
import com.utillity.pojo.ExTableClass
import kotlinx.android.synthetic.main.activity_workout_list_information.*

class WorkoutListInfoActivity : BaseActivity(), CallbackListener {

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.slide_down)
    }

    lateinit var workOutCategoryData: ArrayList<DayExTableClass>
    lateinit var arrExTableClass: ArrayList<ExTableClass>
    lateinit var mContext: Context
    private var currentPos: Int = 0
    private var typeOfControl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list_information)

        mContext = this

        try {
            typeOfControl = intent.getStringExtra(CommonString.extra_workout_details_type) as String
            currentPos = intent.getIntExtra(CommonString.extra_workout_list_pos, 0)

            if (typeOfControl == CommonString.val_is_day_ex_list_edit_adapter) {
                arrExTableClass = intent.getSerializableExtra(CommonString.extra_exercise_list) as ArrayList<ExTableClass>
            } else {
                workOutCategoryData = intent.getSerializableExtra(CommonString.extra_exercise_list) as ArrayList<DayExTableClass>
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        defaultSetup()

        initAction()

        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            CommonConstantAd.loadBannerGoogleAd(this,llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_TYPE_AD)
        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            CommonConstantAd.loadFbAdFacebook(this,llAdViewFacebook, Constant.FB_BANNER, Constant.FB_BANNER_TYPE_AD)
        }

    }



    private fun defaultSetup() {

        val doWorkOutPgrAdpt = DoWorkoutPagerAdapter()
        viewPagerWorkoutDetails.adapter = doWorkOutPgrAdpt
        viewPagerWorkoutDetails.currentItem = currentPos
        viewPagerWorkoutDetails.offscreenPageLimit = 5
        imgbtnDone.text = (1 + currentPos).toString().plus(" / ").plus(workOutCategoryData.size)

        when (typeOfControl) {
            CommonString.val_is_workout -> {
                rltBottomControl.visibility = View.GONE
            }
            CommonString.val_is_day_ex_list_adapter -> {
                tvExTime.text = workOutCategoryData[currentPos].exTime
                rltBottomControl.visibility = View.VISIBLE
                llSave.visibility = View.VISIBLE
                llReplace.visibility = View.GONE
            }

            CommonString.val_is_day_ex_list_edit_adapter -> {

                try {
                    if (arrExTableClass[currentPos].exUnit == resources.getString(R.string.s)) {
                        tvExTime.text = CommonUtility.timeToSecond(intent.getStringExtra(CommonString.extra_replace_time)!!.trim()).toString()
                    } else {
                        tvExTime.text = intent.getStringExtra(CommonString.extra_replace_time)
                    }

                    rltBottomControl.visibility = View.VISIBLE
                    llSave.visibility = View.GONE
                    llReplace.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    }

    private fun initAction() {

        imgbtnBack.setOnClickListener {
            onBackPressed()
        }

        imgbtnNext.setOnClickListener {
            try {
                if (viewPagerWorkoutDetails.currentItem < workOutCategoryData.size)
                    viewPagerWorkoutDetails.currentItem = viewPagerWorkoutDetails.currentItem + 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        imgbtnPrev.setOnClickListener {
            try {
                if (viewPagerWorkoutDetails.currentItem > 0)
                    viewPagerWorkoutDetails.currentItem = viewPagerWorkoutDetails.currentItem - 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        imgbtnVideo.setOnClickListener {
            try {
                val strVideoLink: String
                if (typeOfControl == CommonString.val_is_day_ex_list_edit_adapter) {
                    strVideoLink = arrExTableClass[viewPagerWorkoutDetails.currentItem].exVideo
                } else {
                    strVideoLink = workOutCategoryData[viewPagerWorkoutDetails.currentItem].exVideo
                }

                if (strVideoLink != "") {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(strVideoLink)))
                } else {
                    Toast.makeText(this, getString(R.string.error_video_not_exist), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewPagerWorkoutDetails.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageSelected(pos: Int) {
                imgbtnDone.text = (1 + pos).toString().plus(" / ").plus(workOutCategoryData.size)
            }
        })



    }

    inner class DoWorkoutPagerAdapter : androidx.viewpager.widget.PagerAdapter() {

        override fun isViewFromObject(convertView: View, anyObject: Any): Boolean {
            return convertView === anyObject as RelativeLayout
        }

        override fun getCount(): Int {
            if (typeOfControl == CommonString.val_is_day_ex_list_edit_adapter) {
                return arrExTableClass.size
            } else {
                return workOutCategoryData.size
            }

        }

        private fun getItem(pos: Int): DayExTableClass {
            return workOutCategoryData[pos]
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var item: DayExTableClass? = null
            var item1: ExTableClass? = null
            var listImg: ArrayList<String> = ArrayList()

            if (typeOfControl == CommonString.val_is_day_ex_list_edit_adapter) {
                item1 = arrExTableClass[position]
            } else {
                item = workOutCategoryData[position]
            }

            val itemView = LayoutInflater.from(mContext).inflate(R.layout.row_workout_info, container, false)
            val txtWorkoutTitle: TextView = itemView.findViewById(R.id.txtWorkoutTitle)
            val txtWorkoutDetails: TextView = itemView.findViewById(R.id.txtWorkoutDetails)
            val viewfliperWorkout: ViewFlipper = itemView.findViewById(R.id.imgWorkoutDemo)

            try {
                if (typeOfControl == CommonString.val_is_day_ex_list_edit_adapter) {
                    if (item1 != null) {
                        txtWorkoutTitle.text = item1.exName
                        txtWorkoutDetails.text = item1.exDescription.replace("\\n", System.getProperty("line.separator")!!).replace("\\r", "")
                        listImg = Utils.getAssetItems(mContext, item1.exPath)
                    }
                } else {
                    if (item != null) {
                        txtWorkoutTitle.text = item.exName
                        txtWorkoutDetails.text = item.exDescription.replace("\\n", System.getProperty("line.separator")!!).replace("\\r", "")
                        listImg = Utils.getAssetItems(mContext, item.exPath)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            viewfliperWorkout.removeAllViews()

            try {
                for (i in 0 until listImg.size) {
                    val imgview = ImageView(mContext)
                    Glide.with(mContext).load(listImg[i]).into(imgview)

                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.gravity = Gravity.START
                    imgview.layoutParams = layoutParams

                    viewfliperWorkout.addView(imgview)
                }

                viewfliperWorkout.isAutoStart = true
                viewfliperWorkout.flipInterval = mContext.resources.getInteger(R.integer.viewfliper_animation)
                viewfliperWorkout.startFlipping()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as RelativeLayout)
        }
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
