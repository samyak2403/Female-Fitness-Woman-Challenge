package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.WorkoutListActivity
import com.google.gson.Gson
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.pojo.PlanTableClass
import com.utillity.pojo.pWeekDayData
import com.utillity.pojo.pWeeklyDayData
import java.io.Serializable

class WeekDayAdapter(
    val context: Context,
    val arrDayData: ArrayList<pWeekDayData>,
    val arrWeeklyDayStatus: ArrayList<pWeeklyDayData>,
    val pos1: Int,
    val planTableClass: PlanTableClass
) : RecyclerView.Adapter<WeekDayAdapter.ViewHolder>() {

    private var flagPrevDay = true
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val convertView = LayoutInflater.from(context).inflate(R.layout.row_day, viewGroup, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        Log.e("TAG", "onBindViewHolder:::::New data::  "+Gson().toJson(arrDayData[pos])  )
        try {
            if (arrDayData[pos].Day_name == "Cup") {
                holder.tvDay.tag = "n"

                if (arrDayData[pos].Is_completed == "1") {
                    holder.imgCompleteImage.setImageResource(R.drawable.ic_week_winner_cup)
                } else {
                    holder.imgCompleteImage.setImageResource(R.drawable.ic_week_winner_cup_gray)
                }

                holder.imgCompleteImage.visibility = View.VISIBLE
                holder.tvDay.visibility = View.GONE
                holder.imgIndicator.visibility = View.GONE

            } else {
                holder.tvDay.tag = "y"

                holder.imgCompleteImage.setImageResource(R.drawable.ic_week_done_big)
                CommonUtility.setIconTintColorTheme(context,holder.imgCompleteImage)
                if (flagPrevDay && pos1 != 0) {
                    flagPrevDay = arrWeeklyDayStatus[pos1 - 1].Is_completed == "1"
                }

                if (arrWeeklyDayStatus[pos1].Is_completed == "1") {
                    CommonUtility.setIconTintColorTheme(context,holder.imgIndicator)
                    holder.imgCompleteImage.visibility = View.VISIBLE

                    holder.tvDay.visibility = View.GONE

                } else {
                    when {
                        arrDayData[pos].Is_completed == "1" -> {
                            CommonUtility.setIconTintColorTheme(context,holder.imgIndicator)
                            holder.imgCompleteImage.visibility = View.VISIBLE

                            holder.tvDay.visibility = View.GONE

                            flagPrevDay = true
                        }
                        flagPrevDay -> {
                            CommonUtility.setIconTintColorGray(context,holder.imgIndicator)
                            holder.imgCompleteImage.visibility = View.GONE

                            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.colorTheme))
                            holder.tvDay.setBackgroundResource(R.drawable.ic_week_round_dot)
                            CommonUtility.setIconTintColorThemeTextView(context,holder.tvDay)
                            holder.tvDay.visibility = View.VISIBLE

                            flagPrevDay = false
                        }
                        else -> {
                            CommonUtility.setIconTintColorGray(context,holder.imgIndicator)
                            holder.imgCompleteImage.visibility = View.GONE

                            holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
                            holder.tvDay.setBackgroundResource(R.drawable.ic_week_round_line)
                            holder.tvDay.visibility = View.VISIBLE
                            holder.tvDay.tag = "n"

                            flagPrevDay = false
                        }
                    }
                }

                if (pos == 3) {
                    holder.imgIndicator.visibility = View.GONE
                } else {
                    holder.imgIndicator.visibility = View.VISIBLE
                }

                holder.tvDay.text = arrDayData[pos].Day_name.replace("0", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrDayData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val imgIndicator: ImageView = itemView.findViewById(R.id.imgIndicator)
        val imgCompleteImage: ImageView = itemView.findViewById(R.id.imgCompleteImage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            try {
                if (arrDayData[adapterPosition].Day_name != "Cup") {
                    if (tvDay.tag == "y") {

                        val intent = Intent(context, WorkoutListActivity::class.java)
                        intent.putExtra(CommonString.extra_plan_table_class, planTableClass as Serializable)
                        intent.putExtra(CommonString.extra_day_id, arrDayData[adapterPosition].Day_id)
                        intent.putExtra(CommonString.extra_day_name, arrDayData[adapterPosition].Day_name)
                        intent.putExtra(CommonString.extra_week_name, arrWeeklyDayStatus[pos1].Week_name)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Please finish the previous challenge date first.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
