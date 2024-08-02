package com.arrowwould.womenhomeworkout.adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.DaysStatusActivity
import com.arrowwould.womenhomeworkout.activity.WorkoutListActivity
import com.arrowwould.womenhomeworkout.common.Constant
import com.utillity.interfaces.AdsCallback
import com.utillity.objects.CommonString
import com.utillity.objects.CommonUtility
import com.utillity.pojo.PlanTableClass
import java.io.Serializable

class WorkoutCategoryAdapter(private val mContext: Context, private val arrWorkoutCategoryData: ArrayList<PlanTableClass>) :
    RecyclerView.Adapter<WorkoutCategoryAdapter.ViewHolder>() {

    fun addAll(data: ArrayList<PlanTableClass>){
        arrWorkoutCategoryData.clear()
        arrWorkoutCategoryData.addAll(data)
        notifyDataSetChanged()
    }

    var onClickAd = 1
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val convertView = LayoutInflater.from(mContext).inflate(R.layout.row_category, parent, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        try {
            val item = getItem(pos)

            if (item.planLvl == CommonString.PlanLvlTitle) {
                holder.rltWorkOutTitle.visibility = View.VISIBLE
                holder.rltWorkOutDetails.visibility = View.GONE

                holder.txtWorkoutTitle.text = item.planName

            } else {

                holder.rltWorkOutTitle.visibility = View.GONE
                holder.rltWorkOutDetails.visibility = View.VISIBLE

                holder.txtWorkoutCategoryTitle.text = item.planName
                holder.txtWorkoutDetails.text = item.planText
                Log.e("TAG", "onBindViewHolder::::Plan text:::::  ${item.planText}")
                Glide.with(mContext).load("file:///android_asset/".plus(item.planImage)).into(holder.imgWorkoutRow)

                when (item.planLvl) {
                    CommonString.PlanLvlBeginner -> {
                        holder.imgWorkoutDifficulty.setImageResource(R.drawable.ic_beginner_level)
                    }
                    CommonString.PlanLvlIntermediate -> {
                        holder.imgWorkoutDifficulty.setImageResource(R.drawable.ic_intermediate_level)
                    }
                    CommonString.PlanLvlAdvanced -> {
                        holder.imgWorkoutDifficulty.setImageResource(R.drawable.ic_advanced_level)
                    }
                    else -> {
                        holder.imgWorkoutDifficulty.setImageResource(0)
                    }
                }

                if (item.planDays == CommonString.PlanDaysYes) {
                    holder.imgWorkoutDifficulty.visibility = View.INVISIBLE
                    holder.rltProgress.visibility = View.VISIBLE

                    CommonUtility.setDayProgressData(mContext, item.planId, holder.txtDayLeft, holder.txtDayPer, holder.pbDay)

                } else {
                    holder.imgWorkoutDifficulty.visibility = View.VISIBLE
                    holder.rltProgress.visibility = View.INVISIBLE
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getItem(pos: Int): PlanTableClass {
        return arrWorkoutCategoryData[pos]
    }

    override fun getItemCount(): Int {
        return arrWorkoutCategoryData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, AdsCallback {

        val rltWorkOutTitle: RelativeLayout = itemView.findViewById(R.id.rltWorkOutTitle)
        val rltWorkOutDetails: CardView = itemView.findViewById(R.id.RltWorkOutDetails)
        val rltProgress: RelativeLayout = itemView.findViewById(R.id.rltProgress)

        val txtWorkoutTitle: TextView = itemView.findViewById(R.id.txtWorkoutTitle)
        val txtWorkoutDetails: TextView = itemView.findViewById(R.id.txtWorkoutDetails)
        val txtWorkoutCategoryTitle: TextView = itemView.findViewById(R.id.txtWorkoutCategoryTitle)
        val txtDayLeft: TextView = itemView.findViewById(R.id.txtDayLeft)
        val txtDayPer: TextView = itemView.findViewById(R.id.txtDayPer)

        val imgWorkoutRow: ImageView = itemView.findViewById(R.id.imgWorkoutRow)
        val imgWorkoutDifficulty: ImageView = itemView.findViewById(R.id.imgWorkoutDifficulty)

        val pbDay: ProgressBar = itemView.findViewById(R.id.pbDay)

        init {
            rltWorkOutDetails.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            try {
                Log.e("TAG", "onClick::::onCLickkcc $onClickAd  ${Constant.FIRST_CLICK_COUNT}  ${getItem(adapterPosition).planDays}  ${arrWorkoutCategoryData.size}")
                if (onClickAd == Constant.FIRST_CLICK_COUNT && Constant.FIRST_CLICK_COUNT != 0){
                    activityNext()
                    Log.e("TAG", "onClick:::Iff::::  " )
                    onClickAd = 1
                }else{
                    Log.e("TAG", "onClick:Elser::: " )
                    onClickAd += 1
                   activityNext()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun adLoadingFailed() {
            activityNext()
        }

        override fun adClose() {
            activityNext()
        }

        override fun startNextScreen() {
            activityNext()
        }

         private fun activityNext(){

             Log.e("TAG", "activityNext:Click activity::  $onClickAd")
            if (getItem(adapterPosition).planDays == CommonString.PlanDaysYes) {
                val intent = Intent(mContext, DaysStatusActivity::class.java)
                intent.putExtra(CommonString.extra_plan_table_class, getItem(adapterPosition) as Serializable)
                mContext.startActivity(intent)
            } else {
                val intent = Intent(mContext, WorkoutListActivity::class.java)
                intent.putExtra(CommonString.extra_plan_table_class, getItem(adapterPosition) as Serializable)
                mContext.startActivity(intent)
            }
        }

    }

}
