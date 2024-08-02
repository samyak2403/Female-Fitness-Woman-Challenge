package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.WorkoutListInfoActivity
import com.utillity.objects.CommonString
import com.utillity.objects.Utils
import com.utillity.pojo.DayExTableClass

class DayExListAdapter(private val context: Context, private val list: ArrayList<DayExTableClass>, private val isEdited: Boolean) :
    RecyclerView.Adapter<DayExListAdapter.AdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterVH {
        val view = LayoutInflater.from(context).inflate(R.layout.row_day_ex_list_adapter, parent, false)
        return AdapterVH(view)
    }

    override fun onBindViewHolder(holder: AdapterVH, position: Int) {

        try {
            holder.ivReplace.visibility = if (isEdited) View.VISIBLE else View.GONE

            holder.tvExName.text = list[position].exName

            if (list[position].exUnit == "s") {
                holder.tvExCount.text = list[position].exTime.plus(" " + context.resources.getString(R.string.s))
            } else {
                holder.tvExCount.text = context.resources.getString(R.string.x).plus(" " + list[position].exTime)
            }

            holder.vf.removeAllViews()
            val listImg: ArrayList<String> = Utils.getAssetItems(context, list[position].exPath)

            for (i in 0 until listImg.size) {
                val iv = ImageView(context)
                Glide.with(context).load(listImg[i]).into(iv)
                iv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                holder.vf.addView(iv)
            }

            holder.vf.isAutoStart = true
            holder.vf.flipInterval = context.resources.getInteger(R.integer.viewfliper_animation)

            holder.vf.startFlipping()

            holder.ivReplace.setOnClickListener {
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AdapterVH(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tvExName = view.findViewById(R.id.tvExName) as TextView
        val tvExCount = view.findViewById(R.id.tvExCount) as TextView
        val vf = view.findViewById(R.id.vfEx) as ViewFlipper
        val ivReplace = view.findViewById(R.id.ivReplace) as ImageView

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(pos: View?) {
            val intent = Intent(context, WorkoutListInfoActivity::class.java)
            intent.putExtra(CommonString.extra_workout_details_type, CommonString.val_is_day_ex_list_adapter)
            intent.putExtra(CommonString.extra_exercise_list, list)
            intent.putExtra(CommonString.extra_workout_list_pos, adapterPosition)
            context.startActivity(intent)
            (context as AppCompatActivity).overridePendingTransition(R.anim.slide_up, R.anim.none)
        }

    }

}
