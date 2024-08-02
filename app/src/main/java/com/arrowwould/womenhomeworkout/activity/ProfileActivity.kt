package com.arrowwould.womenhomeworkout.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.interfaces.CallbackListener
import com.utillity.custom.DatePickerFragment
import com.utillity.db.DataHelper
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.objects.LocalDB
import kotlinx.android.synthetic.main.activity_profile.*
import kotlin.math.roundToInt

class ProfileActivity : BaseActivity(), DatePickerDialog.OnDateSetListener, CallbackListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dtBirth = "$year - ${CommonUtility.getString2DFormat(month + 1)} - ${CommonUtility.getString2DFormat(dayOfMonth)}"
        tvBirthYear.text = dtBirth
        LocalDB.setBirthDate(this, dtBirth)
    }

    override fun onResume() {
        openInternetDialog(this)
        super.onResume()
    }


    private lateinit var dbHelper: DataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DataHelper(this)

        init()
        initAction()

    }

    private fun init() {

        tvBirthYear.text = LocalDB.getBirthDate(this)

        try {
            if (LocalDB.getWeightUnit(this) == CommonString.DEF_KG) {
                rbKgCm.isChecked = true
                txtWeightValue.text = LocalDB.getLastInputWeight(this).toString()

            } else {
                rbLbFt.isChecked = true
                txtWeightValue.text = CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(this).toDouble()))

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (LocalDB.getHeightUnit(this) == CommonString.DEF_IN) {
                txtHeightValue.text = LocalDB.getLastInputFoot(this).toString().plus(" feet ")
                    .plus(LocalDB.getLastInputInch(this).toString()).plus(" inch")
            } else {
                val inch = CommonUtility.ftInToInch(LocalDB.getLastInputFoot(this), LocalDB.getLastInputInch(this).toDouble())
                txtHeightValue.text = CommonUtility.inchToCm(inch).roundToInt().toDouble().toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (LocalDB.getGender(this) == getString(com.utillity.R.string.male)) {
                rbMale.isChecked = true
            } else {
                rbFemale.isChecked = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initAction() {

        tvBack.setOnClickListener { onBackPressed() }

        rgUnits.setOnCheckedChangeListener { _, checkedId ->
            try {
                when (checkedId) {
                    R.id.rbKgCm -> {
                        LocalDB.setWeightUnit(this, CommonString.DEF_KG)
                        LocalDB.setHeightUnit(this, CommonString.DEF_CM)

                        txtWeightValue.text = LocalDB.getLastInputWeight(this).toString()

                        val inch = CommonUtility.ftInToInch(LocalDB.getLastInputFoot(this), LocalDB.getLastInputInch(this).toDouble())
                        txtHeightValue.text = CommonUtility.inchToCm(inch).roundToInt().toDouble().toString()
                    }

                    R.id.rbLbFt -> {
                        LocalDB.setWeightUnit(this, CommonString.DEF_LB)
                        LocalDB.setHeightUnit(this, CommonString.DEF_IN)

                        txtHeightValue.text = LocalDB.getLastInputFoot(this).toString().plus(" feet ")
                            .plus(LocalDB.getLastInputInch(this).toString()).plus(" inch")

                        txtWeightValue.text = CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(this).toDouble()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rgGender.setOnCheckedChangeListener { _, checkedId ->
            try {
                when (checkedId) {
                    R.id.rbMale -> {
                        LocalDB.setGender(this, getString(com.utillity.R.string.male))
                    }

                    R.id.rbFemale -> {
                        LocalDB.setGender(this, getString(com.utillity.R.string.female))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtWeightValue.setOnClickListener { setHeightWeightDialog() }

        txtHeightValue.setOnClickListener { setHeightWeightDialog() }

        tvBirthYear.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "Date Picker")
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

                    txtHeightValue.text = edFeet.text.toString().plus(" feet ").plus(edInch.text.toString()).plus(" inch")
                } else {
                    val inch = CommonUtility.getStringFormat(CommonUtility.cmToInch(edCM.text.toString().toDouble()))
                    LocalDB.setLastInputFoot(this, CommonUtility.calcInchToFeet(inch.toDouble()))
                    LocalDB.setLastInputInch(this, CommonUtility.calcInFromInch(inch.toDouble()).toFloat())
                    LocalDB.setHeightUnit(this, CommonString.DEF_CM)

                    txtHeightValue.text = CommonUtility.inchToCm(inch.toDouble()).roundToInt().toDouble().toString()

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

                txtWeightValue.text = CommonUtility.getStringFormat(edWeight.text.toString().toDouble())

                if (boolKg) {
                    LocalDB.setLastInputWeight(this, edWeight.text.toString().toFloat())
                } else {
                    LocalDB.setLastInputWeight(this, CommonUtility.lbToKg(edWeight.text.toString().toDouble()).toFloat())
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }

}
