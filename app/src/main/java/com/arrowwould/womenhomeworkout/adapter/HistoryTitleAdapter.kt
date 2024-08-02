package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arrowwould.womenhomeworkout.R
import com.utillity.objects.CommonUtility
import com.utillity.pojo.HistoryWeekDataClass

class HistoryTitleAdapter(private val mContext: Context, private val arrWorkoutCategoryData: ArrayList<HistoryWeekDataClass>) :
    RecyclerView.Adapter<HistoryTitleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.row_history, parent, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val item = getItem(pos)

        holder.txtWeekDate.text =
            CommonUtility.convertDateToDateMonthName(item.weekStart).plus(" - ").plus(CommonUtility.convertDateToDateMonthName(item.weekEnd))
        holder.txtTotalWorkoutCount.text = item.totWorkout.toString().plus(" ${mContext.resources.getString(R.string.workouts)}")
        holder.txtTotalWorkoutTime.text = CommonUtility.secToTime(item.totTime)
        holder.txtTotalBurnCalories.text = item.totKcal.toString().plus(" ${mContext.resources.getString(R.string.lbl_kcal)}")

        holder.rcyHistoryDetails.adapter = HistoryContentAdapter(mContext, item.arrhistoryDetail)
    }

    private fun getItem(pos: Int): HistoryWeekDataClass {
        return arrWorkoutCategoryData[pos]
    }

    override fun getItemCount(): Int {
        return arrWorkoutCategoryData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rltHeader: LinearLayout = itemView.findViewById(R.id.rltHeader)
        private val rltContent: LinearLayout = itemView.findViewById(R.id.rltContent)

        val txtWeekDate: TextView = itemView.findViewById(R.id.txtWeekDate)
        val txtTotalWorkoutCount: TextView = itemView.findViewById(R.id.txtTotalWorkoutCount)
        val txtTotalWorkoutTime: TextView = itemView.findViewById(R.id.txtTotalWorkoutTime)
        val txtTotalBurnCalories: TextView = itemView.findViewById(R.id.txtTotalBurnCalories)

        val rcyHistoryDetails: RecyclerView = itemView.findViewById(R.id.rcyHistoryDetails)

        init {
            rltHeader.visibility = View.VISIBLE
            rltContent.visibility = View.GONE

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

    }
}
