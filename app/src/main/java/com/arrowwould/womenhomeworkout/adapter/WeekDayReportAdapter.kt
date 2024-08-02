package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.HistoryActivity
import com.utillity.db.DataHelper
import com.utillity.objects.CommonUtility
import java.util.*

class WeekDayReportAdapter(val mContext: Context, val isFromHome: Boolean = false) : RecyclerView.Adapter<WeekDayReportAdapter.ViewHolder>() {

    private val arrCurrentWeek = CommonUtility.getCurrentWeekByFirstDay(mContext)
    private val dbHelper = DataHelper(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.row_week_day, parent, false)
        convertView.layoutParams.width = parent.measuredWidth / 7
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        try {
            holder.txtDayDate.text = CommonUtility.convertLongToDay(arrCurrentWeek[pos])

            val dtWeek = CommonUtility.getStringToDate(arrCurrentWeek[pos])

            val calToday = Calendar.getInstance(Locale.ENGLISH)
            calToday.time = dtWeek
            holder.txtDayName.text = CommonUtility.convertDate(calToday.timeInMillis, "E").substring(0, 1)

            if (CommonUtility.convertDate(Calendar.getInstance(Locale.ENGLISH).timeInMillis, "dd") == CommonUtility.convertLongToDay(arrCurrentWeek[pos])) {
                holder.txtDayDate.setTextColor(ContextCompat.getColor(mContext, R.color.colorTheme))
            }

            when {
                dbHelper.isHistoryAvailable(CommonUtility.convertFullDateToDate(arrCurrentWeek[pos])) -> {
                    holder.imgIsWorkoutDay.setImageResource(R.drawable.ic_cal_round_done)
                    CommonUtility.setIconTintColorTheme(mContext,holder.imgIsWorkoutDay)
                }
                Calendar.getInstance(Locale.ENGLISH).time.after(dtWeek) -> {
                    holder.imgIsWorkoutDay.setImageResource(R.drawable.ic_cal_round_fill)
                }
                else -> {
                    holder.imgIsWorkoutDay.setImageResource(R.drawable.ic_cal_round)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return arrCurrentWeek.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val txtDayName: TextView = itemView.findViewById(R.id.txtDayName)
        val txtDayDate: TextView = itemView.findViewById(R.id.txtDayDate)
        val imgIsWorkoutDay: ImageView = itemView.findViewById(R.id.imgIsWorkoutDay)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(mContext, HistoryActivity::class.java)
            mContext.startActivity(intent)
            if (!isFromHome) {
                (mContext as AppCompatActivity).finish()
            }
        }

    }

}
