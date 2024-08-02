package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.arrowwould.womenhomeworkout.R
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.pojo.HistoryDetailsClass

class HistoryContentAdapter(private val mContext: Context, private val arrWorkoutCategoryData: ArrayList<HistoryDetailsClass>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<HistoryContentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.row_history, parent, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val item = getItem(pos)

        setPlanWorkoutImage(item.PlanName, holder.imgCategory)

        if (item.PlanName == CommonString.PlanFullBody) {
            holder.txtWorkoutName.text = item.PlanName.plus(" Day ").plus(item.DayName)
        } else {
            holder.txtWorkoutName.text = item.PlanName
        }

       holder.txtContentDateTime.text =
            CommonUtility.convertDateStrToInputFormat(item.DateTime, "MMM dd, HH:mm a")      //item.totWorkout.toString().plus(" workouts")
        holder.txtContentTotalTime.text = CommonUtility.secToTime(item.CompletionTime.toInt())
        holder.txtContentTotalBurnCalories.text = item.BurnKcal.plus(" KCal")    //CommonUtility.secToTime(item.totTime)

        if ((arrWorkoutCategoryData.size - 1) == pos) {
            holder.btmContentLine.visibility = View.GONE
        } else {
            holder.btmContentLine.visibility = View.VISIBLE
        }

    }

    private fun getItem(pos: Int): HistoryDetailsClass {
        return arrWorkoutCategoryData[pos]
    }

    override fun getItemCount(): Int {
        return arrWorkoutCategoryData.size
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rltHeader: LinearLayout = itemView.findViewById(R.id.rltHeader)
        private val rltContent: LinearLayout = itemView.findViewById(R.id.rltContent)

        val txtContentDateTime: TextView = itemView.findViewById(R.id.txtContentDateTime)
        val txtWorkoutName: TextView = itemView.findViewById(R.id.txtWorkoutName)
        val txtContentTotalTime: TextView = itemView.findViewById(R.id.txtContentTotalTime)
        val txtContentTotalBurnCalories: TextView = itemView.findViewById(R.id.txtContentTotalBurnCalories)
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val btmContentLine: View = itemView.findViewById(R.id.btmContentLine)


        init {
            rltHeader.visibility = View.GONE
            rltContent.visibility = View.VISIBLE

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

    }

    private fun setPlanWorkoutImage(PlanName: String, imgCategory: ImageView) {

        when (PlanName) {
            CommonString.PlanFullBody -> imgCategory.setImageResource(R.drawable.ic_history_full_body)
            CommonString.PlanAbsBeginner, CommonString.PlanAbsIntermediate, CommonString.PlanAbsAdvanced -> imgCategory.setImageResource(R.drawable.ic_history_abs)
            CommonString.PlanButtBeginner, CommonString.PlanButtIntermediate, CommonString.PlanButtAdvanced -> imgCategory.setImageResource(R.drawable.ic_history_butt)
            CommonString.PlanThighBeginner, CommonString.PlanThighIntermediate, CommonString.PlanThighAdvanced -> imgCategory.setImageResource(R.drawable.ic_history_thigh)
            CommonString.PlanMorningWarmup -> imgCategory.setImageResource(R.drawable.ic_history_morning_warmup)
            CommonString.PlanSleepyTimeStretch -> imgCategory.setImageResource(R.drawable.ic_history_sleepy_time)
        }

    }

}
