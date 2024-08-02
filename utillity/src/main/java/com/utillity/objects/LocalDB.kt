package com.utillity.objects

import android.content.Context
import android.content.SharedPreferences

object LocalDB {

    @Synchronized
    private fun getPrefrense(context: Context): SharedPreferences {
        val sharedPreferences: SharedPreferences
        synchronized(LocalDB::class.java) {
            sharedPreferences = context.getSharedPreferences("arm_workout", 0)
        }
        return sharedPreferences
    }

    fun getWeightUnit(context: Context): String? {
        return getPrefrense(context).getString(CommonString.PREF_WEIGHT_UNIT, CommonString.DEF_KG)
    }

    fun setWeightUnit(context: Context, value: String) {
        getPrefrense(context).edit().putString(CommonString.PREF_WEIGHT_UNIT, value).apply()
    }

    fun getHeightUnit(context: Context): String? {
        return getPrefrense(context).getString(CommonString.PREF_HEIGHT_UNIT, CommonString.DEF_IN)
    }

    fun setHeightUnit(context: Context, value: String) {
        getPrefrense(context).edit().putString(CommonString.PREF_HEIGHT_UNIT, value).apply()
    }

    fun setLastInputWeight(context: Context, weight: Float) {
        getPrefrense(context).edit().putFloat(CommonString.PREF_LAST_INPUT_WEIGHT, weight).apply()
    }

    fun getLastInputWeight(context: Context): Float {
        return getPrefrense(context).getFloat(CommonString.PREF_LAST_INPUT_WEIGHT, 0f)
    }

    fun setLastInputFoot(context: Context, weight: Int) {
        getPrefrense(context).edit().putInt(CommonString.PREF_LAST_INPUT_FOOT, weight).apply()
    }

    fun getLastInputFoot(context: Context): Int {
        return getPrefrense(context).getInt(CommonString.PREF_LAST_INPUT_FOOT, 0)
    }

    fun setLastInputInch(context: Context, weight: Float) {
        getPrefrense(context).edit().putFloat(CommonString.PREF_LAST_INPUT_INCH, weight).apply()
    }

    fun getLastInputInch(context: Context): Float {
        return getPrefrense(context).getFloat(CommonString.PREF_LAST_INPUT_INCH, 0f)
    }

    fun setIsFirstTime(context: Context, isFirstTime: Boolean) {
        getPrefrense(context).edit().putBoolean(CommonString.PREF_LAST_COMPLETE_DAY_ID, isFirstTime).apply()
    }

    fun getIsFirstTime(context: Context): Boolean {
        return getPrefrense(context).getBoolean(CommonString.PREF_LAST_COMPLETE_DAY_ID, true)
    }

    fun setSetupPlan(context: Context, planType: String) {
        getPrefrense(context).edit().putString(CommonString.PREF_PLAN_SETUP, planType).apply()
    }



    fun setSoundMute(context: Context, isFirstTime: Boolean) {
        getPrefrense(context).edit().putBoolean(CommonString.PREF_MUTE, isFirstTime).apply()
    }

    fun getSoundMute(context: Context): Boolean {
        return getPrefrense(context).getBoolean(CommonString.PREF_MUTE, false)
    }

    fun setVoiceGuide(context: Context, isFirstTime: Boolean) {
        getPrefrense(context).edit().putBoolean(CommonString.PREF_VOICE_GUIDE, isFirstTime).apply()
    }

    fun getVoiceGuide(context: Context): Boolean {
        return getPrefrense(context).getBoolean(CommonString.PREF_VOICE_GUIDE, true)
    }

    fun setCoachTips(context: Context, isFirstTime: Boolean) {
        getPrefrense(context).edit().putBoolean(CommonString.PREF_COACH_TIPS, isFirstTime).apply()
    }

    fun getCoachTips(context: Context): Boolean {
        return getPrefrense(context).getBoolean(CommonString.PREF_COACH_TIPS, true)
    }

    fun setCountDownTime(context: Context, weight: Int) {
        getPrefrense(context).edit().putInt(CommonString.PREF_COUNTDOWN_TIME, weight).apply()
    }

    fun getCountDownTime(context: Context): Int {
        return getPrefrense(context).getInt(CommonString.PREF_COUNTDOWN_TIME, 15)
    }

