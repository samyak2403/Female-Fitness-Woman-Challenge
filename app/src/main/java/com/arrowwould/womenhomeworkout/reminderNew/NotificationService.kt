package com.arrowwould.womenhomeworkout.reminderNew

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.arrowwould.womenhomeworkout.R
import com.arrowwould.womenhomeworkout.activity.MainActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationService : Service() {


    var mTimer: Timer? = null //timer handling


    override fun onCreate() {
        super.onCreate()


        // Timer task makes your service will repeat after every 20 Sec.
        /*val doAsynchronousTask: TimerTask = object : TimerTask() {
            override fun run() {
                Handler().post(Runnable {
                    // Add your code here.
                })
            }
        }
        //Starts after 20 sec and will repeat on every 20 sec of time interval.
        timer.schedule(doAsynchronousTask, 20000, 20000) // 20 sec timer 
*/

        if (mTimer != null) // Cancel if already existed
            mTimer!!.cancel()
        else
            mTimer = Timer() //recreate new

        mTimer!!.scheduleAtFixedRate(TimeDisplay(this), 1000, 60000)

    }

    //class TimeDisplay for handling task
    internal class TimeDisplay(val context: Context) : TimerTask() {
        val mHandler = Handler() //run on another Thread to avoid crash
        lateinit var reminderClass: Reminder

        override fun run() {
            // run on another thread

            mHandler.post(Runnable {
                Log.e("service is:::::::: ", "running")
                /* val intent2 = Intent(context, AlarmReceiver::class.java)
                 ContextCompat.startForegroundService(context, intent2);



                 val intent3 = Intent(context, BootReceiver::class.java)
                 ContextCompat.startForegroundService(context, intent3);*/

                val rb = ReminderDatabase(context)
                val reminders = rb.allReminders
                for (rm in reminders) {

                    reminderClass = rb.getReminder(rm.iD)!!
                    Log.e("TAG", "run:::::Array List::: " + reminderClass.time + " " + reminderClass.days + " " + reminderClass.iD+"  "+reminderClass.date)

                    var arrOfDays = ArrayList<String>()
                    if (reminderClass.days!!.contains(",")) {
                        arrOfDays = (reminderClass.days!!.split(",")) as ArrayList<String>
                    } else {
                        arrOfDays.add(reminderClass.days!!)
                    }

                    for (i in 0 until arrOfDays.size) {
                        arrOfDays[i] = arrOfDays[i].replace("'", "")
                        Log.e("TAG", "onReceive:Arraydays:::::  " + arrOfDays[i])
                    }


                    val df: DateFormat = SimpleDateFormat("h:mm")
                    val timeCurrent: String = df.format(Calendar.getInstance().time)

                    val df2: DateFormat = SimpleDateFormat("dd/M/yyyy")
                    val dateCurrent: String = df2.format(Calendar.getInstance().time)


                    val dayNumber = getDayNumber(getCurrentDayName().toUpperCase())

                    Log.e("TAG+", "run:::current::: "+dayNumber+"  "+timeCurrent +"  "+reminderClass.time+"  "+dateCurrent+" " +reminderClass.date)

                     if (arrOfDays.contains(dayNumber) && reminderClass.time.equals(timeCurrent) &&  reminderClass.date.equals(dateCurrent)) {
                         Log.e("TAG", "onReceive::::Day Number::::: $dayNumber")
                         fireNotification(context, reminderClass)
                     }
                }


                /*val receiver = ComponentName(context, BootReceiver::class.java)
                val pm = context.packageManager
                pm.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )*/

            })


        }

        private fun fireNotification(context: Context, reminderClass: Reminder) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = reminderClass.iD
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelName = context.resources.getString(R.string.app_name)
            val channelDescription = "Application_name Alert"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(channelId.toString(), channelName, importance)
                mChannel.description = channelDescription
                mChannel.enableVibration(true)
                notificationManager.createNotificationChannel(mChannel)
            }

            val builder = NotificationCompat.Builder(context, channelId.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.ic_alert_sound)
                builder.color = ContextCompat.getColor(context, R.color.colorAccent)
            } else {
                builder.setSmallIcon(R.drawable.ic_alert_sound)
            }

            builder.setStyle(NotificationCompat.BigTextStyle().bigText("Your body needs energy! You haven't exercised in ${getCurrentFullDayName()}!"))
            builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            builder.setContentTitle(context.resources.getString(R.string.app_name))
            builder.setAutoCancel(true)
            builder.setOngoing(false)

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            val intent = PendingIntent.getActivity(context, 0,notificationIntent, 0)
            val intent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_ONE_SHOT
                )
            }
            builder.setContentIntent(intent)

            notificationManager.notify(reminderClass.iD.toInt(), builder.build())

        }

        fun getDayNumber(dayName: String): String {
            var dayNumber = ""
            when (dayName) {
                "MON" -> dayNumber = "1"
                "TUE" -> dayNumber = "2"
                "WED" -> dayNumber = "3"
                "THU" -> dayNumber = "4"
                "FRI" -> dayNumber = "5"
                "SAT" -> dayNumber = "6"
                "SUN" -> dayNumber = "7"
            }
            return dayNumber
        }


        private fun getCurrentFullDayName(): String {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format(cal.time)
        }

        private fun getCurrentDayName(): String {
            val cal = Calendar.getInstance()
            cal.firstDayOfWeek = Calendar.MONDAY
            val sdf = SimpleDateFormat("EEE", Locale.getDefault())
            return sdf.format(cal.time)
        }

        private fun getCurrentDate(): String {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            return sdf.format(cal.time)
        }

        private fun getEndDate(date: Date): String {
            val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            return sdf.format(date)
        }



    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // TODO do something useful
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    /*@SuppressLint("LongLogTag")
    override fun onDestroy() {
        super.onDestroy()
//        mTimer!!.cancel() //For Cancel Timer
        Log.e("service is;:::::::::::: ", "Destroyed")
    }*/
}