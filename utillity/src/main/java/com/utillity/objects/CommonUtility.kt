package com.utillity.objects

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.PowerManager
import android.text.format.DateFormat
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.utillity.R
import com.utillity.db.DataHelper
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt


object CommonUtility {

    fun getStringFormat(str: Double): String {
        return String.format("%.2f", str).replace(",", ".")
    }

    fun getString2DFormat(str: Int): String {
        return String.format("%02d", str).replace(",", ".")
    }

    fun getBmiCalculation(kg: Float, foot: Int, inch: Int): Double {
        val bmi = kg / getMeter(ftInToInch(foot, inch.toDouble())).pow(2.0)
        return bmi
    }

    fun calculationForBmiGraph(point: Float): Float {

        var pos = 0f
        try {

            if (point < 15) {
                return 0f
            } else if (point > 15 && point <= 16) {

                pos = ((point - 15f) * 0.5f) / 1f
                pos -= 0.05f

            } else if (point > 16 && point <= 18.5) {

                pos = ((point - 16f) * 1.5f) / 2.5f
                pos += 0.5f
                pos -= 0.05f


            } else if (point > 18.5 && point <= 25) {

                pos = ((point - 18.5f) * 2f) / 6.5f
                pos += 0.5f + 1.5f
                pos -= 0.05f


            } else if (point > 25 && point <= 30) {

                pos = ((point - 25f) * 1f) / 5f
                pos += 0.5f + 1.5f + 2f
                pos -= 0.05f



            } else if (point > 30 && point <= 35) {

                pos = ((point - 30f) * 1f) / 5f
                pos += 0.5f + 1.5f + 2f + 1f
                pos -= 0.05f


            } else if (point > 35 && point <= 40) {

                pos = ((point - 35f) * 1f) / 5f
                pos += 0.5f + 1.5f + 2f + 1f + 1f
                pos -= 0.05f



            } else if (point > 40) {
                return 6.90f
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pos
    }

    fun bmiWeightString(point: Float): String {

        try {
            if (point < 15) {
                return "Severely underweight"
            } else if (point > 15 && point < 16) {
                return "Very underweight"
            } else if (point > 16 && point < 18.5) {
                return "Underweight"
            } else if (point > 18.5 && point < 25) {
                return "Healthy Weight"
            } else if (point > 25 && point < 30) {
                return "Overweight"
            } else if (point > 30 && point < 35) {
                return "Moderately obese"
            } else if (point > 35 && point < 40) {
                return "Very obese"
            } else if (point > 40) {
                return "Severely obese"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun bmiWeightTextColor(context: Context, point: Float): Int {

        try {
            if (point < 15) {
                return ContextCompat.getColor(context, R.color.colorBlack)
            } else if (point > 15 && point < 16) {
                return ContextCompat.getColor(context, R.color.colorFirst)
            } else if (point > 16 && point < 18.5) {
                return ContextCompat.getColor(context, R.color.colorSecond)
            } else if (point > 18.5 && point < 25) {
                return ContextCompat.getColor(context, R.color.colorThird)
            } else if (point > 25 && point < 30) {
                return ContextCompat.getColor(context, R.color.colorFour)
            } else if (point > 30 && point < 35) {
                return ContextCompat.getColor(context, R.color.colorFive)
            } else if (point > 35 && point < 40) {
                return ContextCompat.getColor(context, R.color.colorSix)
            } else if (point > 40) {
                return ContextCompat.getColor(context, R.color.colorBlack)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ContextCompat.getColor(context, R.color.colorBlack)
    }

    fun getMeter(inch: Double): Double {
        return inch * 0.0254
    }

    fun ftInToInch(ft: Int, `in`: Double): Double {
        return (ft * 12).toDouble() + `in`
    }

    fun calcInchToFeet(inch: Double): Int {
        return (inch / 12.0).toInt()
    }

    fun calcInFromInch(inch: Double): Double {
        return BigDecimal(inch % 12.0).setScale(1, 6).toDouble()
    }

    fun unitFormat(i: Int): String {
        return if (i < 0 || i >= 10) {
            "" + i
        } else "0$i"
    }

    fun lbToKg(weightValue: Double): Double {
        return weightValue / 2.2046226218488
    }

    fun kgToLb(weightValue: Double): Double {
        return weightValue * 2.2046226218488
    }

    fun cmToInch(heightValue: Double): Double {
        Log.d("<><><>Cm to Inch", (heightValue / 2.54).toString())
        return heightValue / 2.54
    }

    fun inchToCm(heightValue: Double): Double {
        Log.d("<><><>inch to cm", (heightValue * 2.54).toString())
        return heightValue * 2.54
    }

    fun timeToSecond(strTime: String): Int {

        val min = strTime.substring(0, strTime.indexOf(":")).toInt() * 60
        val sec = strTime.substring(strTime.indexOf(":") + 1).toInt()

        return (min + sec)
    }

    fun secToTime(time: Int): String {
        return if (time <= 0) {
            "00:00"
        } else unitFormat(time / 60) + ":" + unitFormat(time % 60)
    }

    fun shareStringLink(content: Context, strSubject: String, strText: String) {
        try {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, strText)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, strSubject)
            shareIntent.type = "text/plain"
            content.startActivity(Intent.createChooser(shareIntent, content.resources.getString(R.string.app_name)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun contactUs(content: Context) {
        try {
            val sendIntentGmail = Intent(Intent.ACTION_SEND)
            sendIntentGmail.type = "plain/text"
            sendIntentGmail.setPackage("com.google.android.gm")
            sendIntentGmail.putExtra(Intent.EXTRA_EMAIL, arrayOf(content.getString(R.string.contact_us)))
            sendIntentGmail.putExtra(Intent.EXTRA_SUBJECT, content.resources.getString(R.string.app_name)+" - Android")
            content.startActivity(sendIntentGmail)
        } catch (e: Exception) {
            val sendIntentIfGmailFail = Intent(Intent.ACTION_SEND)
            sendIntentIfGmailFail.type = "*/*"
            sendIntentIfGmailFail.putExtra(Intent.EXTRA_EMAIL, arrayOf(content.getString(R.string.contact_us)))
            sendIntentIfGmailFail.putExtra(Intent.EXTRA_SUBJECT, content.resources.getString(R.string.app_name)+" - Android")
            if (sendIntentIfGmailFail.resolveActivity(content.packageManager) != null) {
                content.startActivity(sendIntentIfGmailFail)
            }
        }
    }

    fun rateUs(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${appPackageName}")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${appPackageName}")
                )
            )
        }
    }

    fun openUrl(content: Context, strUrl: String) {
        try {
            content.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(strUrl)))
        } catch (e: android.content.ActivityNotFoundException) {
            content.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(strUrl)))
        }
    }

    fun downloadTTSEngine(context: Context) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=text to speech&c=apps")))
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/search?q=text to speech&c=apps")
                )
            )
        }
    }

    fun convertDate(dateInMilliseconds: Long, dateFormat: String): String {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString()
    }

    fun convertDateStrToInputFormat(strDate: String, strDateFormat: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat(strDateFormat, Locale.ENGLISH)
        val date = originalFormat.parse(strDate)
        return targetFormat.format(date!!)
    }

    fun convertDateToDateMonthName(strDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("MMM dd", Locale.ENGLISH)
        val date = originalFormat.parse(strDate)
        return targetFormat.format(date!!)
    }

    fun convertLongToDay(strDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        val date = originalFormat.parse(strDate)
        return targetFormat.format(date!!)
    }

    fun getCurrentTimeStamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date()).toString()
    }

    fun getStringToDate(strDt: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = formatter.parse(strDt) as Date
        return date
    }

    fun convertFullDateToDate(strDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = originalFormat.parse(strDate)
        return targetFormat.format(date!!)
    }

    fun getFullDateStringToMilliSecond(strDt: String): Long {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = formatter.parse(strDt) as Date
        val mills = date.time
        return mills
    }

    fun getCurrentWeekByFirstDay(context: Context): ArrayList<String> {
        val currentWeekArrayList = ArrayList<String>()
        try {
            val format = SimpleDateFormat(CommonString.CapDateFormatDisplay, Locale.ENGLISH)
            val calendar = Calendar.getInstance(Locale.ENGLISH)

            when (getFirstWeekDayNameByDayNo(LocalDB.getFirstDayOfWeek(context))) {
                "Sunday" -> {
                    calendar.firstDayOfWeek = Calendar.SUNDAY
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                }
                "Monday" -> {
                    calendar.firstDayOfWeek = Calendar.MONDAY
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                }
                "Saturday" -> {
                    calendar.firstDayOfWeek = Calendar.SATURDAY
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                }
            }

            for (i in 1..7) {
                try {
                    val data = format.format(calendar.time)
                    currentWeekArrayList.add(data)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentWeekArrayList
    }

    fun getFirstWeekDayNameByDayNo(dayNo: Int): String {
        var dayNumber = ""
        when (dayNo) {
            1 -> dayNumber = "Sunday"
            2 -> dayNumber = "Monday"
            3 -> dayNumber = "Saturday"
        }
        return dayNumber
    }

    fun getFirstWeekDayNoByDayName(dayName: String): Int {
        var dayNumber = 1
        when (dayName) {
            "Sunday" -> dayNumber = 1
            "Monday" -> dayNumber = 2
            "Saturday" -> dayNumber = 3
        }
        return dayNumber
    }

    fun getCurrentWeek(): ArrayList<String> {
        val currentWeekArrayList = ArrayList<String>()
        try {
            val format = SimpleDateFormat(CommonString.CapDateFormatDisplay, Locale.ENGLISH)
            val calendar = Calendar.getInstance(Locale.ENGLISH)
            calendar.firstDayOfWeek = Calendar.SUNDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            for (i in 1..7) {
                try {
                    val data = format.format(calendar.time)
                    currentWeekArrayList.add(data)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currentWeekArrayList
    }

    fun setDayProgressData(content: Context, catId: String, txtDayLeft: TextView, txtDayPer: TextView, pbDay: ProgressBar) {
        try {
            val dbHelper = DataHelper(content)

            val compDay = dbHelper.getCompleteDayCountByPlanId(catId)

            val proPercentage = getStringFormat(((((compDay).toFloat()) * 100) / 28).toDouble())
            txtDayLeft.text = (28 - compDay).toString().plus(" Days left")
            txtDayPer.text = proPercentage.toDouble().roundToInt().toString().plus("%")

            pbDay.progress = proPercentage.toFloat().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun setIconTintColorTheme(context: Context, imageView: ImageView) {
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorTheme), PorterDuff.Mode.SRC_IN);
    }

    fun setIconTintColorGray(context: Context, imageView: ImageView) {
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorGray), PorterDuff.Mode.SRC_IN);
    }

    fun setIconTintColorThemeTextView(context: Context, textView: TextView) {
        textView.background.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorTheme), PorterDuff.Mode.SRC_IN)
    }
}
