package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arrowwould.womenhomeworkout.R
import com.google.gson.Gson
import com.utillity.objects.CommonUtility
import com.utillity.pojo.PlanTableClass
import com.utillity.pojo.pWeeklyDayData

class WeeklyDayStatusAdapter(val context: Context, private val arrWeeklyDayStatus: ArrayList<pWeeklyDayData>,private val planTableClass: PlanTableClass) : RecyclerView.Adapter<WeeklyDayStatusAdapter.ViewHolder>() {

    private var boolFlagWeekComplete = false
    override fun getItemCount(): Int {
        return arrWeeklyDayStatus.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val convertView = LayoutInflater.from(context).inflate(R.layout.row_day_status, viewGroup, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        Log.e("TAG", "onBindViewHolder:::WeeklyDayStatus::::: "+Gson().toJson(arrWeeklyDayStatus[pos]) )

        try {
            if (!boolFlagWeekComplete && arrWeeklyDayStatus[pos].Is_completed == "0") {
                holder.weekIcon.setImageResource(R.drawable.ic_week_light_done)
                CommonUtility.setIconTintColorTheme(context,holder.weekIcon)
                holder.vWeekProgressLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTheme))

                boolFlagWeekComplete = true
                holder.tvWeekProgressTxt.visibility = View.VISIBLE

                var count = 0
                for (i in 0 until arrWeeklyDayStatus[pos].arrWeekDayData.size) {
                    if (arrWeeklyDayStatus[pos].arrWeekDayData[i].Is_completed == "1") {
                        count++
                    }
                }

                holder.tvWeekProgressTxt.text = HtmlCompat.fromHtml("<font color='${ContextCompat.getColor(context,R.color.colorTheme)}'>$count</font>/7", HtmlCompat.FROM_HTML_MODE_LEGACY)

            } else if (arrWeeklyDayStatus[pos].Is_completed == "1") {
                holder.weekIcon.setImageResource(R.drawable.ic_week_done_small)
                CommonUtility.setIconTintColorTheme(context,holder.weekIcon)
                holder.vWeekProgressLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTheme))
            } else {
                holder.weekIcon.setImageResource(R.drawable.ic_week_light)
            }

            if (arrWeeklyDayStatus[pos].Week_name == "04") {
                holder.vWeekProgressLine.visibility = View.INVISIBLE
            } else {
                holder.vWeekProgressLine.visibility = View.VISIBLE
            }

            val strCurWeekText = "${context.resources.getString(R.string.week)} ${arrWeeklyDayStatus[pos].Week_name.replace("0", "")}"
            holder.tvCurWeektxt.text = strCurWeekText//.plus(arrWeeklyDayStatus[pos].Week_name.replace("0", ""))

            holder.gvDayItem.adapter = WeekDayAdapter(context, arrWeeklyDayStatus[pos].arrWeekDayData, arrWeeklyDayStatus, pos,planTableClass)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val weekIcon: ImageView = itemView.findViewById(R.id.weekIcon)

        val tvCurWeektxt: TextView = itemView.findViewById(R.id.tvCurWeektxt)
        val tvWeekProgressTxt: TextView = itemView.findViewById(R.id.tvWeekProgressTxt)
        val vWeekProgressLine: View = itemView.findViewById(R.id.vWeekProgressLine)

        val gvDayItem: RecyclerView = itemView.findViewById(R.id.gvDayItem)

        init {
            gvDayItem.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        }

        override fun onClick(v: View?) {

        }

    }

}