    fun setRestTime(context: Context, weight: Int) {
        getPrefrense(context).edit().putInt(CommonString.PREF_REST_TIME, weight).apply()
    }

    fun getRestTime(context: Context): Int {
        return getPrefrense(context).getInt(CommonString.PREF_REST_TIME, 30)
    }

    fun setWeekGoalDay(context: Context, dayName: Int) {
        getPrefrense(context).edit().putInt(CommonString.PREF_WEEK_GOAL_DAYS, dayName).apply()
    }

    fun getWeekGoalDay(context: Context): Int {
        return getPrefrense(context).getInt(CommonString.PREF_WEEK_GOAL_DAYS, 7)
    }

    fun setFirstDayOfWeek(context: Context, dayName: Int) {
        getPrefrense(context).edit().putInt(CommonString.PREF_FIRST_DAY_OF_WEEK, dayName).apply()
    }

    fun getFirstDayOfWeek(context: Context): Int {
        return getPrefrense(context).getInt(CommonString.PREF_FIRST_DAY_OF_WEEK, 1)
    }

    fun setKeepScreen(context: Context, isFirstTime: Boolean) {
        getPrefrense(context).edit().putBoolean(CommonString.PREF_COACH_TIPS, isFirstTime).apply()
    }

    fun getKeepScreen(context: Context): Boolean {
        return getPrefrense(context).getBoolean(CommonString.PREF_COACH_TIPS, true)
    }

    fun setBirthDate(context: Context, dateOfYear: String) {
        getPrefrense(context).edit().putString(CommonString.PREF_BIRTH_DATE, dateOfYear).apply()
    }

    fun getBirthDate(context: Context): String? {
        return getPrefrense(context).getString(CommonString.PREF_BIRTH_DATE, CommonString.DEF_BIRTH)
    }

    fun setGender(context: Context, strGender: String) {
        getPrefrense(context).edit().putString(CommonString.PREF_GENDER, strGender).apply()
    }

    fun getGender(context: Context): String? {
        return getPrefrense(context).getString(CommonString.PREF_GENDER, "Male")
    }

    fun setLastCompletedDay(context: Context, strPlanId: Int, strLastDayName: Int) {

        when (strPlanId) {
            1 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_DAY_COMPLETE_BEGINNER, strLastDayName + 1).apply()
            2 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_DAY_COMPLETE_INTERMEDIATE, strLastDayName + 1).apply()
            3 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_DAY_COMPLETE_ADVANCED, strLastDayName + 1).apply()
        }

    }


    fun setLastUnCompletedExPos(context: Context, strPlanId: Int, strLastDayName: String, NumLastPosition: Int) {

        when (strPlanId) {
            1 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_UN_COMPLETE_BEGINNER_DAY + strLastDayName, NumLastPosition).apply()
            2 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_UN_COMPLETE_INTERMEDIATE_DAY + strLastDayName, NumLastPosition).apply()
            3 -> getPrefrense(context).edit().putInt(CommonString.PREF_LAST_UN_COMPLETE_ADVANCED_DAY + strLastDayName, NumLastPosition).apply()
        }

    }

    fun getLastUnCompletedExPos(context: Context, strPlanId: Int, strLastDayName: String): Int {
        when (strPlanId) {
            1 -> return getPrefrense(context).getInt(CommonString.PREF_LAST_UN_COMPLETE_BEGINNER_DAY + strLastDayName, 0)
            2 -> return getPrefrense(context).getInt(CommonString.PREF_LAST_UN_COMPLETE_INTERMEDIATE_DAY + strLastDayName, 0)
            3 -> return getPrefrense(context).getInt(CommonString.PREF_LAST_UN_COMPLETE_ADVANCED_DAY + strLastDayName, 0)
        }
        return 0
    }

    fun setInteger(context: Context,key:String,value: Int){
        getPrefrense(context).edit().putInt(key, value).apply()
    }

    fun getInteger(context: Context,key: String,value: Int):Int{
        return getPrefrense(context).getInt(key,value)
    }


    fun setString(context: Context,key:String,value: String){
        getPrefrense(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context,key: String,value: String):String{
        return getPrefrense(context).getString(key,value)!!
    }

    fun setBoolean(context: Context,value:Boolean,key:String){
        getPrefrense(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context,value:Boolean,key:String): Boolean {
        return getPrefrense(context).getBoolean(key, value)
    }

}
