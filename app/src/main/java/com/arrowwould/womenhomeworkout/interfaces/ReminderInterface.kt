package com.arrowwould.womenhomeworkout.interfaces

interface ReminderInterface {

    fun editTime(Id: String, isEdit: Boolean, arrOfDaysArgs: ArrayList<String>,hour:Int,minute:Int)

    fun editDays(Id: String, isEdit: Boolean, arrOfDaysArgs: ArrayList<String>)

}