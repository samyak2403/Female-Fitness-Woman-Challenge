package com.arrowwould.womenhomeworkout.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.HistoryActivity
import com.arrowwould.womenhomeworkout.adapter.WeekDayReportAdapter
import com.arrowwould.womenhomeworkout.common.Constant
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.ads.AdView
//import com.google.type.DateTime

import com.arrowwould.womenhomeworkout.ConstantAd
import com.utillity.db.DataHelper
import com.utillity.objects.*
import kotlinx.android.synthetic.main.fragment_my_training.view.*
import java.text.SimpleDateFormat
import org.joda.time.DateTime

import java.util.*
//import org.joda.time.DateTime

import kotlin.math.roundToInt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReportFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        loadDefaultSettings()
    }

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

    private var count = 0

    private lateinit var daysText: ArrayList<String>
    private lateinit var daysYearText: ArrayList<String>

    private lateinit var adView: AdView
    private lateinit var txtTotalWorkouts: TextView
    private lateinit var txtTotalKcal: TextView
    private lateinit var txtTotalMinutes: TextView
    private lateinit var txtCurrentKg: TextView
    private lateinit var txtSelWeightUnit: TextView
    private lateinit var txtHeaviestKg: TextView
    private lateinit var txtLightestKg: TextView
    private lateinit var tvHeight: TextView
    private lateinit var txtBmiTitleValue: TextView
    private lateinit var txtBmiGrade: TextView
    private lateinit var txtWeightString: TextView
    private lateinit var txtRecord: TextView
    private lateinit var txtHistoryMore: TextView

    private lateinit var btnEditHeight: TextView
    private lateinit var btnEdit: TextView

    private lateinit var txtHideShowBmi: TextView

    private lateinit var lnyBmiGraphMain: LinearLayout

    private lateinit var blankView1: View
    private lateinit var ivAddGraph: ImageView

    private lateinit var rcyHistoryWeek: RecyclerView
    private lateinit var chartWeight: CombinedChart
    private lateinit var chartExercise: BarChart
    private lateinit var llAdView: RelativeLayout
    private lateinit var llAdViewFacebook: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_report, container, false)

        mContext = rootView.context
        dbHelper = DataHelper(mContext)

        init()
        initAction()

        return rootView
    }

    private fun init() {
        txtTotalWorkouts = rootView.findViewById(R.id.txtTotalWorkouts)
        txtTotalMinutes = rootView.findViewById(R.id.txtTotalMinutes)
        txtSelWeightUnit = rootView.findViewById(R.id.txtSelWeightUnit)
        txtBmiTitleValue = rootView.findViewById(R.id.txtBmiTitleValue)
        txtWeightString = rootView.findViewById(R.id.txtWeightString)
        txtHistoryMore = rootView.findViewById(R.id.txtHistoryMore)
        txtCurrentKg = rootView.findViewById(R.id.txtCurrentKg)
        txtHeaviestKg = rootView.findViewById(R.id.txtHeaviestKg)
        txtLightestKg = rootView.findViewById(R.id.txtLightestKg)
        btnEditHeight = rootView.findViewById(R.id.btnEditHeight)
        txtTotalKcal = rootView.findViewById(R.id.txtTotalKcal)
        txtBmiGrade = rootView.findViewById(R.id.txtBmiGrade)
        txtRecord = rootView.findViewById(R.id.txtRecord)
        tvHeight = rootView.findViewById(R.id.tvHeight)
        btnEdit = rootView.findViewById(R.id.btnEdit)
        txtHideShowBmi = rootView.findViewById(R.id.txtHideShowBmi)

        lnyBmiGraphMain = rootView.findViewById(R.id.lnyBmiGraphMain)

        ivAddGraph = rootView.findViewById(R.id.ivAddGraph)

        chartWeight = rootView.findViewById(R.id.chartWeight)
        rcyHistoryWeek = rootView.findViewById(R.id.rcyHistoryWeek)
        blankView1 = rootView.findViewById(R.id.blankView1)


        ConstantAd.setBannerAdd(rootView.adMobView,requireContext())

    }

    private fun loadDefaultSettings(){
//        CommonConstantAd.loadBannerAd(mContext, adView)
//        CommonConstantAd.loadBannerGoogleAd(mContext, llAdView,Constant.GOOGLE_BANNER,"Rec")

        setupGraph()
        setWeightValues()
        setBmiCalculation()

        txtTotalWorkouts.text = dbHelper.getHistoryTotalWorkout().toString()
        txtTotalKcal.text = dbHelper.getHistoryTotalKCal().toInt().toString()
        txtTotalMinutes.text = ((dbHelper.getHistoryTotalMinutes() / 60).toDouble()).roundToInt().toString()
        rcyHistoryWeek.adapter = WeekDayReportAdapter(mContext, true)

    }

    private fun initAction() {

        ivAddGraph.setOnClickListener { setWeightDialog() }

        btnEdit.setOnClickListener { setHeightWeightDialog() }

        btnEditHeight.setOnClickListener { setHeightWeightDialog() }

        txtRecord.setOnClickListener {
            val intent = Intent(mContext, HistoryActivity::class.java)
            startActivity(intent)
        }

        txtHistoryMore.setOnClickListener {
            val intent = Intent(mContext, HistoryActivity::class.java)
            startActivity(intent)
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

    }

    private fun setupGraph() {

        try {
            val format = SimpleDateFormat("dd/MM", Locale.getDefault())
            val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)

            calendar.time = formatDate.parse("$year-01-01")!!

            count = isLeapYear(year) + 1
            daysText = ArrayList()
            daysYearText = ArrayList()

            for (i in 0 until count) {
                daysText.add(format.format(calendar.time))
                daysYearText.add(formatDate.format(calendar.time))
                calendar.add(Calendar.DATE, 1)
            }

            chartWeight.drawOrder = arrayOf(CombinedChart.DrawOrder.LINE)

            chartWeight.description.isEnabled = false
            chartWeight.description.text = "Date"
            chartWeight.setNoDataText(resources.getString(R.string.app_name))
            chartWeight.setBackgroundColor(Color.WHITE)
            chartWeight.setDrawGridBackground(false)
            chartWeight.setDrawBarShadow(false)
            chartWeight.isHighlightFullBarEnabled = false

            val l = chartWeight.legend
            l.isWordWrapEnabled = false
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)

            val leftAxis = chartWeight.axisLeft
            leftAxis.setDrawGridLines(true)

            val rightAxis = chartWeight.axisRight
            rightAxis.isEnabled = false

            val xAxis = chartWeight.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = count.toFloat()
            xAxis.granularity = 1f
            xAxis.labelCount = 30

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value < daysText.size && value > 0) {
                        daysText[value.toInt()]
                    } else ""
                }
            }

            val data = CombinedData()
            data.setData(generateLineData())

            data.setValueTypeface(Typeface.DEFAULT)
            chartWeight.data = data

            chartWeight.setVisibleXRange(5f, 8f)

            val strDate = CommonUtility.convertFullDateToDate(CommonUtility.getCurrentTimeStamp())
            val position = daysYearText.indexOf(strDate)
            chartWeight.centerViewTo(position.toFloat(), 50f, YAxis.AxisDependency.LEFT)

            setGraphTouch()

            chartWeight.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    private fun generateLineData(): LineData {

        val yAxisData = dbHelper.getUserWeightData()
        val d = LineData()

        val entries = ArrayList<Entry>()
        if (yAxisData.size > 0) {
            try {
                for (index in 0 until yAxisData.size) {
                    //            yAxisData[index]["KG"]
                    val strDate = yAxisData[index]["DT"]
                    val position = daysYearText.indexOf(strDate)
                    if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                        entries.add(Entry(position.toFloat(), yAxisData[index]["KG"]!!.toFloat()))
                    } else {
                        entries.add(Entry(position.toFloat(), CommonUtility.kgToLb(yAxisData[index]["KG"]!!.toDouble()).toFloat()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                if (LocalDB.getLastInputWeight(mContext) > 0) {
                    val strDate = CommonUtility.convertFullDateToDate(CommonUtility.getCurrentTimeStamp())
                    val position = daysYearText.indexOf(strDate)
                    if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                        entries.add(Entry(position.toFloat(), LocalDB.getLastInputWeight(mContext)))
                    } else {
                        entries.add(Entry(position.toFloat(), CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble()).toFloat()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val set = LineDataSet(entries, "Date")
        set.color = Color.rgb(130, 87, 242)
        set.lineWidth = 1.5f
        set.setCircleColor(Color.rgb(130, 130, 130))
        set.circleRadius = 5f


        set.fillColor = Color.rgb(130, 130, 130)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(130, 87, 242)

        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)

        return d
    }


    private fun setGraphTouch() {

        chartWeight.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    chartWeight.parent.requestDisallowInterceptTouchEvent(true)
            }
            false
        }

    }

    private fun isLeapYear(year: Int): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR)
    }

    private fun setWeightValues() {

        if (LocalDB.getLastInputWeight(mContext) != 0f) {
            if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                txtCurrentKg.text = CommonUtility.getStringFormat(LocalDB.getLastInputWeight(mContext).toDouble()).plus(" ${CommonString.DEF_KG}")
            } else {
                txtCurrentKg.text = CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble()))
                    .plus(" ${CommonString.DEF_LB}")
            }
        }

        if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
            txtSelWeightUnit.text = CommonString.DEF_KG
            txtCurrentKg.text = LocalDB.getLastInputWeight(mContext).toString().plus(" ${CommonString.DEF_KG}")
        } else {
            txtSelWeightUnit.text = CommonString.DEF_LB
            txtCurrentKg.text =
                CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble())).plus(" ${CommonString.DEF_LB}")
        }

        val maxWeight = dbHelper.getMaxWeight().toFloat()
        val minWeight = dbHelper.getMinWeight().toFloat()

        try {
            if (maxWeight > 0) {
                if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                    txtHeaviestKg.text = maxWeight.toString().plus(" ${CommonString.DEF_KG}")
                } else {
                    txtHeaviestKg.text =
                        CommonUtility.getStringFormat(CommonUtility.kgToLb(maxWeight.toDouble())).plus(" ${CommonString.DEF_LB}")
                }
            } else {
                if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                    txtHeaviestKg.text = LocalDB.getLastInputWeight(mContext).toString().plus(" ${CommonString.DEF_KG}")
                } else {
                    txtHeaviestKg.text =
                        CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble()))
                            .plus(" ${CommonString.DEF_LB}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (minWeight > 0) {
                if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                    txtLightestKg.text = minWeight.toString().plus(" ${CommonString.DEF_KG}")
                } else {
                    txtLightestKg.text =
                        CommonUtility.getStringFormat(CommonUtility.kgToLb(minWeight.toDouble())).plus(" ${CommonString.DEF_LB}")
                }
            } else {
                if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                    txtLightestKg.text = LocalDB.getLastInputWeight(mContext).toString().plus(" ${CommonString.DEF_KG}")
                } else {
                    txtLightestKg.text =
                        CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble()))
                            .plus(" ${CommonString.DEF_KG}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (txtHeaviestKg.text.toString().replace(" KG", "").replace(" LB", "").toFloat() > 0f) {
                chartWeight.axisLeft.axisMaximum = txtHeaviestKg.text.toString().replace(" KG", "").replace(" LB", "").toFloat() + 10f
            } else {
                chartWeight.axisLeft.axisMaximum = 240f
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (txtLightestKg.text.toString().replace(" KG", "").replace(" LB", "").toFloat() > 0f) {
                chartWeight.axisLeft.axisMinimum = txtLightestKg.text.toString().replace(" KG", "").replace(" LB", "").toFloat() - 10f
            } else {
                chartWeight.axisLeft.axisMinimum = 30f
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            tvHeight.text =
                LocalDB.getLastInputFoot(mContext).toString().plus(" ${CommonString.DEF_FT} ")
                    .plus(LocalDB.getLastInputInch(mContext))
                    .plus(" ${CommonString.DEF_IN}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setBmiCalculation() {
        try {

            val bmiValue = CommonUtility.getBmiCalculation(
                LocalDB.getLastInputWeight(mContext),
                LocalDB.getLastInputFoot(mContext),
                LocalDB.getLastInputInch(mContext).toInt()
            )

            val calBmiGraph = CommonUtility.calculationForBmiGraph(CommonUtility.getStringFormat(bmiValue).toFloat())

            val param = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (CommonUtility.getStringFormat(calBmiGraph.toDouble()).toFloat())
            )

            txtBmiTitleValue.text = ": ".plus(CommonUtility.getStringFormat(bmiValue))
            txtBmiGrade.text = CommonUtility.getStringFormat(bmiValue)
            txtWeightString.text = CommonUtility.bmiWeightString(CommonUtility.getStringFormat(bmiValue).toFloat())
            txtWeightString.setTextColor(ColorStateList.valueOf(CommonUtility.bmiWeightTextColor(mContext, bmiValue.toFloat())))
            blankView1.layoutParams = param
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun parseTime(time: Long, pattern: String): String {
        val sdf = SimpleDateFormat(
            pattern,
            Locale.getDefault()
        )
        return sdf.format(Date(time))
    }

    private fun setWeightDialog() {

        var boolKg: Boolean
//        var dateSelect: String = CommonUtility.convertFullDateToDate(CommonUtility.getCurrentTimeStamp())
        var dateSelect: String = parseTime(Date().time, Constant.WEIGHT_TABLE_DATE_FORMAT)

        Log.e("TAG", "setWeightDialog:Dataatat  $dateSelect" )
        val dialog = Dialog(mContext)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_weight_set_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dtpWeightSet = dialog.findViewById<HorizontalPicker>(R.id.dtpWeightSet)
        val edKG = dialog.findViewById<EditText>(R.id.edKG)
        val txtUnit = dialog.findViewById<TextView>(R.id.txtUnit)
        val txtLB = dialog.findViewById(R.id.txtLB) as TextView
        val btnChooseUnit = dialog.findViewById<Button>(R.id.btnChooseUnit)
        val btnCancel = dialog.findViewById<Button>(R.id.btnOkay)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
            boolKg = true

            edKG.setText(LocalDB.getLastInputWeight(mContext).toString())

            txtUnit.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

            txtUnit.background = resources.getDrawable(R.drawable.ract_theme_select, null)
            txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)
        } else {
            boolKg = false

            edKG.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble())))

            txtUnit.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
            txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

            txtUnit.background = resources.getDrawable(R.drawable.ract_gray, null)
            txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)
        }


        dtpWeightSet.performClick()

        dtpWeightSet
            .setDays(369)
            .setOffset(365)
            .setListener { dateSelected ->
//                dateSelect = DateUtils.getDate(dateSelected.toDate().time, Locale.getDefault())
                dateSelect = parseTime(dateSelected.toDate().time, Constant.WEIGHT_TABLE_DATE_FORMAT)
                Log.e("TAG", "setWeightDialog::::dateSelect :::  $dateSelect" ) }
            .showTodayButton(false)
            .init()

        dtpWeightSet.setDate(DateTime.now())

        btnChooseUnit.setOnClickListener {
            dialog.cancel()

            dlUnitDialog(true)
        }

        txtLB.setOnClickListener {
            try {
                if (boolKg) {
                    boolKg = false

                    txtUnit.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

                    txtUnit.background = resources.getDrawable(R.drawable.ract_gray, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                    edKG.hint = "LB"

                    if (edKG.text.toString() != "") {
                        edKG.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(edKG.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        txtUnit.setOnClickListener {
            try {
                if (!boolKg) {
                    boolKg = true

                    txtUnit.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

                    txtUnit.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                    txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)

                    edKG.hint = "KG"

                    if (edKG.text.toString() != "") {
                        edKG.setText(CommonUtility.getStringFormat(CommonUtility.lbToKg(edKG.text.toString().toDouble())))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnCancel.setOnClickListener {
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnSave.setOnClickListener {

            try {
                val strKG: Float

                if (boolKg) {
                    strKG = edKG.text.toString().toFloat()
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_KG)
                    LocalDB.setLastInputWeight(mContext, strKG)
                } else {
                    strKG = CommonUtility.lbToKg(edKG.text.toString().toDouble()).roundToInt().toFloat()
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_LB)
                    LocalDB.setLastInputWeight(mContext, strKG)
                }

                if (dbHelper.weightExistOrNot(dateSelect)) {
                    if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                        dbHelper.updateWeight(dateSelect, strKG.toString(), "")
                    } else {
                        dbHelper.updateWeight(dateSelect, CommonUtility.lbToKg(strKG.toString().toDouble()).roundToInt().toString(), "")
                    }
                } else {
                    if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                        dbHelper.addUserWeight(strKG.toString(), dateSelect, "")
                    } else {
                        dbHelper.addUserWeight(CommonUtility.lbToKg(strKG.toString().toDouble()).roundToInt().toString(), dateSelect, "")
                    }
                }

                setupGraph()
                setWeightValues()
                setBmiCalculation()

                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    private fun dlUnitDialog(boolIsWeight: Boolean) {

        val dialog = Dialog(mContext)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_weight_unit)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val txtDlDialog = dialog.findViewById(R.id.txt_dl_dialog) as TextView
        val rbDlWeight1 = dialog.findViewById(R.id.rbDlWeight1) as RadioButton
        val rbDlWeight2 = dialog.findViewById(R.id.rbDlWeight2) as RadioButton
        val rgDlWeight = dialog.findViewById(R.id.rgDlWeight) as RadioGroup

        try {
            if (boolIsWeight) {

                if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {
                    rbDlWeight2.isChecked = true
                } else {
                    rbDlWeight1.isChecked = true
                }

                txtDlDialog.text = resources.getString(R.string.select_your_weight_unit)
                rbDlWeight1.text = CommonString.DEF_LB
                rbDlWeight2.text = CommonString.DEF_KG

            } else {

                if (LocalDB.getHeightUnit(mContext) == CommonString.DEF_IN) {
                    rbDlWeight2.isChecked = true
                } else {
                    rbDlWeight1.isChecked = true
                }

                txtDlDialog.text = resources.getString(R.string.select_your_height_unit)
                rbDlWeight1.text = CommonString.DEF_CM
                rbDlWeight2.text = CommonString.DEF_IN

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rgDlWeight.setOnCheckedChangeListener { _, checkedId ->

            try {

                if (checkedId == R.id.rbDlWeight1) {
                    if (boolIsWeight) {
                        LocalDB.setWeightUnit(mContext, CommonString.DEF_LB)

                    } else {
                        LocalDB.setHeightUnit(mContext, CommonString.DEF_CM)
                    }
                } else if (checkedId == R.id.rbDlWeight2) {
                    if (boolIsWeight) {
                        LocalDB.setWeightUnit(mContext, CommonString.DEF_KG)
                    } else {
                        LocalDB.setHeightUnit(mContext, CommonString.DEF_IN)
                    }
                }

                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            setWeightDialog()
        }

        dialog.show()
    }

    private fun setHeightWeightDialog() {

        var boolKg: Boolean
        var boolInch: Boolean

        val dialog = Dialog(mContext)
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
            if (LocalDB.getWeightUnit(mContext) == CommonString.DEF_KG) {

                edWeight.setText(LocalDB.getLastInputWeight(mContext).toString())

                txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

                txtKG.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_gray, null)
            } else {
                boolKg = false

                edWeight.setText(CommonUtility.getStringFormat(CommonUtility.kgToLb(LocalDB.getLastInputWeight(mContext).toDouble())))

                txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

                txtKG.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtLB.background = resources.getDrawable(R.drawable.ract_theme_select, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        boolInch = true
        try {
            if (LocalDB.getHeightUnit(mContext) == CommonString.DEF_IN) {

                edCM.visibility = View.INVISIBLE
                lnyInch.visibility = View.VISIBLE

                txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

                txtIN.background = resources.getDrawable(R.drawable.ract_theme_select, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_gray, null)

                edFeet.setText(LocalDB.getLastInputFoot(mContext).toString())
                edInch.setText(LocalDB.getLastInputInch(mContext).toString())
            } else {
                boolInch = false

                edCM.visibility = View.VISIBLE
                lnyInch.visibility = View.INVISIBLE

                txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

                txtIN.background = resources.getDrawable(R.drawable.ract_gray, null)
                txtCM.background = resources.getDrawable(R.drawable.ract_theme_select, null)

                val inch = CommonUtility.ftInToInch(LocalDB.getLastInputFoot(mContext), LocalDB.getLastInputInch(mContext).toDouble())

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

                    txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                    txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

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

                    txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

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

                    txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

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

                txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

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

                    txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                    txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

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

                    txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

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

                    txtKG.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                    txtLB.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))

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

                txtIN.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                txtCM.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))

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
                    LocalDB.setLastInputFoot(mContext, edFeet.text.toString().toInt())
                    LocalDB.setLastInputInch(mContext, edInch.text.toString().toFloat())
                    LocalDB.setHeightUnit(mContext, CommonString.DEF_IN)

                } else {
                    val inch = CommonUtility.getStringFormat(CommonUtility.cmToInch(edCM.text.toString().toDouble()))
                    LocalDB.setLastInputFoot(mContext, CommonUtility.calcInchToFeet(inch.toDouble()))
                    LocalDB.setLastInputInch(mContext, CommonUtility.calcInFromInch(inch.toDouble()).toFloat())
                    LocalDB.setHeightUnit(mContext, CommonString.DEF_CM)

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {

                val strKG: Float
                if (boolKg) {
                    strKG = edWeight.text.toString().toFloat()
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_KG)
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_KG)
                    LocalDB.setLastInputWeight(mContext, strKG)
                } else {
                    strKG = CommonUtility.lbToKg(edWeight.text.toString().toDouble()).roundToInt().toFloat()
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_LB)
                    LocalDB.setWeightUnit(mContext, CommonString.DEF_LB)
                    LocalDB.setLastInputWeight(mContext, strKG)
                }

                val currentDate = CommonUtility.convertFullDateToDate(CommonUtility.getCurrentTimeStamp())

                if (dbHelper.weightExistOrNot(currentDate)) {
                    dbHelper.updateWeight(currentDate, strKG.toString(), "")
                } else {
                    dbHelper.addUserWeight(strKG.toString(), currentDate, "")
                }


                if (boolKg) {
                    LocalDB.setLastInputWeight(mContext, edWeight.text.toString().toFloat())
                } else {
                    LocalDB.setLastInputWeight(mContext, CommonUtility.lbToKg(edWeight.text.toString().toDouble()).toFloat())
                }

                setupGraph()

                setWeightValues()

                setBmiCalculation()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            dialog.dismiss()
        }

        dialog.show()
    }

}
