package com.utillity.objects

import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.utillity.R

object CommonDialog {

    fun soundOptionDialog(context: Context) {

        var boolOtherClick = false
        var boolMuteClick = false
        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_sound_option)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val swtMute = dialog.findViewById(R.id.swtMute) as Switch
        val swtVoiceGuide = dialog.findViewById(R.id.swtVoiceGuide) as Switch
        val swtCoachTips = dialog.findViewById(R.id.swtCoachTips) as Switch
        val btnOk = dialog.findViewById(R.id.btnOk) as TextView

        swtVoiceGuide.isChecked = LocalDB.getVoiceGuide(context)

        swtCoachTips.isChecked = LocalDB.getCoachTips(context)

        if (LocalDB.getSoundMute(context)) {
            swtMute.isChecked = true
            swtCoachTips.isChecked = false
            swtVoiceGuide.isChecked = false
        }

        swtMute.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolOtherClick) {
                        boolMuteClick = true
                        if (isChecked) {
                            LocalDB.setSoundMute(context, true)
                            swtVoiceGuide.isChecked = false
                            swtCoachTips.isChecked = false

                        } else {
                            LocalDB.setSoundMute(context, false)
                            swtVoiceGuide.isChecked = LocalDB.getVoiceGuide(context)
                            swtCoachTips.isChecked = LocalDB.getCoachTips(context)
                        }
                        boolMuteClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        swtVoiceGuide.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolMuteClick) {
                        boolOtherClick = true
                        if (isChecked) {
                            swtMute.isChecked = false
                            LocalDB.setSoundMute(context, false)
                            LocalDB.setVoiceGuide(context, true)
                        } else {
                            LocalDB.setVoiceGuide(context, false)
                        }
                        boolOtherClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        swtCoachTips.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                try {
                    if (!boolMuteClick) {
                        boolOtherClick = true
                        if (isChecked) {
                            swtMute.isChecked = false
                            LocalDB.setSoundMute(context, false)
                            LocalDB.setCoachTips(context, true)
                        } else {
                            LocalDB.setCoachTips(context, false)
                        }
                        boolOtherClick = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        btnOk.setOnClickListener {
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    fun setDurationDialog(context: Context, type: String, tvDuration: TextView) {

        var duration = 0
        var maxDuration = 0
        var minDuration = 0
        var startMillis: Long = 0

        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_set_duration)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val tvDlDurationTitle = dialog.findViewById(R.id.tvDlDurationTitle) as TextView
        val tvDurations = dialog.findViewById(R.id.tvDurations) as TextView
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvSet = dialog.findViewById(R.id.tvSet) as TextView

        val btnPrev = dialog.findViewById(R.id.btnPrev) as ImageButton
        val btnNext = dialog.findViewById(R.id.btnNext) as ImageButton

        if (type == CommonString.DL_COUNT_DOWN_TIME) {
            tvDlDurationTitle.text = "Set duration (10 ~ 15 secs)"
            duration = LocalDB.getCountDownTime(context)

            maxDuration = 15
            minDuration = 10
        } else if (type == CommonString.DL_REST_SET) {
            tvDlDurationTitle.text = "Set duration (5 ~ 180 secs)"
            duration = LocalDB.getRestTime(context)

            maxDuration = 180
            minDuration = 5
        }

        tvDurations.text = duration.toString()

        btnPrev.setOnTouchListener { _, event ->

            try {
                val time = System.currentTimeMillis()
                if (startMillis == (0).toLong() || (time - startMillis > 500)) {
                    startMillis = time
                    //                count = 1
                    if (duration > minDuration) {
                        duration--
                        tvDurations.text = duration.toString()
                    }
                }
                if (MotionEvent.ACTION_UP == event.action) {
                    startMillis = 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }

        btnNext.setOnTouchListener { v, event ->

            try {
                val time = System.currentTimeMillis()
                if (startMillis == (0).toLong() || (time - startMillis > 500)) {
                    startMillis = time
                    if (duration < maxDuration) {
                        duration++
                        tvDurations.text = duration.toString()
                    }
                }
                if (MotionEvent.ACTION_UP == event.action) {
                    startMillis = 0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }

        tvCancel.setOnClickListener {
            try {
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tvSet.setOnClickListener {
            try {
                if (type == CommonString.DL_COUNT_DOWN_TIME) {
                    LocalDB.setCountDownTime(context, duration)
                } else if (type == CommonString.DL_REST_SET) {
                    LocalDB.setRestTime(context, duration)
                }
                tvDuration.text = duration.toString().plus(" secs")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dialog.cancel()
        }

        dialog.show()
    }


    fun setWeekDayGoalDialog(context: Context, edWeekDayGoal: EditText) {

        var curGoalDay = LocalDB.getWeekGoalDay(context)

        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_set_week_goal)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnOkay = dialog.findViewById(R.id.btnOkay) as Button
        val npWeeklyDayGoal = dialog.findViewById(R.id.npWeeklyDayGoal) as com.shawnlin.numberpicker.NumberPicker

        val data = arrayOf("1", "2", "3", "4", "5", "6", "7")

        npWeeklyDayGoal.minValue = 1
        npWeeklyDayGoal.maxValue = data.size
        npWeeklyDayGoal.displayedValues = data
        npWeeklyDayGoal.wrapSelectorWheel = false
        npWeeklyDayGoal.value = curGoalDay

        npWeeklyDayGoal.setOnValueChangedListener(object : com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: com.shawnlin.numberpicker.NumberPicker?, oldVal: Int, newVal: Int) {
                curGoalDay = newVal
            }
        })

        btnCancel.setOnClickListener {

            dialog.cancel()
        }

        btnOkay.setOnClickListener {
            try {
                edWeekDayGoal.setText(curGoalDay.toString())
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    fun setFirstWeekDayDialog(context: Context, edFirstDayOfWeek: EditText) {

        var curWeekName = LocalDB.getFirstDayOfWeek(context)

        val dialog = Dialog(context)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dl_first_day_of_week)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnOkay = dialog.findViewById(R.id.btnOkay) as Button
        val npWeeklyDayGoal = dialog.findViewById(R.id.npWeeklyDayGoal) as com.shawnlin.numberpicker.NumberPicker

        val data = arrayOf("Sunday", "Monday", "Saturday")

        npWeeklyDayGoal.minValue = 1
        npWeeklyDayGoal.maxValue = data.size
        npWeeklyDayGoal.displayedValues = data
        npWeeklyDayGoal.wrapSelectorWheel = false
        npWeeklyDayGoal.value = curWeekName

        npWeeklyDayGoal.setOnValueChangedListener(object : com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: com.shawnlin.numberpicker.NumberPicker?, oldVal: Int, newVal: Int) {
                curWeekName = newVal
            }
        })

        btnCancel.setOnClickListener {
            dialog.cancel()
        }

        btnOkay.setOnClickListener {
            try {
                edFirstDayOfWeek.setText(CommonUtility.getFirstWeekDayNameByDayNo(curWeekName))
                dialog.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dialog.show()
    }

    fun adDimLightProgressDialog(context: Context): Dialog {

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.anim_ad_progress)

        val alDialog = builder.create()
        alDialog.show()

        alDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        return alDialog
    }



}
