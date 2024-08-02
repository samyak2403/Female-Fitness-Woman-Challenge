
package com.arrowwould.womenhomeworkout.reminderNew

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class BootReceiver : BroadcastReceiver() {
    private val mTitle: String? = null
    private var mTime: String? = null
    private var mDate: String? = null
    private var mRepeatNo: String? = null
    private var mRepeatType: String? = null
    private var mActive: String? = null
    private var mRepeat: String? = null
    private var mDateSplit: Array<String>?=null
    private var mTimeSplit: Array<String>?=null
    private var mYear = 0
    private var mMonth = 0
    private var mHour = 0
    private var mMinute = 0
    private var mDay = 0
    private var mReceivedID = 0
    private var mRepeatTime: Long = 0
    private var mCalendar: Calendar? = null
    private var mAlarmReceiver: AlarmReceiver? = null
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("TAG", "onReceive:inside:::: " )
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val rb = ReminderDatabase(context)
            mCalendar = Calendar.getInstance()
            mAlarmReceiver = AlarmReceiver()
            val reminders = rb.allReminders
            Log.e("TAG", "onReceive::::Boot:: ")
            for (rm in reminders) {
                mReceivedID = rm.iD
                mRepeat = rm.repeat
                mRepeatNo = rm.repeatNo
                mRepeatType = rm.repeatType
                mActive = rm.active
                mDate = rm.date
                mTime = rm.time
                mDateSplit = mDate!!.split("/".toRegex()).toTypedArray()
                mTimeSplit = mTime!!.split(":".toRegex()).toTypedArray()
                mDay = mDateSplit!![0].toInt()
                mMonth = mDateSplit!![1].toInt()
                mYear = mDateSplit!![2].toInt()
                mHour = mTimeSplit!![0].toInt()
                mMinute = mTimeSplit!![1].toInt()
                mCalendar!!.set(Calendar.MONTH, --mMonth)
                mCalendar!!.set(Calendar.YEAR, mYear)
                mCalendar!!.set(Calendar.DAY_OF_MONTH, mDay)
                mCalendar!!.set(Calendar.HOUR_OF_DAY, mHour)
                mCalendar!!.set(Calendar.MINUTE, mMinute)
                mCalendar!!.set(Calendar.SECOND, 0)

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);

                // Check repeat type
                if (mRepeatType == "Minute") {
                    mRepeatTime = mRepeatNo!!.toInt() * milMinute
                } else if (mRepeatType == "Hour") {
                    mRepeatTime = mRepeatNo!!.toInt() * milHour
                } else if (mRepeatType == "Day") {
                    mRepeatTime = mRepeatNo!!.toInt() * milDay
                } else if (mRepeatType == "Week") {
                    mRepeatTime = mRepeatNo!!.toInt() * milWeek
                } else if (mRepeatType == "Month") {
                    mRepeatTime = mRepeatNo!!.toInt() * milMonth
                }

                // Create a new notification
                if (mActive == "true") {
                    if (mRepeat == "true") {
                        mAlarmReceiver!!.setRepeatAlarm(context, mCalendar!!, mReceivedID, mRepeatTime)
                    } else if (mRepeat == "false") {
                        mAlarmReceiver!!.setAlarm(context, mCalendar!!, mReceivedID)
                    }
                }
            }
        }
    }

    companion object {
        // Constant values in milliseconds
        private const val milMinute = 60000L
        private const val milHour = 3600000L
        private const val milDay = 86400000L
        private const val milWeek = 604800000L
        private const val milMonth = 2592000000L
    }
}