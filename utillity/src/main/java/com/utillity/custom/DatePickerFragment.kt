package com.utillity.custom

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.utillity.objects.LocalDB
import java.util.*

class DatePickerFragment : androidx.fragment.app.DialogFragment() {

    private lateinit var calendar: Calendar
    private var year: Int = 1990
    private var month: Int = 1
    private var day: Int = 1
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        calendar = Calendar.getInstance()


        val birthDate = LocalDB.getBirthDate(activity!!)

        if (birthDate != "1990") {
            if (birthDate != null) {
                year = birthDate.substring(0, 5).trim().toInt()//LocalDB.getBirthDate(activity!!)
                month = birthDate.substring(6, 9).trim().toInt()
                day = birthDate.substring(11, 14).trim().toInt()
            }
        } else {
            year = birthDate.substring(0, 5).trim().toInt()//LocalDB.getBirthDate(activity!!)
            month = birthDate.substring(6, 9).trim().toInt()
            day = birthDate.substring(11, 14).trim().toInt()

        }

        val dialog = DatePickerDialog(
            activity!!,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            activity as (DatePickerDialog.OnDateSetListener),
            year,
            (month - 1),
            day
        )




        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

}
