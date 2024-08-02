package com.arrowwould.womenhomeworkout.adapter

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.interfaces.ReminderInterface
import com.arrowwould.womenhomeworkout.reminderNew.AlarmReceiver
import com.arrowwould.womenhomeworkout.reminderNew.Reminder
import com.arrowwould.womenhomeworkout.reminderNew.ReminderDatabase

class ReminderListAdapter(val context: Context, val arrReminder: ArrayList<Reminder>) :
    RecyclerView.Adapter<ReminderListAdapter.ViewHolder>() {

    val reminderInterface = context as ReminderInterface
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val imgDelete = view.findViewById<ImageView>(R.id.imgDelete)
        val txtTime = view.findViewById<TextView>(R.id.txtTime)
        val txtRepeat = view.findViewById<TextView>(R.id.txtRepeat)
        val txtRepeatTitle = view.findViewById<TextView>(R.id.txtRepeatTitle)
        val swtReminderOnOff = view.findViewById<Switch>(R.id.swtReminderOnOff)

        init {



            imgDelete.setOnClickListener {
                try {
                    confirmDeleteReminder(context, "Tip", "Confirm Delete", adapterPosition)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            txtRepeatTitle.setOnClickListener {
                try {
                    if (arrReminder[adapterPosition].days!!.contains(",")) {
                        reminderInterface.editDays(
                            arrReminder[adapterPosition].iD.toString(),
                            true, arrReminder[adapterPosition].days!!.split(",") as ArrayList<String>)
                    } else {
                        reminderInterface.editDays(
                            arrReminder[adapterPosition].iD.toString(),
                            true, arrayListOf(arrReminder[adapterPosition].days!!)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            txtTime.setOnClickListener {
                try {
                    if (arrReminder[adapterPosition].days!!.contains(",")) {
                        reminderInterface.editTime(
                            arrReminder[adapterPosition].iD.toString(),
                            true, arrReminder[adapterPosition].days!!.split(",") as ArrayList<String>,
                            arrReminder[adapterPosition].time!!.split(":")[0].toInt(),
                            arrReminder[adapterPosition].time!!.split(":")[1].toInt())
                    } else {
                        reminderInterface.editTime(
                            arrReminder[adapterPosition].iD.toString(),
                            true, arrayListOf(arrReminder[adapterPosition].days!!),
                            arrReminder[adapterPosition].time!!.split(":")[0].toInt(),
                            arrReminder[adapterPosition].time!!.split(":")[1].toInt())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            swtReminderOnOff.setOnCheckedChangeListener { buttonView, isChecked ->
                try {
//                    val dbHelper = DataHelper(context)
                    val rb = ReminderDatabase(context)
                    if (isChecked) {
                        rb.updateReminderActive(arrReminder[adapterPosition].iD.toString(), "true")
//                        dbHelper.updateReminder(arrReminder[adapterPosition].iD.toString(), "true")
                    } else {
                        rb.updateReminderActive(arrReminder[adapterPosition].iD.toString(), "false")
//                        dbHelper.updateReminder(arrReminder[adapterPosition].iD.toString(), "false")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    override fun onCreateViewHolder(pos: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_reminder, pos, false))
    }

    override fun getItemCount(): Int {
        return arrReminder.size
    }

    override fun onBindViewHolder(vHolder: ViewHolder, pos: Int) {

        try {
            vHolder.txtTime.text = arrReminder[pos].time

            val strDays = arrReminder[pos].days!!.split(",").sorted()

            vHolder.swtReminderOnOff.isChecked = arrReminder[pos].active == "true"

            vHolder.txtRepeat.text = ""

            for (i in 0 until strDays.size) {
                if (vHolder.txtRepeat.text.toString().isEmpty()) {
                    vHolder.txtRepeat.text = getDayName(strDays[i])
                } else {
                    vHolder.txtRepeat.append((", ").plus(getDayName(strDays[i])))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getDayName(dayNo: String): String {
        var dayName = ""
        when (dayNo) {
            "1" -> dayName = "Mon"
            "2" -> dayName = "Tue"
            "3" -> dayName = "Wed"
            "4" -> dayName = "Thu"
            "5" -> dayName = "Fri"
            "6" -> dayName = "Sat"
            "7" -> dayName = "Sun"
        }
        return dayName
    }

    /*private fun confirmDeleteReminder(content: Context, strTitle: String, strMsg: String,adapterPosition:Int): Boolean {

        val builder = AlertDialog.Builder(content)
        builder.setTitle(strTitle)
        builder.setMessage(strMsg)
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") { dialog, id ->
            try {
                val dbHelper = DataHelper(context)
                dbHelper.deleteReminder(arrReminder[adapterPosition].iD.toString())
                arrReminder.removeAt(adapterPosition)
                notifyDataSetChanged()
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setNegativeButton("No") { dialog, id ->
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()

        return false
    }*/
    private fun confirmDeleteReminder(content: Context, strTitle: String, strMsg: String,adapterPosition:Int): Boolean {

        val builder = AlertDialog.Builder(content)
        builder.setTitle(strTitle)
        builder.setMessage(strMsg)
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") { dialog, id ->
            /*val dbHelper = DataHelper(context)
            dbHelper.deleteReminder(arrReminder[adapterPosition].iD.toString())
            arrReminder.removeAt(adapterPosition)
            notifyDataSetChanged()
            dialog.cancel()*/
            try {
                val rb = ReminderDatabase(context)

                rb.deleteReminder(arrReminder[adapterPosition].iD.toString())
//                val dbHelper = DataHelper(context)
//                dbHelper.deleteReminder(arrReminder[adapterPosition].iD.toString())
                AlarmReceiver().cancelAlarm(context,arrReminder[adapterPosition].iD)
                arrReminder.removeAt(adapterPosition)
                notifyDataSetChanged()
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setNegativeButton("No") { dialog, id ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()

        return false
    }

}
