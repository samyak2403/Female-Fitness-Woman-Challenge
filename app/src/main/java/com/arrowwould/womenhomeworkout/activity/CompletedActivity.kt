package com.arrowwould.womenhomeworkout.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import com.arrowwould.womenhomeworkout.MyInterstitialAds_abc
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.common.Constant
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.db.DataHelper
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.objects.LocalDB
import com.utillity.pojo.DayExTableClass
import kotlinx.android.synthetic.main.activity_completed.*
import kotlin.math.roundToInt

class CompletedActivity : BaseActivity(), CallbackListener ,
    MyInterstitialAds_abc.OnInterstitialAdListnear{

    override fun onBackPressed() {
        saveData()
        if (sessionManager!!.getBooleanValue(Constant.Adshow)) {
            CDInstrialAds!!.showAds()
        } else {
            onAdFail()
        }
    }

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    private var CDInstrialAds: MyInterstitialAds_abc? = null
    private lateinit var txtTotalNoOfLevel: TextView
    private lateinit var txtDurationTime: TextView

    private lateinit var context: Context
    private lateinit var pWorkoutList: ArrayList<DayExTableClass>

    private var calValue = 0.0
    private var feelRate = "0"
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout
    private var strDayName: String = ""
    private lateinit var dbHelper: DataHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        CDInstrialAds = MyInterstitialAds_abc(this, this)
        context = this
        dbHelper = DataHelper(context)

        defaultSetup()
        initAction()

        setBannerAdd(adMobView)

    }

    private fun defaultSetup() {
        //llAdView = findViewById(R.id.llAdView)
        //llAdViewFacebook = findViewById(R.id.llAdViewFacebook)
//        CommonConstantAd.loadBannerAd(this, adView)
//        CommonConstantAd.loadBannerGoogleAd(this, llAdView,Constant.GOOGLE_BANNER,"Rec")

//        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
//            CommonConstantAd.loadBannerGoogleAd(context!!,llAdView, Constant.GOOGLE_BANNER, Constant.GOOGLE_BANNER_TYPE_AD)
//        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
//            CommonConstantAd.loadFbAdFacebook(context!!,llAdViewFacebook, Constant.FB_BANNER, Constant.FB_BANNER_TYPE_AD)
//        }

/*
        if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_GOOGLE) {
            CommonConstantAd.loadFullGoogleAd(this,Constant.GOOGLE_INTERSTITIAL)
        } else if (Constant.AD_TYPE_FB_GOOGLE == Constant.AD_FACEBOOK) {
            CommonConstantAd.loadFullFbAd(context!!,llAdViewFacebook, Constant.FB_BANNER, Constant.FB_RECTANGLE_BANNER_TYPE_AD)
        }*/




        txtDurationTime = findViewById(R.id.txtDurationTime)
        txtTotalNoOfLevel = findViewById(R.id.txtTotalNoOfLevel)

        try {
            pWorkoutList = intent.getSerializableExtra(CommonString.extra_exercise_list) as ArrayList<DayExTableClass>
            txtDurationTime.text = intent.getStringExtra("Duration")

            strDayName = dbHelper.getPlanDayNameByDayId(pWorkoutList[0].dayId)

            txtTotalNoOfLevel.text = pWorkoutList.size.toString()

            calValue = CommonString.secDurationCal * CommonUtility.timeToSecond(intent.getStringExtra("Duration")!!)

            txtBurnCaloriesValues.text = CommonUtility.getStringFormat(calValue)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        setWeightValues()

        setBmiCalculation()

    }

    private fun initAction() {

        imgBack.setOnClickListener { onBackPressed() }

        btnDoItAgain.setOnClickListener {

            try {
                val intent1 = intent
                val intent = Intent(context, ExerciseActivity::class.java)
                intent.putExtra(CommonString.extra_exercise_list, intent1.getSerializableExtra(CommonString.extra_exercise_list))
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            onBackPressed()
        }

        btnNext.setOnClickListener { saveData() }

        btnSaveBottom.setOnClickListener { saveData()

            interAd()

        }

        btnShare.setOnClickListener {

            val link = "https://play.google.com/store/apps/details?id=$packageName"
            val strSubject = "Share ${resources.getString(R.string.app_name)} with you"
            val strText =
                " I have finish ${txtTotalNoOfLevel.text} of ${resources.getString(R.string.app_name)} exercise.\n" +
                        "you should start working out at home too. You'll get results in no time! \n" +
                        "Please download the app: $link"

            CommonUtility.shareStringLink(this, strSubject, strText)
        }

        btnEdit.setOnClickListener {
            try {
                setHeightWeightDialog()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtKG.setOnClickListener {
            try {
                if (LocalDB.getWeightUnit(this) != CommonString.DEF_KG && LocalDB.getLastInputWeight(this) != 0f) {
                    edWeight.setText(CommonUtility.getStringFormat((LocalDB.getLastInputWeight(this).toDouble())))
                }

                LocalDB.setWeightUnit(this, CommonString.DEF_KG)
                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtLB.setOnClickListener {

            try {
                if (LocalDB.getWeightUnit(this) != CommonString.DEF_LB && LocalDB.getLastInputWeight(this) != 0f) {
                    edWeight.setText(CommonUtility.getStringFormat((CommonUtility.kgToLb(LocalDB.getLastInputWeight(this).toDouble()))))
                }

                LocalDB.setWeightUnit(this, CommonString.DEF_LB)
                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        edWeight.setOnEditorActionListener { _, actionId, _ ->

            try {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (LocalDB.getWeightUnit(this) != CommonString.DEF_KG) {
                        LocalDB.setLastInputWeight(
                            this,
                            CommonUtility.getStringFormat((CommonUtility.lbToKg(edWeight.text.toString().toDouble()))).toFloat()
                        )
                    } else {
                        LocalDB.setLastInputWeight(this, CommonUtility.getStringFormat((edWeight.text.toString().toDouble())).toFloat())
                    }

                    setBmiCalculation()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }

        rdgFeel.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.rdoFeelOne -> feelRate = "1"
                R.id.rdoFeelTwo -> feelRate = "2"
                R.id.rdoFeelThree -> feelRate = "3"
                R.id.rdoFeelFour -> feelRate = "4"
                R.id.rdoFeelFive -> feelRate = "5"
            }

        }

        txtHideShowBmi.setOnClickListener {
            if (lnyBmiGraphMain.visibility == View.VISIBLE) {
                lnyBmiGraphMain.visibility = View.GONE
                txtHideShowBmi.text = resources.getString(R.string.btn_show)
            } else {
                lnyBmiGraphMain.visibility = View.VISIBLE
                txtHideShowBmi.text = resources.getString(R.string.btn_hide)
            }
        }

        btnFeedBack.setOnClickListener { CommonUtility.rateUs(context) }

    }

    private fun setBmiCalculation() {

        try {
            if (LocalDB.getLastInputWeight(this) != 0f && LocalDB.getLastInputFoot(this) != 0 && LocalDB.getLastInputInch(this).toInt() != 0) {

                val bmiValue = CommonUtility.getBmiCalculation(
                    LocalDB.getLastInputWeight(this),
                    LocalDB.getLastInputFoot(this),
                    LocalDB.getLastInputInch(this).toInt()
                )

                val bmiVal = CommonUtility.calculationForBmiGraph(CommonUtility.getStringFormat((bmiValue)).toFloat())

                val param = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (CommonUtility.getStringFormat((bmiVal.toDouble())).toFloat())
                )

                txtBmiGrade.text = CommonUtility.getStringFormat(bmiValue)
                txtWeightString.text = CommonUtility.bmiWeightString(CommonUtility.getStringFormat((bmiValue)).toFloat())
                txtWeightString.setTextColor(ColorStateList.valueOf(CommonUtility.bmiWeightTextColor(this, bmiValue.toFloat())))
                blankView1.layoutParams = param

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setWeightValues() {

        try {
            if (LocalDB.getWeightUnit(this) == CommonString.DEF_KG && LocalDB.getLastInputWeight(this) != 0f) {
                edWeight.setText(CommonUtility.getStringFormat(LocalDB.getLastInputWeight(this).toDouble()))
                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)
            } else if (LocalDB.getWeightUnit(this) == CommonString.DEF_LB && LocalDB.getLastInputWeight(this) != 0f) {
                edWeight.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(this).toDouble())))
                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setHeightWeightDialog() {

        var boolKg: Boolean
        var boolInch: Boolean

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_weight_height)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val lnyInch = dialog.findViewById(R.id.lnyInch) as LinearLayout

        val txtKG = dialog.findViewById(R.id.txtKG) as TextView
        val txtLB = dialog.findViewById(R.id.txtLB) as TextView
        val txtCM = dialog.findViewById(R.id.txtCM) as TextView
        val txtIN = dialog.findViewById(R.id.txtIN) as TextView

        val edWeight = dialog.findViewById(R.id.edWeight) as EditText
        val edCM = dialog.findViewById(R.id.edCM) as EditText
        val edFeet = dialog.findViewById(R.id.edFeet) as EditText
        val edInch = dialog.findViewById(R.id.edInch) as EditText

        val btnCancel = dialog.findViewById(R.id.btnOkay) as Button
        val btnNext = dialog.findViewById(R.id.btnNext) as Button

        boolKg = true
        try {
            if (LocalDB.getWeightUnit(this) == CommonString.DEF_KG) {

                edWeight.setText(LocalDB.getLastInputWeight(this).toString())

                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)
            } else {
                boolKg = false

                edWeight.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(this).toDouble())))

                txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        boolInch = true
        try {
            if (LocalDB.getHeightUnit(this) == CommonString.DEF_IN) {

                edCM.visibility = View.INVISIBLE
                lnyInch.visibility = View.VISIBLE

                txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtIN.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_gray, null)

                edFeet.setText(LocalDB.getLastInputFoot(this).toString())
                edInch.setText(LocalDB.getLastInputInch(this).toString())
            } else {
                boolInch = false

                edCM.visibility = View.VISIBLE
                lnyInch.visibility = View.INVISIBLE

                txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                txtIN.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                val inch = CommonUtility.ftInToInch(LocalDB.getLastInputFoot(this), LocalDB.getLastInputInch(this).toDouble())

                edCM.setText(CommonUtility.inchToCm(inch).roundToInt().toDouble().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        txtKG.setOnClickListener {
            try {
                if (boolInch) {
                    boolInch = false

                    edCM.visibility = View.VISIBLE
                    lnyInch.visibility = View.INVISIBLE

                    txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                    txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    txtIN.background = resources.getDrawable(R.drawable.ract_gray, null)
                    txtCM.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                    var inch = 0.0
                    if (edFeet.text.toString() != "" && edInch.text.toString() != "") {
                        inch = CommonUtility.ftInToInch(edFeet.text.toString().toInt(), edInch.text.toString().toDouble())
                    } else if (edFeet.text.toString() != "" && edInch.text.toString() == "") {
                        inch = CommonUtility.ftInToInch(edFeet.text.toString().toInt(), 0.0)
                    } else if (edFeet.text.toString() == "" && edInch.text.toString() != "") {
                        inch = CommonUtility.ftInToInch(1, edInch.text.toString().toDouble())
                    }

                    edCM.setText(CommonUtility.inchToCm(inch).roundToInt().toDouble().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (!boolKg) {
                    boolKg = true

                    txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                    txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                    txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)

                    edWeight.hint = "KG"

                    if (edWeight.text.toString() != "") {
                        edWeight.setText(CommonUtility.getStringFormat(CommonUtility.lbToKg(edWeight.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtLB.setOnClickListener {
            try {
                if (boolKg) {
                    boolKg = false

                    txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                    txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                    edWeight.hint = "LB"

                    if (edWeight.text.toString() != "") {
                        edWeight.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(edWeight.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                edCM.visibility = View.INVISIBLE
                lnyInch.visibility = View.VISIBLE

                txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtIN.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_gray, null)

                try {
                    if (!boolInch) {
                        boolInch = true

                        if (edCM.text.toString() != "") {
                            val inch = CommonUtility.getStringFormat(CommonUtility.cmToInch(edCM.text.toString().toDouble()))
                            edFeet.setText(CommonUtility.calcInchToFeet(inch.toDouble()).toString())
                            edInch.setText(CommonUtility.calcInFromInch(inch.toDouble()).toString())
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtCM.setOnClickListener {
            try {
                if (boolInch) {
                    boolInch = false

                    edCM.visibility = View.VISIBLE
                    lnyInch.visibility = View.INVISIBLE

                    txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                    txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    txtIN.background = resources.getDrawable(R.drawable.ract_gray, null)
                    txtCM.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                    var inch = 0.0
                    if (edFeet.text.toString() != "" && edInch.text.toString() != "") {
                        inch = CommonUtility.ftInToInch(edFeet.text.toString().toInt(), edInch.text.toString().toDouble())
                    } else if (edFeet.text.toString() != "" && edInch.text.toString() == "") {
                        inch = CommonUtility.ftInToInch(edFeet.text.toString().toInt(), 0.0)
                    } else if (edFeet.text.toString() == "" && edInch.text.toString() != "") {
                        inch = CommonUtility.ftInToInch(1, edInch.text.toString().toDouble())
                    }

                    edCM.setText(CommonUtility.inchToCm(inch).roundToInt().toDouble().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (!boolKg) {
                    boolKg = true

                    txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                    txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                    txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)

                    edWeight.hint = "KG"

                    if (edWeight.text.toString() != "") {
                        edWeight.setText(CommonUtility.getStringFormat(CommonUtility.lbToKg(edWeight.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtIN.setOnClickListener {
            try {
                if (boolKg) {
                    boolKg = false

                    txtKG.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                    txtLB.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))

                    txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                    edWeight.hint = "LB"

                    if (edWeight.text.toString() != "") {
                        edWeight.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(edWeight.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                edCM.visibility = View.INVISIBLE
                lnyInch.visibility = View.VISIBLE

                txtIN.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                txtIN.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_gray, null)

                try {
                    if (!boolInch) {
                        boolInch = true

                        if (edCM.text.toString() != "") {
                            val inch = CommonUtility.getStringFormat(CommonUtility.cmToInch(edCM.text.toString().toDouble()))
                            edFeet.setText(CommonUtility.calcInchToFeet(inch.toDouble()).toString())
                            edInch.setText(CommonUtility.calcInFromInch(inch.toDouble()).toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnCancel.setOnClickListener {
            try {
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnNext.setOnClickListener {

            try {
                if (boolInch) {
                    LocalDB.setLastInputFoot(this, edFeet.text.toString().toInt())
                    LocalDB.setLastInputInch(this, edInch.text.toString().toFloat())
                    LocalDB.setHeightUnit(this, CommonString.DEF_IN)

                } else {
                    val inch = CommonUtility.getStringFormat(CommonUtility.cmToInch(edCM.text.toString().toDouble()))
                    LocalDB.setLastInputFoot(this, CommonUtility.calcInchToFeet(inch.toDouble()))
                    LocalDB.setLastInputInch(this, CommonUtility.calcInFromInch(inch.toDouble()).toFloat())
                    LocalDB.setHeightUnit(this, CommonString.DEF_CM)

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {

                val strKG: Float
                if (boolKg) {
                    strKG = edWeight.text.toString().toFloat()
                    LocalDB.setWeightUnit(this, CommonString.DEF_KG)
                    LocalDB.setWeightUnit(this, CommonString.DEF_KG)
                    LocalDB.setLastInputWeight(this, strKG)
                } else {
                    strKG = CommonUtility.lbToKg(edWeight.text.toString().toDouble()).roundToInt().toFloat()
                    LocalDB.setWeightUnit(this, CommonString.DEF_LB)
                    LocalDB.setWeightUnit(this, CommonString.DEF_LB)
                    LocalDB.setLastInputWeight(this, strKG)
                }

                val currentDate = CommonUtility.convertFullDateToDate(CommonUtility.getCurrentTimeStamp())

                if (dbHelper.weightExistOrNot(currentDate)) {
                    dbHelper.updateWeight(currentDate, strKG.toString(), "")
                } else {
                    dbHelper.addUserWeight(strKG.toString(), currentDate, "")
                }

                if (boolKg) {
                    LocalDB.setLastInputWeight(this, edWeight.text.toString().toFloat())
                } else {
                    LocalDB.setLastInputWeight(this, CommonUtility.lbToKg(edWeight.text.toString().toDouble()).toFloat())
                }

                setWeightValues()

                setBmiCalculation()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveData() {

        try {
            dbHelper.addHistory(
                pWorkoutList[0].planId,
                dbHelper.getPlanNameByPlanId(pWorkoutList[0].planId),
                CommonUtility.getCurrentTimeStamp(),
                CommonUtility.timeToSecond(txtDurationTime.text.toString()).toString(),
                CommonUtility.getStringFormat(calValue),
                txtTotalNoOfLevel.text.toString(),
                LocalDB.getLastInputWeight(this).toString(),
                LocalDB.getLastInputFoot(this).toString(),
                LocalDB.getLastInputInch(this).toString(),
                feelRate,
                dbHelper.getPlanDayNameByDayId(pWorkoutList[0].dayId)
            )

            dbHelper.updatePlanDayCompleteByDayId(pWorkoutList[0].dayId)

            LocalDB.setLastCompletedDay(this, pWorkoutList[0].planId.toInt(), dbHelper.getPlanDayNameByDayId(pWorkoutList[0].dayId).toInt())

            LocalDB.setLastUnCompletedExPos(this, pWorkoutList[0].planId.toInt(), pWorkoutList[0].dayId, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        finish()
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
